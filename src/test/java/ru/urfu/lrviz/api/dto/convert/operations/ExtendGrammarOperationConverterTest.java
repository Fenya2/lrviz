package ru.urfu.lrviz.api.dto.convert.operations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.lrviz.api.dto.operations.ExtendGrammarOperationDto;
import ru.urfu.lrviz.core.lr.operations.ExtendGrammarOperation;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ExtendGrammarOperationConverterTest {

    @InjectMocks
    private ExtendGrammarOperationConverter converter;

    @Test
    void convert() {
        // given
        ExtendGrammarOperation operation = new ExtendGrammarOperation();
        ExtendGrammarOperationDto dto = converter.convert(operation);
        assertThat(dto).isNotNull();
        assertThat(dto.getLevel()).isEqualTo("comment");
        assertThat(dto.getMessage()).isEqualTo("Расширяем исходную грамматику.");
    }
}
