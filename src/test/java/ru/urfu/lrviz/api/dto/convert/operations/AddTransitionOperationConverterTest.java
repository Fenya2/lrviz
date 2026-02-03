package ru.urfu.lrviz.api.dto.convert.operations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.lrviz.api.dto.operations.AddTransitionOperationDto;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.operations.AddTransitionOperation;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AddTransitionOperationConverterTest {

    @InjectMocks
    private AddTransitionOperationConverter converter;

    @Test
    void convert() {
        GrammarSymbol symbol = new Terminal("a");
        AddTransitionOperation operation = new AddTransitionOperation("S1", "S2", symbol);
        AddTransitionOperationDto dto = converter.convert(operation);

        assertThat(dto).isNotNull();
        assertThat(dto.getFrom()).isEqualTo("S1");
        assertThat(dto.getTo()).isEqualTo("S2");
        assertThat(dto.getThrough()).isEqualTo("a");
    }
}
