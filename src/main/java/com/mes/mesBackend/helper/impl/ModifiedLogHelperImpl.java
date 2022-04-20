//package com.mes.mesBackend.helper.impl;
//
//import com.mes.mesBackend.entity.ModifiedLog;
//import com.mes.mesBackend.entity.User;
//import com.mes.mesBackend.entity.enumeration.ModifiedDivision;
//import com.mes.mesBackend.exception.NotFoundException;
//import com.mes.mesBackend.helper.ModifiedLogHelper;
//import com.mes.mesBackend.repository.ModifiedLogRepository;
//import com.mes.mesBackend.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//// 업데이트 시 기록 생성
//@Component
//@RequiredArgsConstructor
//public class ModifiedLogHelperImpl implements ModifiedLogHelper {
//    private final UserRepository userRepo;
//    private final ModifiedLogRepository modifiedLogRepo;
//
//    // 업데이트 시 로그 생성
//    @Override
//    public <T> void createModifiedLog(String userCode, ModifiedDivision modifiedDivision, T t) throws NotFoundException {
//        User user = getUserOrThrow(userCode);
//        ModifiedLog modifiedLog = new ModifiedLog();
//        modifiedLog.created(user.getUserCode(), modifiedDivision, user.getLevel(), t, false);
//        modifiedLogRepo.save(modifiedLog);
//    }
//
//    // 생성 시 로그 생성
//    @Override
//    public <T> void createInsertLog(String userCode, ModifiedDivision modifiedDivision, T t) throws NotFoundException {
//        User user = getUserOrThrow(userCode);
//        ModifiedLog modifiedLog = new ModifiedLog();
//        modifiedLog.created(user.getUserCode(), modifiedDivision, user.getLevel(), t, true);
//        modifiedLogRepo.save(modifiedLog);
//    }
//
//    // 업데이트 기록 조회
//    @Override
//    public ModifiedLog getModifiedLog(ModifiedDivision modifiedDivision, Long divisionId) {
//        return modifiedLogRepo.findByModifiedDivisionId(divisionId, modifiedDivision)
//                .orElse(null);
//    }
//
//    // 생성 기록 조회
//    @Override
//    public ModifiedLog getInsertLog(ModifiedDivision modifiedDivision, Long divisionId) {
//        return modifiedLogRepo.findByInsertDivisionId(divisionId, modifiedDivision)
//                .orElse(null);
//    }
//
//    // userCode 로 user 조회 및 예외
//    private User getUserOrThrow(String userCode) throws NotFoundException {
//        return userRepo.findByUserCode(userCode)
//                .orElseThrow(() -> new NotFoundException("[modifiedLogHelper] user does not exists. input userCode: " + userCode));
//    }
//}
