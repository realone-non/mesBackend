package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ModelMapper mapper;

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

    // 로그인
    @Override
    public UserResponse.idAndKorNameAndEmail getLogin(String userCode, String password) throws NotFoundException, NoSuchAlgorithmException, BadRequestException {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new NotFoundException("user does not exist. input userCode: " + userCode));

        // 입력받은 password를 기존 유저의 salt와 조합
        String hashing = passWordHashing(password.getBytes(), user.getSalt());

        // 기존 유저 해싱된 password와 입력받은 password의 해싱된 값과 맞는지 비교
        if (!user.getPassword().equals(hashing)) {
            throw new BadRequestException("password is not correct.");
        }

//        System.out.println("========================== password 확인 ============================");
//        System.out.println("입력받은 password: " + password);
//        System.out.println("해당 유저의 salt 값: " + user.getSalt());
//        System.out.println("해당 유저의 해싱된 password: " + user.getPassword());
//        System.out.println("입력받은 password의 해싱된 값: " + hashing);
//        System.out.println(user.getPassword().equals(hashing));
        return mapper.toResponse(user, UserResponse.idAndKorNameAndEmail.class);
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
    private String passWordHashing(byte[] password, String salt) throws NoSuchAlgorithmException {
        // SHA-256 암호와 알고리즘: 임의의 길이 메세지를 256비트의 축약된 메세지로 만들어내는 해시 알고리즘
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for (int i=0; i<10000; i++) {
            String temp = byteToString(password) + salt;    // 패스워드와 Salt를 합쳐 새로운 문자열 생성
            md.update(temp.getBytes());                     // temp의 문자열을 해싱하여 md에 저장
            password = md.digest();                         // md 객체의 다이제스트를 얻어 pass를 갱신.
        }
        return byteToString(password);
    }
}
