package ru.urfu.lrviz.core.lr.operations;

/**
 * Уровень операции
 *
 * @author fenya
 * @since 28.03.2026
 */
public enum OperationLevel {
    /**
     * Операция-комментарий. Пропуск не влияет на построение
     */
    COMMENT,
    /**
     * Ключевая для построения LR-автомата операция
     */
    ACTION
}
