package ru.urfu.lrviz.api.dto.convert.operations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.lrviz.api.dto.operations.AddStateOperationDto;
import ru.urfu.lrviz.core.lr.operations.AddStateOperation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AddStateOperationConverterTest {

    @InjectMocks
    private AddStateOperationConverter converter;

    @Test
    void convert() {
        AddStateOperation operation = new AddStateOperation("S1");
        AddStateOperationDto dto = converter.convert(operation);
        Assertions.assertThat(dto).isNotNull();
        assertThat(dto.getStateName()).isEqualTo("S1");
    }
}
