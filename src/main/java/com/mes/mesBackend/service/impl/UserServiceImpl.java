package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.auth.JwtTokenProvider;
import com.mes.mesBackend.auth.TokenResponse;
import com.mes.mesBackend.auth.TokenRequest;
import com.mes.mesBackend.dto.request.UserLogin;
import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.RefreshTokenRepository;
import com.mes.mesBackend.repository.RoleRepository;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.repository.UserRoleRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

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

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

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
        user.setPassword(passwordHashing(userRequest.getPassword().getBytes(), salt));
        user.addJoin(department);

        // 권한 추가
        // RoleUser 테이블에
//        for (Long roleId : userRequest.getRoles()) {
//            Role role = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("role does not exist. input role id: " + roleId));
//            UserRole userRole = new UserRole();
//            userRole.save(user, role);
//            userRoleRepository.save(userRole);
//            user.getUserRoles().add(userRole);
//        }

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
        findUser.setPassword(passwordHashing(userRequest.getPassword().getBytes(), salt));
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

    // 로그인
    @Override
    public TokenResponse getLogin(UserLogin userLogin) throws NotFoundException, BadRequestException {
        User user = userRepository.findByUserCode(userLogin.getUserCode())
                .orElseThrow(() -> new NotFoundException("user does not exist. input userCode: " + userLogin.getUserCode()));

        // 입력받은 password를 기존 유저의 salt와 조합
        String hashing = passwordHashing(userLogin.getPassword().getBytes(), user.getSalt());

        // 저장소에 해싱되어 있는 Password 와 입력받은 Password 의 해싱된 값과 맞는지 비교
        if (!user.getPassword().equals(hashing)) {
            throw new BadRequestException("password is not correct.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserCode(), user.getPassword(), Collections.emptyList());

        // AccessToken, RefreshToken 생성
        String accessToken = jwtTokenProvider.createAccessToken(authenticationToken);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        TokenResponse tokenDto = new TokenResponse();

        tokenDto.putToken(accessToken, refreshToken);

        // 기존 저장소에 있던 RefreshToken False 로 변경
        refreshTokenUseYnTrueToUseYnFalse(authenticationToken);

        // RefreshToken 저장
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.save(authenticationToken.getName(), refreshToken);
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    // 기존에 있던 refreshToken 삭제
    private void refreshTokenUseYnTrueToUseYnFalse(Authentication authentication) {
        List<RefreshToken> findRefreshToken = refreshTokenRepository.findAllByUserCodeAndUseYnTrue(authentication.getName());
        for (RefreshToken token : findRefreshToken) {
            if (token.getUseYn()) {
                token.useYnFalse();
            }
        }
        refreshTokenRepository.saveAll(findRefreshToken);
    }

    @Override
    public TokenResponse reissue(TokenRequest tokenRequestDto) throws CustomJwtException {
        // 1. Refresh Token , AccessToken 검증
        jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken(), "refreshToken");
        jwtTokenProvider.validateToken(tokenRequestDto.getAccessToken(), "accessToken");

        // 2. Access Token user 인증정보 조회
        Authentication authentication = jwtTokenProvider.getAuthenticationFromAccessToken(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 userCode 를 기반으로 RefreshToken 값 가져옴.
        RefreshToken findRefreshToken = refreshTokenRepository.findByUserCodeAndUseYnTrue(authentication.getName())
                .orElseThrow(() -> new CustomJwtException("user have does not refresh token."));

        // 4. 입력받은 RefreshToken 과 저장소에 있는 RefreshToken 값이 일치하는지 확인.
        if (!findRefreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomJwtException("user token does not match input refresh token.");
        }

        // 5. 새로운 AccessToken, RefreshToken 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        // 6. RefreshToken 저장소 정보 업데이트
        refreshTokenUseYnTrueToUseYnFalse(authentication);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.save(authentication.getName(), newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        TokenResponse tokenResponse = new TokenResponse();

        return tokenResponse.putToken(newAccessToken, newRefreshToken);
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
    private String passwordHashing(byte[] password, String salt) {
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
