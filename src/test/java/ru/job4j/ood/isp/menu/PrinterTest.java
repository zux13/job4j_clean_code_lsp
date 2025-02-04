package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PrinterTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void whenPrintMenuThenOutputCorrectlyFormatted() {

        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "File", STUB_ACTION);
        menu.add("File", "Open", STUB_ACTION);
        menu.add("File", "Save", STUB_ACTION);
        menu.add(Menu.ROOT, "Edit", STUB_ACTION);
        menu.add("Edit", "Copy", STUB_ACTION);
        menu.add("Edit", "Paste", STUB_ACTION);

        Printer printer = new Printer();
        printer.print(menu);

        String expectedOutput = """
                1. File
                ----1.1. Open
                ----1.2. Save
                2. Edit
                ----2.1. Copy
                ----2.2. Paste
                """;
        assertThat(outContent.toString().trim().stripIndent()).isEqualTo(expectedOutput.trim());
    }

    @Test
    public void whenPrintEmptyMenuThenNoOutput() {
        Menu menu = new SimpleMenu();
        Printer printer = new Printer();
        printer.print(menu);
        assertThat(outContent.toString().trim()).isEmpty();
    }

    @Test
    public void whenPrintDeeplyNestedMenuThenOutputWithCorrectIndentation() {

        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Root", STUB_ACTION);
        menu.add("Root", "Level1", STUB_ACTION);
        menu.add("Level1", "Level2", STUB_ACTION);
        menu.add("Level2", "Level3", STUB_ACTION);

        Printer printer = new Printer();
        printer.print(menu);

        String expectedOutput = """
                1. Root
                ----1.1. Level1
                --------1.1.1. Level2
                ------------1.1.1.1. Level3
                """;
        assertThat(outContent.toString().trim().stripIndent()).isEqualTo(expectedOutput.trim());
    }
}