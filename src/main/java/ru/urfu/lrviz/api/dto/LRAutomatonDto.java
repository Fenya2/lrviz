package ru.urfu.lrviz.api.dto;

import java.util.List;
import java.util.Map;

/**
 * @author fenya
 * @since 02.02.2026
 */
public record LRAutomatonDto(List<LRStateDto> states, Map<TransitionKeyDto, String> transitions) {

}
