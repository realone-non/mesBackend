package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.enumeration.BreakReason;
import com.mes.mesBackend.entity.enumeration.ProcessStatus;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// pop
@RequestMapping("/pop")
@Tag(name = "pop", description = "pop API")
@RestController
@RequiredArgsConstructor
public class PopController {
    private final PopService popService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PopController.class);
    private CustomLogger cLogger;

    @GetMapping("/work-processes")
    @ResponseBody
    @Operation(summary = "(pop) 작업공정 전체 조회", description = "검색조건: 재사용 공정 여부")
    public ResponseEntity<List<WorkProcessResponse>> getPopWorkProcesses(
            @RequestParam(required = false) @Parameter(description = "재사용 공정(true: 재사용 공정만 조회)") Boolean recycleYn
    ) {
        List<WorkProcessResponse> workProcesses = popService.getPopWorkProcesses(recycleYn);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info( "viewed the list of from getPopWorkProcesses.");
        return new ResponseEntity<>(workProcesses, OK);
    }

    // 작업 공정에 해당하는 설비 조회
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/equipments")
    @ResponseBody
    @Operation(summary = "(pop) 설비 목록 조회", description = "작업공정에 대한 설비 목록 <br /> " +
            "작업공정 구분: [자재입고: MATERIAL_INPUT, 원료혼합: MATERIAL_MIXING, 충진: FILLING, 캡조립: CAP_ASSEMBLY, 라벨링: LABELING, 포장: PACKAGING, 출하: SHIPMENT, 재사용 : RECYCLE]")
    public ResponseEntity<List<PopEquipmentResponse>> getPopEquipments(@RequestParam
            @Parameter(description = "작업공정 구분 값") WorkProcessDivision workProcessDivision,
            @RequestParam(required = false) @Parameter(description = "생성가능여부 구분값 (true: 생성가능, false: 생성불가능)") Boolean produceYn,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopEquipmentResponse> equipments = popService.getPopEquipments(workProcessDivision, produceYn);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopWorkOrders.");
        return new ResponseEntity<>(equipments, OK);
    }

    // 작업지시 정보 리스트 api, 조건: 작업자, 작업공정
    // 작업지시 목록(공정)
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/work-orders")
    @ResponseBody
    @Operation(
            summary = "(pop) 작업지시 정보",
            description = "조건: 작업공정 구분 값, 날짜(당일) <br /> " +
                    " 작업공정 구분: [자재입고: MATERIAL_INPUT, 원료혼합: MATERIAL_MIXING, 충진: FILLING, " +
                    "캡조립: CAP_ASSEMBLY, 라벨링: LABELING, 포장: PACKAGING, 출하: SHIPMENT, 재사용 : RECYCLE]"
    )
    public ResponseEntity<List<PopWorkOrderResponse>> getPopWorkOrders(
            @RequestParam @Parameter(description = "작업공정 구분 값") WorkProcessDivision workProcessDivision,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopWorkOrderResponse> popWorkOrderResponses = popService.getPopWorkOrders(workProcessDivision);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopWorkOrders.");
        return new ResponseEntity<>(popWorkOrderResponses, OK);
    }

    // 작업지시 진행상태 정보 조회
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/work-orders-states")
    @ResponseBody
    @Operation(
            summary = "(pop) 작업지시 진행상태 정보 조회",
            description = "[원자재 등록: MATERIAL_REGISTRATION, 중간 검사: MIDDLE_TEST, 로트 분할: LOT_DIVIDE]"
    )
    public ResponseEntity<List<PopWorkOrderStates>> getPopWorkOrderStates(
            @RequestParam @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopWorkOrderStates> popWorkOrderStates = popService.getPopWorkOrderStates(workOrderId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopWorkOrderStates.");
        return new ResponseEntity<>(popWorkOrderStates, OK);
    }

    // 작업지시 진행상태 변경
    @SecurityRequirement(name = AUTHORIZATION)
    @PutMapping("/work-order-states")
    @ResponseBody
    @Operation(
            summary = "(pop) 작업지시 진행상태 변경",
            description = "[원자재 등록: MATERIAL_REGISTRATION, 중간 검사: MIDDLE_TEST, 로트 분할: LOT_DIVIDE] <br />" +
                    "설비로트의 생산공정이 충진이고, 상태값이 MIDDLE_TEST 이면 원료혼합의 반제품이 소진된다."
    )
    public ResponseEntity updatePopWorkOrderState(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "상태 값") ProcessStatus processStatus,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        popService.updatePopWorkOrderState(lotMasterId, processStatus);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from updatePopWorkOrderState.");
        return new ResponseEntity<>(OK);
    }

    // 작업완료 수량 입력
    // 원료혼합 공정에서만 fillingEquipmentId 값이 들어오는데, 다음 공정인 충진공정에서 사용할 설비를 지정해주는 값이다.
    @SecurityRequirement(name = AUTHORIZATION)
    @PostMapping("/work-orders/{work-order-id}")
    @ResponseBody
    @Operation(summary = "(pop) 작업완료 수량 입력", description = "")
    public ResponseEntity<Long> createWorkOrder(
            @PathVariable(value = "work-order-id") @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestParam @Parameter(description = "품목 id") Long itemId,
            @RequestParam @Parameter(description = "생산 수량") int productAmount,
            @RequestParam @Parameter(description = "양품 수량") int stockAmount,
            @RequestParam @Parameter(description = "불량 수량") int badItemAmount,
            @RequestParam @Parameter(description = "설비 id") Long equipmentId,
            @RequestParam(required = false) @Parameter(description = "충진공정 설비 id") Long fillingEquipmentCode,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        Long lotId = popService.createWorkOrder(workOrderId, itemId, userCode, productAmount, stockAmount, badItemAmount, equipmentId, fillingEquipmentCode);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from getPopWorkOrders.");
        return new ResponseEntity<>(lotId, OK);
    }

    // 사용한 원자재 등록
    // 해당 품목(반제품)에 대한 원자재, 부자재 정보 가져와야함
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/bom-detail-items")
    @ResponseBody
    @Operation(
            summary = "(pop) 사용한 레시피 조회",
            description = "사용한 원부자재 리스트 조회 <br />" +
                    "bomMasterItem: 만들어진 제품, bomDetailItem: 레시피"
    )
    public ResponseEntity<List<PopBomDetailItemResponse>> getPopBomDetailItems(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopBomDetailItemResponse> popBomDetailItems = popService.getPopBomDetailItems(lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopBomDetailItems.");
        return new ResponseEntity<>(popBomDetailItems, OK);
    }

    // 원자재 LOT NO 등록 및 수정
    // 원자재, 부자재에 해당되는 lotMaster 조회
    // bomDetail 의 Item 과 같은 lotMaster 조회, 1 이상인 것만
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/bom-detail-item-lots")
    @ResponseBody
    @Operation(summary = "(pop) 원부자재에 해당되는 LOT 조회", description = "")
    public ResponseEntity<List<PopBomDetailLotMasterResponse>> getPopBomDetailLotMasters(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "품목 id") Long itemId,
            @RequestParam(required = false) @Parameter(description = "lot no") String lotNo,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<PopBomDetailLotMasterResponse> popBomDetailLotMasters = popService.getPopBomDetailLotMasters(lotMasterId, itemId, lotNo);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPopBomDetailLotMasters.");
        return new ResponseEntity<>(popBomDetailLotMasters, OK);
    }

    // 사용 lotMaster 조회
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/exhaust-lots")
    @ResponseBody
    @Operation(summary = "(pop) 원부자재 lot 사용정보 조회", description = "")
    public ResponseEntity<List<PopBomDetailLotMasterResponse>> getLotMasterExhaust(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "품목 id") Long itemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        List<PopBomDetailLotMasterResponse> popBomDetailLotMasterResponses = popService.getLotMasterExhaust(lotMasterId, itemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from getLotMasterExhaust.");
        return new ResponseEntity<>(popBomDetailLotMasterResponses, OK);
    }

    // 사용 lotMaster 생성
    @SecurityRequirement(name = AUTHORIZATION)
    @PostMapping("/exhaust-lots")
    @ResponseBody
    @Operation(summary = "(pop) 원부자재 lot 사용정보 등록", description = "")
    public ResponseEntity<PopBomDetailLotMasterResponse> createLotMasterExhaust(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "품목 id") Long itemId,
            @RequestParam @Parameter(description = "사용한 lotMaster id") Long exhaustLotMasterId,
            @RequestParam @Parameter(description = "사용한 수량(소진여부가 true 인 건 소진이 안되었으면 0, 소진이 되었으면 전체수량)") int exhaustAmount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        PopBomDetailLotMasterResponse popBomDetailLotMasterResponse = popService.createLotMasterExhaust(lotMasterId, itemId, exhaustLotMasterId, exhaustAmount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from createLotMasterExhaust.");
        return new ResponseEntity<>(popBomDetailLotMasterResponse, OK);
    }

    // 사용 lotMaster 수정
    @SecurityRequirement(name = AUTHORIZATION)
    @PutMapping("/exhaust-lots")
    @ResponseBody
    @Operation(summary = "(pop) 원부자재 lot 사용정보 수정", description = "")
    public ResponseEntity<PopBomDetailLotMasterResponse> putLotMasterExhaust(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "품목 id") Long itemId,
            @RequestParam @Parameter(description = "사용한 lotMaster id") Long exhaustLotMasterId,
            @RequestParam @Parameter(description = "수량(소진X: 0, 소진O: 전체수량)") int exhaustAmount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        PopBomDetailLotMasterResponse popBomDetailLotMasterResponse = popService.putLotMasterExhaust(lotMasterId, itemId, exhaustLotMasterId, exhaustAmount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from putLotMasterExhaust.");
        return new ResponseEntity<>(popBomDetailLotMasterResponse, OK);
    }

    // 사용 lotMaster 삭제
    @SecurityRequirement(name = AUTHORIZATION)
    @DeleteMapping("/exhaust-lots")
    @ResponseBody
    @Operation(summary = "(pop) 원부자재 lot 사용정보 삭제", description = "")
    public ResponseEntity deleteLotMasterExhaust(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "품목 id") Long itemId,
            @RequestParam @Parameter(description = "사용한 lotMaster id") Long exhaustLotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        popService.deleteLotMasterExhaust(lotMasterId, itemId, exhaustLotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from deleteLotMasterExhaust.");
        return new ResponseEntity<>(NO_CONTENT);
    }

    // 중간검사 품목 정보 조회
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/test-items")
    @ResponseBody
    @Operation(summary = "(pop) 중간검사 품목 정보 조회", description = "")
    public ResponseEntity<PopTestItemResponse> getPopTestItem(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        PopTestItemResponse popTestResponse = popService.getPopTestItem(lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from getPopTestItem.");
        return new ResponseEntity<>(popTestResponse, OK);
    }

    // 공정에 해당하는 불량유형 조회
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/bad-item-types")
    @ResponseBody
    @Operation(summary = "(pop) lot 에 해당하는 불량유형 조회", description = "")
    public ResponseEntity<List<PopBadItemTypeResponse>> getPopTestBadItemTypes(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        List<PopBadItemTypeResponse> badItemResponses = popService.getPopTestBadItemTypes(lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from getPopTestBadItemTypes.");
        return new ResponseEntity<>(badItemResponses, OK);
    }

    // 중간검사 등록된 불량 조회
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/bad-items")
    @ResponseBody
    @Operation(summary = "(pop) 중간검사 등록된 불량 리스트 조회", description = "")
    public ResponseEntity<List<PopTestBadItemResponse>> getPopBadItemEnrollments(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        List<PopTestBadItemResponse> popTestBadItemResponses = popService.getPopBadItemEnrollments(lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from getPopBadItemEnrollments.");
        return new ResponseEntity<>(popTestBadItemResponses, OK);
    }

    // 불량 등록
    @SecurityRequirement(name = AUTHORIZATION)
    @PostMapping("/enrollment-bad-items")
    @ResponseBody
    @Operation(summary = "(pop) 불량 등록", description = "")
    public ResponseEntity<PopTestBadItemResponse> createPopBadItemEnrollment(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "불량 항목 id") Long badItemTypeId,
            @RequestParam @Parameter(description = "불량 수량") int badItemAmount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        PopTestBadItemResponse response =
                popService.createPopBadItemEnrollment(lotMasterId, badItemTypeId, badItemAmount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from createPopBadItemEnrollment.");
        return new ResponseEntity<>(response, OK);
    }

    // 불량 수량 수정
    @SecurityRequirement(name = AUTHORIZATION)
    @PutMapping("/enrollment-bad-items")
    @ResponseBody
    @Operation(summary = "(pop) 불량 수량 수정", description = "")
    public ResponseEntity<PopTestBadItemResponse> putPopBadItemEnrollment(
            @RequestParam @Parameter(description = "불량 id") Long enrollmentBadItemId,
            @RequestParam @Parameter(description = "불량 수량") int badItemAmount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        PopTestBadItemResponse response = popService.putPopBadItemEnrollment(enrollmentBadItemId, badItemAmount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from putPopBadItemEnrollment.");
        return new ResponseEntity<>(response, OK);
    }

    // 불량 삭제
    @SecurityRequirement(name = AUTHORIZATION)
    @DeleteMapping("/enrollment-bad-items")
    @ResponseBody
    @Operation(summary = "(pop) 불량 삭제", description = "")
    public ResponseEntity deletePopBadItemEnrollment(
            @RequestParam @Parameter(description = "불량 id") Long enrollmentBadItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        popService.deletePopBadItemEnrollment(enrollmentBadItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from deletePopBadItemEnrollment.");
        return new ResponseEntity<>(NO_CONTENT);
    }

    // 분할 lot 조회
    @SecurityRequirement(name = AUTHORIZATION)
    @GetMapping("/enrollment-lots")
    @ResponseBody
    @Operation(summary = "(pop) 분할 lot 리스트 조회", description = "")
    public ResponseEntity<List<PopLotMasterResponse>> getPopLotMasters(
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        List<PopLotMasterResponse> popLotMasterResponses = popService.getPopLotMasters(lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from getPopLotMasters.");
        return new ResponseEntity<>(popLotMasterResponses, OK);
    }

    // 분할 lot 생성
    // 자식 lotMaster: createdAmount, stockAmount
    @SecurityRequirement(name = AUTHORIZATION)
    @PostMapping("/enrollment-lots")
    @ResponseBody
    @Operation(summary = "(pop) 분할 lot 생성", description = "")
    public ResponseEntity<PopLotMasterResponse> createPopLotMasters(
            @RequestParam @Parameter(description = "만들어진 반제품 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "수량") int amount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        PopLotMasterResponse popLotMasterResponse = popService.createPopLotMasters(lotMasterId, amount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from createPopLotMasters.");
        return new ResponseEntity<>(popLotMasterResponse, OK);
    }

    // 분할 lot 수정
    @SecurityRequirement(name = AUTHORIZATION)
    @PutMapping("/enrollment-lots")
    @ResponseBody
    @Operation(summary = "(pop) 분할 lot 수정", description = "")
    public ResponseEntity<PopLotMasterResponse> putPopLotMasters(
            @RequestParam @Parameter(description = "분할 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "수량") int amount,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        PopLotMasterResponse popLotMasterResponse = popService.putPopLotMasters(lotMasterId, amount);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from putPopLotMasters.");
        return new ResponseEntity<>(popLotMasterResponse, OK);
    }

    // 분할 lot 삭제
    @SecurityRequirement(name = AUTHORIZATION)
    @DeleteMapping("/enrollment-lots")
    @ResponseBody
    @Operation(summary = "(pop) 분할 lot 삭제", description = "")
    public ResponseEntity deletePopLotMasters(
            @RequestParam @Parameter(description = "분할 lotMaster id") Long lotMasterId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        popService.deletePopLotMasters(lotMasterId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from deletePopLotMasters.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 충진 설비 선택 api
    @SecurityRequirement(name = AUTHORIZATION)
    @PutMapping("/filling-equipments")
    @ResponseBody
    @Operation(summary = "(pop) 충진공정 설비 선택", description = "등록 완료 시 설비의 생산가능 상태가 false 로 변경됨. [생산가능: true, 생산불가능: false]")
    public ResponseEntity putFillingEquipmentOfRealLot(
            @RequestParam @Parameter(description = "분할 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "충진공정 설비 id") Long equipmentId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        popService.putFillingEquipmentOfRealLot(lotMasterId, equipmentId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is putFillingEquipmentOfRealLot.");
        return new ResponseEntity<>(OK);
    }

    // 충진공정 설비 고장등록 api
    @SecurityRequirement(name = AUTHORIZATION)
    @PostMapping("/filling-equipment-errors")
    @ResponseBody
    @Operation(summary = "(pop) 충진공정 설비 고장 등록", description = "")
    public ResponseEntity createFillingEquipmentError(
            @RequestParam @Parameter(description = "작업지시 id") Long workOrderId,
            @RequestParam @Parameter(description = "설비 lotMaster id") Long lotMasterId,
            @RequestParam @Parameter(description = "고장 사유") BreakReason breakReason,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        popService.createFillingEquipmentError(workOrderId, lotMasterId, breakReason);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is viewed the list of from createFillingEquipmentError.");
        return new ResponseEntity<>(OK);
    }
}
