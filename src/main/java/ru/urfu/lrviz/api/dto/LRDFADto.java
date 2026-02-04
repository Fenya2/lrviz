package ru.urfu.lrviz.api.dto;

import java.util.List;
import java.util.Map;

public record LRDFADto(
        List<LRStateDto> states,
        Map<TransitionDto, String> transitions){
}
