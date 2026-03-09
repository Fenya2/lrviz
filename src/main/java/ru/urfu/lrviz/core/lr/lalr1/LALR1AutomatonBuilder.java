package ru.urfu.lrviz.core.lr.lalr1;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.grammar.GrammarService;
import ru.urfu.lrviz.core.lr.AutomatonType;
import ru.urfu.lrviz.core.lr.lr0.LR0AutomatonBuilder;

import static ru.urfu.lrviz.core.lr.AutomatonType.LALR;

/**
 * @author fenya
 * @since 07.03.2026
 */
@Component
public class LALR1AutomatonBuilder extends LR0AutomatonBuilder {
    public LALR1AutomatonBuilder(GrammarService grammarService) {
        super(grammarService);
    }

    /**
     * Распространение и спонтанная генерация символов предпросмотра
     *
     * @return
     */
    @Override
    public AutomatonType getBuildType() {
        return LALR;
    }

}
