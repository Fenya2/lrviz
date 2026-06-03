package ru.urfu.lrviz.api.dto.operations;

/**
 * Детали операции построения автомата
 *
 * @author fenya
 * @since 03.02.2026
 */
public final class AddStateOperationDto extends StateModificationOperationDto {
    public AddStateOperationDto(String message, String stateName) {
        super(message, "addState", stateName);
    }
}
