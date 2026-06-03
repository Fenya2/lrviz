package ru.urfu.lrviz.core.lr;

import jakarta.annotation.Nullable;
import ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm;

/**
 * Параметры построения LR-автомата
 *
 * @author fenya
 * @since 07.03.2026
 */
public final class BuildOptions {
    @Nullable
    private final StateNamesGenerationStrategy namesGenerationStrategy;
    @Nullable
    private final LALR1BuildAlgorithm lalr1BuildAlgorithm;
    private final boolean enableBuildLog;

    private BuildOptions(@Nullable StateNamesGenerationStrategy namesGenerationStrategy,
                         @Nullable LALR1BuildAlgorithm lalr1BuildAlgorithm,
                         boolean enableBuildLog) {
        this.namesGenerationStrategy = namesGenerationStrategy;
        this.lalr1BuildAlgorithm = lalr1BuildAlgorithm;
        this.enableBuildLog = enableBuildLog;
    }

    public static OptionsBuilder builder() {
        return new OptionsBuilder();
    }

    public static BuildOptions createEmpty() {
        return builder().build();
    }

    @Nullable
    public StateNamesGenerationStrategy namesGenerationStrategy() {
        return namesGenerationStrategy;
    }

    @Nullable
    public LALR1BuildAlgorithm lalr1BuildAlgorithm() {
        return lalr1BuildAlgorithm;
    }

    public boolean enableBuildLog() {
        return enableBuildLog;
    }

    public static final class OptionsBuilder {
        private StateNamesGenerationStrategy namesGenerationStrategy;
        private LALR1BuildAlgorithm lalr1BuildAlgorithm;
        private boolean enableBuildLog = false;

        private OptionsBuilder() {
        }

        public OptionsBuilder namesGenerationStrategy(@Nullable StateNamesGenerationStrategy strategy) {
            this.namesGenerationStrategy = strategy;
            return this;
        }

        public OptionsBuilder lalr1BuildAlgorithm(@Nullable LALR1BuildAlgorithm algorithm) {
            this.lalr1BuildAlgorithm = algorithm;
            return this;
        }

        public OptionsBuilder enableBuildLog(boolean enableBuildLog) {
            this.enableBuildLog = enableBuildLog;
            return this;
        }

        public BuildOptions build() {
            return new BuildOptions(
                    namesGenerationStrategy,
                    lalr1BuildAlgorithm,
                    enableBuildLog
            );
        }
    }
}