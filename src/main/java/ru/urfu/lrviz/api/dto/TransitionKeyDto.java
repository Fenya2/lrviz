package ru.urfu.lrviz.api.dto;

/**
 * @param stateName имя состояния, из которого осуществляется переход
 * @param symbol символ, по которому осуществляется переход
 *
 * @author fenya
 * @since 03.02.2026
 */
public record TransitionKeyDto(String stateName, String symbol) {
}
