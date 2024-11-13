package com.test.geekz.features.item;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.test.geekz.exceptions.ApiRequestException;
import com.test.geekz.features.item.dto.ItemRequestDto;
import com.test.geekz.features.item.dto.ItemResponseDto;
import com.test.geekz.helper.CustomPage;
import com.test.geekz.helper.MapList;
import com.test.geekz.helper.ResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final MapList mapList;

    public ResponseEntity<ResponseDto<ItemResponseDto>> getById(Long id) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (!itemOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }

        ItemResponseDto itemResponse = modelMapper.map(itemOpt.get(), ItemResponseDto.class);

        ResponseDto<ItemResponseDto> response = new ResponseDto<>();
        response.setMessage("Get Data Success");
        response.setPayload(itemResponse);

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ResponseDto<CustomPage<ItemResponseDto>>> getPage(Pageable paging) {
        
        Page<Item> pageData = itemRepository.findAll(paging);
        
        CustomPage<ItemResponseDto> responsePage = mapList.mapEntityPageIntoDtoPage(pageData, ItemResponseDto.class);

        ResponseDto<CustomPage<ItemResponseDto>> response = new ResponseDto<>();
        response.setMessage("Get Data Success");
        response.setPayload(responsePage);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ResponseDto<ItemResponseDto>> insert(ItemRequestDto request) {
                
        Item item = modelMapper.map(request, Item.class);
        item = itemRepository.save(item);

        ResponseDto<ItemResponseDto> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.CREATED.value());
        response.setMessage("Insert data success");
        response.setPayload(modelMapper.map(item, ItemResponseDto.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ResponseDto<ItemResponseDto>> update(Long id, ItemRequestDto request) {
        
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (!itemOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }
                
        Item item = itemOpt.get();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item = itemRepository.save(item);

        ResponseDto<ItemResponseDto> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Update data success");
        response.setPayload(modelMapper.map(item, ItemResponseDto.class));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ResponseDto<Boolean>> delete(Long id) {
        
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (!itemOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }
        
        itemRepository.delete(itemOpt.get());

        ResponseDto<Boolean> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Delete data success");
        response.setPayload(true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
