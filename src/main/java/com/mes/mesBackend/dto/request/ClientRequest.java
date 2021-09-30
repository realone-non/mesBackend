package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ClientRequest {
    String clientCode;
    String name;
    int shortName;
    String clientType;
    String businessNumber;
    MultipartFile businessFile;
    String ceoName;
    String postalCode;
    String address;
    Long businessTypeId;
    String telNumber;
    String mail;
}
