package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.auth.JwtTokenProvider;
import com.mes.mesBackend.auth.TokenRequest;
import com.mes.mesBackend.auth.TokenResponse;
import com.mes.mesBackend.dto.request.UserCreateRequest;
import com.mes.mesBackend.dto.request.UserLogin;
import com.mes.mesBackend.dto.request.UserRegistrationRequest;
import com.mes.mesBackend.dto.request.UserUpdateRequest;
import com.mes.mesBackend.dto.response.UserRegistrationResponse;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.RefreshToken;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.enumeration.UserType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.ClientIpHelper;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogSender;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.RefreshTokenRepository;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.UserType.NEW;
import static com.mes.mesBackend.entity.enumeration.UserType.NORMAL;
import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentService departmentService;
    private final ModelMapper mapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final int SALT_SIZE = 16;
    private static final String REFRESH_TOKEN = "refreshToken";
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private CustomLogger cLogger;
    private LogSender logSender;
    private ClientIpHelper clientIpHelper;

    // 직원(작업자) 생성
    public UserResponse createUser(UserCreateRequest userRequest) throws NotFoundException, BadRequestException {
        // 중복되는 유저코드가 있는지 체크
        checkUserCode(userRequest.getUserCode());
        // 중복되는 이메일이 있는지 체크
        checkUserEmail(userRequest.getMail());

        Department department = userRequest.getDepartment() != null ? departmentService.getDepartmentOrThrow(userRequest.getDepartment()) : null;
        User user = mapper.toEntity(userRequest, User.class);

        // salt 생성
        String salt = createSalt();
        // 솔트값, 해싱된 Password
        user.setSalt(salt);
        user.setPassword(passwordHashing(userRequest.getUserCode().getBytes(), salt));
        user.addJoin(department);
        user.setUserType(NEW);

        userRepository.save(user);
        return mapper.toResponse(user, UserResponse.class);
    }

    // 직원(작업자) 단일 조회
    public UserResponse getUser(Long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        return mapper.toResponse(user, UserResponse.class);
    }

    // 직원(작업자) 전체 조회
    public List<UserResponse> getUsers(Long departmentId, String userCode, String korName, UserType userType) {
        List<User> users = userRepository.findAllCondition(departmentId, userCode, korName, userType);
        return mapper.toListResponses(users, UserResponse.class);
    }

    // 직원(작업자) 페이징 조회
