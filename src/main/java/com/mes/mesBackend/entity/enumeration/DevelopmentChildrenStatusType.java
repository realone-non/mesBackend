package com.mes.mesBackend.entity.enumeration;

public enum DevelopmentChildrenStatusType {
    /*
     * 개발 품목 등록 프로세스 하위
     * ORDER : 수주
     * DEVELOPMENT_REQUEST : 개발의뢰서
     * VALIDATION_CHECK : 타당성 검토
     * DEVELOPMENT_PLAN : 개발계획서
     * DEVELOPMENT_START : 개발착수회의
     * DESIGN_PLAN : 설계
     * DESIGN_REVIEW : 디자인 리뷰회의
     * PRODUCT_VERIFICATION : 제품검증
     * PROTOTYPE_EVALUATION : 시제품 평가 회의
     * STANDARD_DRAWING : 규격도면
     * COMPLETE_REPORT : 완료보고
     * OTHER_DOCUMENT : 기타문서
     * MINUTES : 회의록
     */

    ORDER, DEVELOPMENT_REQUEST, VALIDATION_CHECK, DEVELOPMENT_PLAN, DEVELOPMENT_START, DESIGN_PLAN, DESIGN_REVIEW,
    PRODUCT_VERIFICATION, PROTOTYPE_EVALUATION, STANDARD_DRAWING, COMPLETE_REPORT, OTHER_DOCUMENT, MINUTES
}