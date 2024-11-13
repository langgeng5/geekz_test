package com.test.geekz.features.inventory;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.test.geekz.exceptions.ApiRequestException;
import com.test.geekz.features.inventory.dto.InventoryRequestDto;
import com.test.geekz.features.inventory.dto.InventoryResponseDto;
import com.test.geekz.features.item.Item;
import com.test.geekz.features.item.ItemRepository;
import com.test.geekz.helper.CustomPage;
import com.test.geekz.helper.MapList;
import com.test.geekz.helper.ResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final MapList mapList;

    public ResponseEntity<ResponseDto<InventoryResponseDto>> getById(Long id) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (!inventoryOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }

        InventoryResponseDto inventoryResponse = modelMapper.map(inventoryOpt.get(), InventoryResponseDto.class);

        ResponseDto<InventoryResponseDto> response = new ResponseDto<>();
        response.setMessage("Get Data Success");
        response.setPayload(inventoryResponse);

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ResponseDto<CustomPage<InventoryResponseDto>>> getPage(Pageable paging) {
        
        Page<Inventory> pageData = inventoryRepository.findAll(paging);
        
        CustomPage<InventoryResponseDto> responsePage = mapList.mapEntityPageIntoDtoPage(pageData, InventoryResponseDto.class);

        ResponseDto<CustomPage<InventoryResponseDto>> response = new ResponseDto<>();
        response.setMessage("Get Data Success");
        response.setPayload(responsePage);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ResponseDto<InventoryResponseDto>> insert(InventoryRequestDto request) {
        //check data item    
        Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
        if (!itemOpt.isPresent()) {
            throw new ApiRequestException("Item data with id:"+request.getItemId()+" not found", new Error());
        }

        Inventory inventory = modelMapper.map(request, Inventory.class);
        inventory.setItem(itemOpt.get());
        inventory = inventoryRepository.save(inventory);

        ResponseDto<InventoryResponseDto> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.CREATED.value());
        response.setMessage("Insert data success");
        response.setPayload(modelMapper.map(inventory, InventoryResponseDto.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ResponseDto<InventoryResponseDto>> update(Long id, InventoryRequestDto request) {
        
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (!inventoryOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }

        //check data item
        Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
        if (!itemOpt.isPresent()) {
            throw new ApiRequestException("Item data with id:"+request.getItemId()+" not found", new Error());
        }
                
        Inventory inventory = inventoryOpt.get();
        inventory.setItem(itemOpt.get());
        inventory.setQty(request.getQty());
        inventory.setType(request.getType());
        inventory = inventoryRepository.save(inventory);

        ResponseDto<InventoryResponseDto> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Update data success");
        response.setPayload(modelMapper.map(inventory, InventoryResponseDto.class));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ResponseDto<Boolean>> delete(Long id) {
        
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (!inventoryOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }
        
        inventoryRepository.delete(inventoryOpt.get());

        ResponseDto<Boolean> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Delete data success");
        response.setPayload(true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
