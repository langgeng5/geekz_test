package com.test.geekz.features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.test.geekz.features.item.Item;
import com.test.geekz.features.item.ItemRepository;
import com.test.geekz.features.item.ItemService;
import com.test.geekz.features.item.dto.ItemRequestDto;
import com.test.geekz.features.item.dto.ItemResponseDto;
import com.test.geekz.helper.MapList;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ItemUnitTests {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ModelMapper modelMapper;
    
    @Mock
    private MapList mapList;

    @InjectMocks
    private ItemService itemService;


    @BeforeEach
	public void before() {
        Item dataBefore = new Item();
        dataBefore.setName("before 1");
        dataBefore.setPrice(50000D);

        itemRepository.save(dataBefore);
	}


    @Test
    public void insertTest() {
        ItemRequestDto test = new ItemRequestDto();
        test.setName("test1");
        test.setPrice(10000D);

        ItemResponseDto expectation = new ItemResponseDto();
        expectation.setId(2L);
        expectation.setName("test1");
        expectation.setPrice(10000D);
        expectation.setStock(0);

        ItemResponseDto result = itemService.insert(test).getBody().getPayload();

        assertEquals(expectation, result);
    }

    @Test
    public void updateTest() {
        ItemRequestDto test = new ItemRequestDto();
        test.setName("update 1");
        test.setPrice(299D);

        ItemResponseDto expectation = new ItemResponseDto();
        expectation.setId(2L);
        expectation.setName("update 1");
        expectation.setPrice(299D);
        expectation.setStock(0);


        ItemResponseDto result = itemService.update(2L, test).getBody().getPayload();

        assertEquals(expectation, result);
    }

    @Test
    public void deleteTest() {
        itemService.delete(1L).getBody().getPayload();
        Integer total = itemRepository.findAll().size();

        assertEquals(0, total);
    }

    @Test
    public void getTest() {
        ItemResponseDto test = itemService.getById(1L).getBody().getPayload();

        ItemResponseDto expectation = new ItemResponseDto();
        expectation.setId(2L);
        expectation.setName("before 1");
        expectation.setPrice(50000D);
        expectation.setStock(0);

        assertEquals(expectation, test);
    }

    @Test
    public void getPageTest() {
        Sort sort = Sort.by("id").descending();
        Pageable paging = PageRequest.of(1, 2, sort);

        List<ItemResponseDto> test = itemService.getPage(paging).getBody().getPayload().getContent();

        assertEquals(1, test.size());
    }
}
