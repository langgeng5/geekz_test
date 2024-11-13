package com.test.geekz.features.order;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.geekz.features.order.dto.OrderRequestDto;
import com.test.geekz.features.order.dto.OrderResponseDto;
import com.test.geekz.helper.CustomPage;
import com.test.geekz.helper.ResponseDto;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderResponseDto>> getSingle(
            @PathVariable("id") Long id) {

           return orderService.getById(id);
    }

    @GetMapping("/page")
    public ResponseEntity<ResponseDto<CustomPage<OrderResponseDto>>> getPage(
            @RequestParam(defaultValue = "1") @Min(value = 1) int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = Sort.by(sortBy).ascending();
        if (direction.equalsIgnoreCase("DESC")) {
            sort = Sort.by(sortBy).descending();
        }
        Pageable paging = PageRequest.of(page - 1, size, sort);

        return orderService.getPage(paging);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<OrderResponseDto>> tambahData(
            @RequestBody OrderRequestDto request) {

        return orderService.insert(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderResponseDto>> ubahData(
            @PathVariable("id") Long id,
            @RequestBody OrderRequestDto request) {
       
        return orderService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Boolean>> deleteData(
            @PathVariable("id") Long id) {

        return orderService.delete(id);
    }
}
