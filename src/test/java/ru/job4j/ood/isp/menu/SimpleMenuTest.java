package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleMenuTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;
    private SimpleMenu menu;

    @BeforeEach
    void setUp() {
        menu = new SimpleMenu();
        menu.add(Menu.ROOT, "File", STUB_ACTION);
        menu.add("File", "Open", STUB_ACTION);
        menu.add("File", "Save", STUB_ACTION);
        menu.add(Menu.ROOT, "Edit", STUB_ACTION);
        menu.add("Edit", "Copy", STUB_ACTION);
        menu.add("Edit", "Paste", STUB_ACTION);
    }

    @Test
    public void whenAddThenReturnSame() {
        menu.add("Open", "SubOpen", STUB_ACTION);

        assertThat(new Menu.MenuItemInfo("File",
                List.of("Open", "Save"), STUB_ACTION, "1."))
                .isEqualTo(menu.select("File").get());
        assertThat(new Menu.MenuItemInfo("Open",
                List.of("SubOpen"), STUB_ACTION, "1.1."))
                .isEqualTo(menu.select("Open").get());
        assertThat(new Menu.MenuItemInfo(
                "Copy", List.of(), STUB_ACTION, "2.1."))
                .isEqualTo(menu.select("Copy").get());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    void testSelectExistingItem() {
        menu.add(Menu.ROOT, "TestRoot", STUB_ACTION);
        menu.add("TestRoot", "TestChild", STUB_ACTION);

        Optional<Menu.MenuItemInfo> result = menu.select("TestChild");
        assertTrue(result.isPresent());
        Menu.MenuItemInfo info = result.get();
        assertEquals("TestChild", info.getName());
        assertEquals("3.1.", info.getNumber());
    }

    @Test
    void testSelectNonExistingItem() {
        Optional<Menu.MenuItemInfo> result = menu.select("Delete");
        assertFalse(result.isPresent());
    }

    @Test
    void testSelectRootItem() {
        Optional<Menu.MenuItemInfo> result = menu.select("File");
        assertTrue(result.isPresent());
        Menu.MenuItemInfo info = result.get();
        assertEquals("File", info.getName());
        assertEquals("1.", info.getNumber());
    }

    @Test
    void testSelectDeeplyNestedItem() {
        menu.add("Open", "SubOpen", STUB_ACTION);

        Optional<Menu.MenuItemInfo> result = menu.select("SubOpen");
        assertTrue(result.isPresent());
        Menu.MenuItemInfo info = result.get();
        assertEquals("SubOpen", info.getName());
        assertEquals("1.1.1.", info.getNumber());
    }

    @Test
    void testSelectWithNullName() {
        Optional<Menu.MenuItemInfo> result = menu.select(Menu.ROOT);
        assertFalse(result.isPresent());
    }

    @Test
    void testSelectWithEmptyName() {
        Optional<Menu.MenuItemInfo> result = menu.select("");
        assertFalse(result.isPresent());
    }
}