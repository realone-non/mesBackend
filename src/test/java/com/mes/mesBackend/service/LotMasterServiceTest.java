package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.LotCreatedResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LotMasterServiceTest {
    @Autowired private LotMasterService lotMasterService;

    @DisplayName("Lot No (융착, 포장, 라벨링) 입력 시 해당 LOT 를 생산하는데 투입된 반제품의 가장 오래전인 제조일자 반환")
    @Transactional
    @Test
    void getHalfLotCreatedDateByLotNo() throws NotFoundException, BadRequestException {
        // given
        String capAssemblyLotNo = "2208173A001";    // 라벨링 lot
        LotCreatedResponse lotCreatedResponse = new LotCreatedResponse().toResponse(797L, "22081132005", LocalDateTime.of(2022, 8, 11, 14, 43, 24, 370784));

        // then(테스트 시행, 테스트 검증)
        LotCreatedResponse response = lotMasterService.getHalfLotCreatedDateByLotNo(capAssemblyLotNo);
        assertEquals(lotCreatedResponse.getLotNo(), response.getLotNo());
    }


    @DisplayName("Lot Id(융착, 포장, 라벨링) 입력 시 해당 LOT 를 생산하는데 투입된 반제품의 가장 오래전인 제조일자 반환")
    @Transactional
    @Test
    void getHalfLotCreatedDateByLotId() throws NotFoundException, BadRequestException {
        // given
        Long capAssemblyLotId = 824L;
        LotCreatedResponse lotCreatedResponse = new LotCreatedResponse().toResponse(797L, "22081132005", LocalDateTime.of(2022, 8, 11, 14, 43, 24, 370784));

        // then(테스트 시행, 테스트 검증)
        LotCreatedResponse response = lotMasterService.getHalfLotCreatedDateByLotId(capAssemblyLotId);
        assertEquals(lotCreatedResponse.getLotNo(), response.getLotNo());
    }

    @DisplayName("융착, 포장, 라벨링이 아닌 경우 예외")
    @Transactional
    @Test
    void workProcessDivisionBadRequestMessageTest() {
        String fillingLotNo = "22081131012";        // 충진 lot
        String capAssemblyLotNo = "22081821003";    // 캡조립 lot
        String labelingLotNo = "2208173A001";       // 라벨링 lot
        String packagingLotNo = "MFG220817S10006";  // 포장 lot

        assertThrows(BadRequestException.class, () -> lotMasterService.getHalfLotCreatedDateByLotNo(fillingLotNo));
    }
}