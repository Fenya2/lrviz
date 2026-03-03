package ru.urfu.lrviz.api.dto.map;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.BuildLogDto;
import ru.urfu.lrviz.api.dto.LRAutomatonDto;
import ru.urfu.lrviz.api.dto.LrBuildResultDto;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.LRAutomaton;

/**
 * @author fenya
 * @since 02.02.2026
 */
@Service
public class LrBuildResultMapper {

    private final ConversionService conversionService;

    public LrBuildResultMapper(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * Преобразует результат построения LR автомата в формат, согласный REST API
     *
     * @param automaton построенный автомат
     * @param context   контекст, использовавшийся при построении
     * @return результат построения
     */
    public LrBuildResultDto map(LRAutomaton automaton, BuildContext context) {
        LRAutomatonDto convertedAutomaton = conversionService.convert(automaton, LRAutomatonDto.class);
        BuildLogDto convertedLog = conversionService.convert(context.getBuildLog(), BuildLogDto.class);
        return new LrBuildResultDto(convertedAutomaton, convertedLog);
    }
}
