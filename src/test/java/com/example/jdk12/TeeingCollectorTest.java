package com.example.jdk12;

import org.apache.commons.lang3.Range;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.minBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.teeing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

@DisplayName("Teeing Collector")
@ExtendWith(SoftAssertionsExtension.class)
class TeeingCollectorTest {

    /**
     * Source: https://www.azul.com/39-new-features-and-apis-in-jdk-12/
     */
    @Test
    void contrivedExampleToComputeAverage() {
        double average = Stream.of(1, 4, 2, 7, 4, 6, 5)
                .collect(teeing(
                        summingDouble(i -> i),
                        counting(),
                        (sum, n) -> sum / n)
                );

        assertThat(average).isCloseTo(4.14, withinPercentage(1L));
    }

    /**
     * Reference: https://blog.codefx.org/java/teeing-collector/
     */
    @Test
    void createRangeFromListOfNumbers(SoftAssertions softly) {
        var numbers = List.of(67, 45, 8, 97, 23, 107, 5);

        var range = numbers.stream()
                .collect(teeing(
                        minBy(Integer::compareTo),
                        maxBy(Integer::compareTo),
                        TeeingCollectorTest::integerRangeFromOptionals)
                );

        softly.assertThat(range.getMinimum()).isEqualTo(5);
        softly.assertThat(range.getMaximum()).isEqualTo(107);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Range<Integer> integerRangeFromOptionals(Optional<Integer> min, Optional<Integer> max) {
        return Range.between(min.orElse(0), max.orElse(Integer.MAX_VALUE));
    }
}
