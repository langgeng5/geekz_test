package com.test.geekz.features.order;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.test.geekz.exceptions.ApiRequestException;
import com.test.geekz.features.item.Item;
import com.test.geekz.features.item.ItemRepository;
import com.test.geekz.features.order.dto.OrderRequestDto;
import com.test.geekz.features.order.dto.OrderResponseDto;
import com.test.geekz.helper.CustomPage;
import com.test.geekz.helper.MapList;
import com.test.geekz.helper.ResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final MapList mapList;

    public ResponseEntity<ResponseDto<OrderResponseDto>> getById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (!orderOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }

        OrderResponseDto orderResponse = modelMapper.map(orderOpt.get(), OrderResponseDto.class);

        ResponseDto<OrderResponseDto> response = new ResponseDto<>();
        response.setMessage("Get Data Success");
        response.setPayload(orderResponse);

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ResponseDto<CustomPage<OrderResponseDto>>> getPage(Pageable paging) {
        
        Page<Order> pageData = orderRepository.findAll(paging);
        
        CustomPage<OrderResponseDto> responsePage = mapList.mapEntityPageIntoDtoPage(pageData, OrderResponseDto.class);

        ResponseDto<CustomPage<OrderResponseDto>> response = new ResponseDto<>();
        response.setMessage("Get Data Success");
        response.setPayload(responsePage);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ResponseDto<OrderResponseDto>> insert(OrderRequestDto request) {
        //check data item    
        Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
        if (!itemOpt.isPresent()) {
            throw new ApiRequestException("Item data with id:"+request.getItemId()+" not found", new Error());
        }
        if (itemOpt.get().getStock() < request.getQty()) {
            throw new ApiRequestException("Insuficient item stock", new Error());
        }

        Order order = modelMapper.map(request, Order.class);
        order.setItem(itemOpt.get());
        order = orderRepository.save(order);

        ResponseDto<OrderResponseDto> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.CREATED.value());
        response.setMessage("Insert data success");
        response.setPayload(modelMapper.map(order, OrderResponseDto.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ResponseDto<OrderResponseDto>> update(Long id, OrderRequestDto request) {
        
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (!orderOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }

        //check data item
        Optional<Item> itemOpt = itemRepository.findById(request.getItemId());
        if (!itemOpt.isPresent()) {
            throw new ApiRequestException("Item data with id:"+request.getItemId()+" not found", new Error());
        }
        if ((itemOpt.get().getStock() + orderOpt.get().getQty()) < request.getQty()) {
            throw new ApiRequestException("Insuficient item stock", new Error());
        }
                
        Order order = orderOpt.get();
        order.setItem(itemOpt.get());
        order.setQty(request.getQty());
        order.setPrice(request.getPrice());
        order = orderRepository.save(order);

        ResponseDto<OrderResponseDto> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Update data success");
        response.setPayload(modelMapper.map(order, OrderResponseDto.class));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ResponseDto<Boolean>> delete(Long id) {
        
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (!orderOpt.isPresent()) {
            throw new ApiRequestException("Data with id:"+id+" not found", new Error());
        }
        
        orderRepository.delete(orderOpt.get());

        ResponseDto<Boolean> response = new ResponseDto<>();
        response.setStatus(true);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Delete data success");
        response.setPayload(true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