//    public Page<UserResponse> getUsers(Pageable pageable) {
//        Page<User> users = userRepository.findAllByDeleteYnFalse(pageable);
//        return mapper.toPageResponses(users, UserResponse.class);
//    }

    // 직원(작업자) 수정
    public UserResponse updateUser(Long id, UserUpdateRequest userRequest) throws NotFoundException, BadRequestException {
        Department newDepartment = userRequest.getDepartment() != null ? departmentService.getDepartmentOrThrow(userRequest.getDepartment()) : null;
        User newUser = mapper.toEntity(userRequest, User.class);
        User findUser = getUserOrThrow(id);

        // 이메일 변경 시 중복 이메일인지 체크
        if (!findUser.getMail().equals(userRequest.getMail())) checkUserEmail(userRequest.getMail());

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
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info("userCode: " + userLogin.getUserCode() + ", password: " +userLogin.getPassword() + " 으로 로그인을 시도함.");
        User user = userRepository.findByUserCode(userLogin.getUserCode())
                .orElseThrow(() -> new NotFoundException("아이디가 잘못 입력 되었습니다. 아이디를 정확히 입력해 주세요."));

        // 입력받은 password를 기존 유저의 salt와 조합
        String hashing = passwordHashing(userLogin.getPassword().getBytes(), user.getSalt());

        // 저장소에 해싱되어 있는 Password 와 입력받은 Password 의 해싱된 값과 맞는지 비교
        if (!user.getPassword().equals(hashing)) {
            cLogger.error("password is not correct.");
            throw new BadRequestException("비밀번호가 잘못 입력 되었습니다. 비밀번호를 정확히 입력해 주세요.");
        }

        List<UserType> userRoles = new ArrayList<>();
        userRoles.add(user.getUserType());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserCode(), user.getPassword(), userRoles);

        // AccessToken, RefreshToken 생성
        String accessToken = jwtTokenProvider.createAccessToken(authenticationToken);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        TokenResponse tokenDto = new TokenResponse();

        tokenDto.putToken(accessToken, refreshToken, user.getKorName(), user.getUserType());

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
    public TokenResponse reissue(TokenRequest tokenRequestDto) throws CustomJwtException, NotFoundException {
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        // 1. Refresh Token , AccessToken 검증
        jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken(), REFRESH_TOKEN);

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

        User user = userRepository.findByUserCode(authentication.getName())
                .orElseThrow(() -> new NotFoundException("아이디가 잘못 입력 되었습니다. 아이디를 정확히 입력해 주세요."));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.save(authentication.getName(), newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        TokenResponse tokenResponse = new TokenResponse();

        return tokenResponse.putToken(newAccessToken, newRefreshToken, user.getKorName(), user.getUserType());
    }

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

    // 비밀번호 초기화
    @Override
    public void resetPassword(String email) throws NotFoundException {
        User user = userRepository.findByMailAndDeleteYnFalse(email)
                .orElseThrow(() -> new NotFoundException("입력한 이메일에 해당하는 직원정보가 없습니다."));

        String salt = createSalt();
        user.setSalt(salt);
        user.setPassword(passwordHashing(user.getUserCode().getBytes(), salt));
        userRepository.save(user);
    }

    // 비밀번호 변경
    @Override
    public void updatePassword(String userCode, UserCreateRequest.password password) throws NotFoundException, BadRequestException {
        if (password.getPassword().length() <= 4) {
            throw new BadRequestException("비밀번호를 5자 이상 입력해주세요.");
        }

        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new NotFoundException("유저정보를 찾을 수 없습니다."));

        String salt = createSalt();
        user.setSalt(salt);
        user.setPassword(passwordHashing(password.getPassword().getBytes(), salt));
        userRepository.save(user);
    }

    // =============================== 18-3. 사용자 등록 ===============================
    // 사용자 생성
    @Override
    public UserRegistrationResponse createUserRegistration(UserRegistrationRequest userRegistrationRequest) throws BadRequestException, NotFoundException {
        // 중복되는 유저코드가 있는지 체크
        checkUserCode(userRegistrationRequest.getUserCode());
        // 중복되는 이메일이 있는지 체크
        checkUserEmail(userRegistrationRequest.getMail());

        User user = mapper.toEntity(userRegistrationRequest, User.class);

        // salt 생성
        String salt = createSalt();
        // 솔트값, 해싱된 Password
        user.setSalt(salt);
        user.setPassword(passwordHashing(userRegistrationRequest.getUserCode().getBytes(), salt));
        user.setUserType(NEW);

        userRepository.save(user);
        return getUserRegistration(user.getId());
    }

    // 사용자 단일 조회
    @Override
    public UserRegistrationResponse getUserRegistration(Long id) throws NotFoundException {
        return userRepository.findUserRegistrationByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + id));
    }

    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(사번), 이름, 공장(X)
    @Override
    public List<UserRegistrationResponse> getUserRegistrations(String userCode, String korName) {
        return userRepository.findUserRegistrationByCondition(userCode, korName);
    }

    // 사용자 수정
    @Override
    public UserRegistrationResponse updateUserRegistration(Long id, UserRegistrationRequest.Update userRegistrationRequest) throws NotFoundException, BadRequestException {
        User findUser = getUserOrThrow(id);

        // 이메일 변경 시 중복 이메일인지 체크
        if (!findUser.getMail().equals(userRegistrationRequest.getMail())) checkUserEmail(userRegistrationRequest.getMail());
        User newUser = mapper.toEntity(userRegistrationRequest, User.class);
        findUser.setMail(userRegistrationRequest.getMail());
        findUser.setKorName(newUser.getKorName());
        findUser.setUseYn(newUser.isUseYn());
        userRepository.save(findUser);
        return getUserRegistration(findUser.getId());
    }

    // 사용자 삭제
    @Override
    public void deleteUserRegistration(Long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        user.delete();
        userRepository.save(user);
    }


    public User getUserOrThrow(Long id) throws NotFoundException {
        return userRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("user does not exists. input id: " + id));
    }

    // userCode 가 중복되지 않게 확인
    private void checkUserCode(String userCode) throws BadRequestException {
        boolean existsByUserCode = userRepository.existsByUserCodeAndDeleteYnFalse(userCode);
        if (existsByUserCode) throw new BadRequestException("이미 존재하는 유저코드 입니다. 다른 유저코드를 입력 해주세요.");
    }
    // 중복되는 이메일이 있는지 체크
    private void checkUserEmail(String email) throws BadRequestException {
        boolean existsByMail = userRepository.existsByMailAndDeleteYnFalse(email);
        if (existsByMail) throw new BadRequestException("이미 존재하는 이메일 입니다. 다른 이메일을 입력 해주세요.");
    }
}

