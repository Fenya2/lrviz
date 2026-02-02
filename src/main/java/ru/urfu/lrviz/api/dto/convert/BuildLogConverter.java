package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import ru.urfu.lrviz.api.dto.BuildLogDto;
import ru.urfu.lrviz.core.lr.BuildLog;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public class BuildLogConverter implements Converter<BuildLog, BuildLogDto> {

    @Override
    public BuildLogDto convert(BuildLog source) {
        return null;
    }
}
