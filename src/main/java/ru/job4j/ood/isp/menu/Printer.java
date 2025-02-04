package ru.job4j.ood.isp.menu;

public class Printer implements MenuPrinter {
    @Override
    public void print(Menu menu) {
        for (Menu.MenuItemInfo itemInfo : menu) {
            System.out.println(formatedItemInfo(itemInfo));
        }
    }

    private String formatedItemInfo(Menu.MenuItemInfo itemInfo) {

        StringBuilder sb = new StringBuilder();
        String number = itemInfo.getNumber();

        return sb.append("----".repeat(Math.max(0, number.split("\\.").length - 1)))
                .append(number)
                .append(" ")
                .append(itemInfo.getName())
                .toString();
    }
}