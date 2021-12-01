package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.config.JwtTokenProvider;
import com.mes.mesBackend.config.TokenDto;
import com.mes.mesBackend.config.TokenRequestDto;
import com.mes.mesBackend.dto.request.UserLogin;
import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.exception.ExpiredJwtException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.RefreshTokenRepository;
import com.mes.mesBackend.repository.RoleRepository;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.repository.UserRoleRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.UserService;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ModelMapper mapper;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    RoleRepository roleRepository;

    public User getUserOrThrow(Long id) throws NotFoundException {
        return userRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("user does not exists. input id: " + id));
    }

    // userCode가 중복되지 않게 확인
    private void checkUserCode(String userCode) throws BadRequestException {
        boolean existsByUserCode = userRepository.existsByUserCode(userCode);

        if (existsByUserCode) {
            throw new BadRequestException("user code exist. input userCode: " + userCode);
        }
    }

    // 이름, 메일주소, 프로필 이미지

    @Autowired
    UserRoleRepository userRoleRepository;

    // 직원(작업자) 생성
    public UserResponse createUser(UserRequest userRequest) throws NotFoundException, NoSuchAlgorithmException, BadRequestException {
        checkUserCode(userRequest.getUserCode());

        Department department = departmentService.getDepartmentOrThrow(userRequest.getDepartment());
        User user = mapper.toEntity(userRequest, User.class);

        String salt = createSalt();
        // 솔트값, 해싱된 Password
        user.setSalt(salt);
        user.setPassword(passWordHashing(userRequest.getPassword().getBytes(), salt));
        user.addJoin(department);

        // 권한 추가
        // RoleUser 테이블에
        for (Long roleId : userRequest.getRoles()) {
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("role does not exist. input role id: " + roleId));
            UserRole userRole = new UserRole();
            userRole.save(user, role);
            userRoleRepository.save(userRole);
            user.getUserRoles().add(userRole);
        }

        userRepository.save(user);
        return mapper.toResponse(user, UserResponse.class);
    }

    // 직원(작업자) 단일 조회
    public UserResponse getUser(Long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        return mapper.toResponse(user, UserResponse.class);
    }

    // 직원(작업자) 전체 조회
    public List<UserResponse> getUsers(Long departmentId, String userCode, String korName) {
        List<User> users = userRepository.findAllCondition(departmentId, userCode, korName);
        return mapper.toListResponses(users, UserResponse.class);
    }

    // 직원(작업자) 페이징 조회
//    public Page<UserResponse> getUsers(Pageable pageable) {
//        Page<User> users = userRepository.findAllByDeleteYnFalse(pageable);
//        return mapper.toPageResponses(users, UserResponse.class);
//    }

    // 직원(작업자) 수정
    public UserResponse updateUser(Long id, UserRequest userRequest) throws NotFoundException, NoSuchAlgorithmException {
        Department newDepartment = departmentService.getDepartmentOrThrow(userRequest.getDepartment());
        User newUser = mapper.toEntity(userRequest, User.class);
        String salt = createSalt();
        // 솔트값, 해싱된 Password
        User findUser = getUserOrThrow(id);
        findUser.setSalt(salt);
        findUser.setPassword(passWordHashing(userRequest.getPassword().getBytes(), salt));
        findUser.put(newUser, newDepartment);
        userRepository.save(findUser);
        return mapper.toResponse(findUser, UserResponse.class);
    }

    // 직원(작업자) 삭제
    public void deleteUser(Long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        user.delete();
        userRepository.save(user);
    }

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    // 로그인
    @Override
    public TokenDto getLogin(UserLogin userLogin) throws NotFoundException, BadRequestException {
        User user = userRepository.findByUserCode(userLogin.getUserCode())
                .orElseThrow(() -> new NotFoundException("user does not exist. input userCode: " + userLogin.getUserCode()));

        // 입력받은 password를 기존 유저의 salt와 조합
        String hashing = passWordHashing(userLogin.getPassword().getBytes(), user.getSalt());

        // 기존 유저 해싱된 password와 입력받은 password의 해싱된 값과 맞는지 비교
        if (!user.getPassword().equals(hashing)) {
            throw new BadRequestException("password is not correct.");
        }

        // 토큰 생성
//        TokenDto tokenDto = jwtTokenProvider.createTokenDto(user.getUserCode(), user.getRoles());
        // 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserCode(), user.getPassword(),
               //user.getAuthorities()
                Collections.emptyList()
        );

        String accessToken = jwtTokenProvider.createAccessToken(authenticationToken);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        TokenDto tokenDto = new TokenDto();

        tokenDto.putToken(accessToken, refreshToken);

        // 기존에 있던 refresh 삭제
        refreshTokenUseYnFalse(authenticationToken);

        // Refresh 객체에 저장
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.save(authenticationToken.getName(), refreshToken);
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    // 기존에 있던 refreshToken 삭제
    private void refreshTokenUseYnFalse(Authentication authentication) {
        // userCode로 RefreshToken 찾아서 userYn이 true인게 존재하면 걔를 false로 바꾼 다음 새로운 RefreshToken 생성
        List<RefreshToken> findRefreshToken = refreshTokenRepository.findAllByUserCodeAndUseYnTrue(authentication.getName());
        for (RefreshToken token : findRefreshToken) {
            if (token.getUseYn()) {
                token.useYnFalse();
            }
        }
        refreshTokenRepository.saveAll(findRefreshToken);
    }

    @Override
    public TokenDto reissue(TokenRequestDto tokenRequestDto) throws CustomJwtException {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 UserID 가져오기
        // token에서 인증정보 조회
        Authentication authentication = jwtTokenProvider.getAuthenticationFromAccessToken(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 userCode를 기반으로 refreshToken 값 가져옴.
        RefreshToken findRefreshToken = refreshTokenRepository.findByUserCodeAndUseYnTrue(authentication.getName());

//        RefreshToken refreshToken = refreshTokenRepository.findByUserCode(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자 입니다."));

        // userCode로 user가져옴
        User user = userRepository.findByUserCode(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));


        // 4. Refresh Token 일치하는지 검사
        if (!findRefreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        // 6. 저장소 정보 업데이트
        refreshTokenUseYnFalse(authentication);     // refreshToken useYn이 true인게 존재하면 false로 변경

        // Refresh 객체에 저장
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.save(authentication.getName(), newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        TokenDto tokenDto = new TokenDto();

        return tokenDto.putToken(newAccessToken, newRefreshToken);
    }

    private static final int SALT_SIZE = 16;

    // salt 값 생성
    private String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
        random.nextBytes(temp);
        return byteToString(temp);
    }

    // byte 값을 16진수로 변경
    private String byteToString(byte[] temp) {
        StringBuilder sb = new StringBuilder();
        // %02x : 2자리 hex를 대문자로, 그리고 1자리 hex는 앞에 0을 붙임.
        for (byte b : temp) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 비밀번호 해싱
    private String passWordHashing(byte[] password, String salt) {
        // SHA-256 암호와 알고리즘: 임의의 길이 메세지를 256비트의 축약된 메세지로 만들어내는 해시 알고리즘
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        for (int i=0; i<10000; i++) {
            String temp = byteToString(password) + salt;    // 패스워드와 Salt를 합쳐 새로운 문자열 생성
            md.update(temp.getBytes());                     // temp의 문자열을 해싱하여 md에 저장
            password = md.digest();                         // md 객체의 다이제스트를 얻어 pass를 갱신.
        }
        return byteToString(password);
    }
}
