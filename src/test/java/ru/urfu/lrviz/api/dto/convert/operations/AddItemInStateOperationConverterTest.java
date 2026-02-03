package ru.urfu.lrviz.api.dto.convert.operations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.operations.AddItemInStateOperationDto;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.operations.AddItemInStateOperation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddItemInStateOperationConverterTest {

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private AddItemInStateOperationConverter converter;

    @Test
    void convert() {
        LRItem item = mock(LRItem.class);
        LRItemDto itemDto = mock(LRItemDto.class);
        when(conversionService.convert(item, LRItemDto.class)).thenReturn(itemDto);
        AddItemInStateOperation operation = new AddItemInStateOperation("S1", item);
        AddItemInStateOperationDto dto = converter.convert(operation);
        assertThat(dto).isNotNull();
        assertThat(dto.getState()).isEqualTo("S1");
        assertThat(dto.getItem()).isSameAs(itemDto);
    }
}
