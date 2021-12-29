package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ItemFileRequest;
import com.mes.mesBackend.dto.request.ItemRequest;
import com.mes.mesBackend.dto.response.ItemFileResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemService {
    // 품목 생성
    ItemResponse createItem(ItemRequest itemRequest) throws NotFoundException, BadRequestException;
    // 품목 단일 조회
    ItemResponse getItem(Long id) throws NotFoundException;
    // 품목 전체 조회
    List<ItemResponse> getItems(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, String searchWord);
    // 품목 페이징 조회
//    Page<ItemResponse> getItems(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, String searchWord, Pageable pageable);
    // 품목 수정
    ItemResponse updateItem(Long id, ItemRequest itemRequest) throws NotFoundException, BadRequestException;
    // 품목 삭제
    void deleteItem(Long id) throws NotFoundException;
    // 품목 단일 조회 및 예외
    Item getItemOrThrow(Long id) throws NotFoundException;


    // 파일 정보 생성
    ItemFileResponse createItemFileInfo(Long itemId, ItemFileRequest itemFileRequest) throws NotFoundException;
    // 파일 생성
    ItemFileResponse createFile(Long itemId, Long itemFileId, MultipartFile file) throws IOException, NotFoundException, BadRequestException;
    // 파일 리스트 조회
    List<ItemFileResponse> getItemFiles(Long itemId) throws NotFoundException;
    // 파일 정보 수정
    ItemFileResponse updateItemFileInfo(Long itemId, Long itemFileId, ItemFileRequest itemFileRequest) throws NotFoundException;
    // 파일 삭제
    void deleteItemFile(Long itemId, Long itemFileId) throws NotFoundException;
}
