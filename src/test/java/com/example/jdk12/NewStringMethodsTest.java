package com.example.jdk12;

import com.google.common.io.Resources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("New JDK 12 String methods")
class NewStringMethodsTest {

    @Test
    void shouldIndent() {
        var text = readFileOnClasspath("testTextFile.txt");
        var expectedText = readFileOnClasspath("testIndentedTextFile.txt") + System.lineSeparator();

        assertThat(text.indent(4)).isEqualTo(expectedText);
    }

    @SuppressWarnings({"UnstableApiUsage", "squid:S00112"})
    private String readFileOnClasspath(String fileName) {
        try {
            var uri = Resources.getResource(fileName).toURI();
            return Files.readString(Paths.get(uri));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldTransformByFunctionApplication() {
        var str = "  The\n  quick brown fox  \n  jumped over the lazy  \n    brown dog \n   in the meadow\n\n   ";

        var transformed = str.transform(this::crazyTransformer);

        assertThat(transformed).isEqualTo("QUICK BROWN FOX JUMPED OVER LAZY BROWN DOG IN MEADOW");
    }

    private String crazyTransformer(String s) {
        return s.lines()
                .map(String::toUpperCase)
                .map(String::strip)
                .collect(joining(" "))
                .replace("THE", "")
                .replace("  ", " ")
                .strip();
    }
}
