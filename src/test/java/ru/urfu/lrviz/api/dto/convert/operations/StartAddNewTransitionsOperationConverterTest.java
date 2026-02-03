package ru.urfu.lrviz.api.dto.convert.operations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.lrviz.api.dto.operations.StartAddNewTransitionsOperationDto;
import ru.urfu.lrviz.core.lr.operations.StartAddNewTransitionsOperation;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StartAddNewTransitionsOperationConverterTest {

    @InjectMocks
    private StartAddNewTransitionsOperationConverter converter;

    @Test
    void convert() {
        StartAddNewTransitionsOperation operation =
                new StartAddNewTransitionsOperation("S1");
        StartAddNewTransitionsOperationDto dto = converter.convert(operation);
        assertThat(dto).isNotNull();
        assertThat(dto.getMessage()).contains("Просматриваем состояние 'S1'");
    }
}
