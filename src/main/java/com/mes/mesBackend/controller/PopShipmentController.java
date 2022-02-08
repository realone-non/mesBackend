package com.mes.mesBackend.controller;

import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.service.PopShipmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequestMapping("/pop-shipments")
@Tag(name = "pop-shipment", description = "[pop] 출하 API")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class PopShipmentController {
    private final PopShipmentService popShipmentService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PopShipmentController.class);
    private CustomLogger cLogger;

    // 출하정보 전체 조회

    //
}
