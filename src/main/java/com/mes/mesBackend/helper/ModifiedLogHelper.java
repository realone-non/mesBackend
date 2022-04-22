//package com.mes.mesBackend.helper;
//
//import com.mes.mesBackend.entity.ModifiedLog;
//import com.mes.mesBackend.entity.enumeration.ModifiedDivision;
//import com.mes.mesBackend.exception.NotFoundException;
//
//// 업데이트 시 기록 생성
//public interface ModifiedLogHelper {
//    // 업데이트 시 로그 생성
//    <T> void createModifiedLog(String userCode, ModifiedDivision modifiedDivision, T t) throws NotFoundException;
//    // 생성 시 로그 생성
//    <T> void createInsertLog(String userCode, ModifiedDivision modifiedDivision, T t) throws NotFoundException;
//    // 업데이트 기록 조회
//    ModifiedLog getModifiedLog(ModifiedDivision modifiedDivision, Long divisionId);
//    // 생성 기록 조회
//    ModifiedLog getInsertLog(ModifiedDivision modifiedDivision, Long divisionId);
//}
