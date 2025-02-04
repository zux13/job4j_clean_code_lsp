package ru.job4j.ood.isp.menu;

import java.util.*;

public class SimpleMenu implements Menu {

    private final List<MenuItem> rootElements = new ArrayList<>();

    @Override
    public boolean add(String parentName, String childName, ActionDelegate actionDelegate) {

        boolean rsl = false;

        if (parentName == Menu.ROOT) {
            rootElements.add(new SimpleMenuItem(childName, actionDelegate));
            rsl = true;
        } else {
            Optional<ItemInfo> parentInfo = findItem(parentName);
            if (parentInfo.isPresent()) {
                MenuItem parent = parentInfo.get().menuItem;
                parent.getChildren().add(new SimpleMenuItem(childName, actionDelegate));
                rsl = true;
            }
        }

        return rsl;
    }

    @Override
    public Optional<MenuItemInfo> select(String itemName) {

        Optional<MenuItemInfo> rsl = Optional.empty();
        Optional<ItemInfo> itemInfo = findItem(itemName);

        if (itemInfo.isPresent()) {
            ItemInfo info = itemInfo.get();
            rsl = Optional.of(new Menu.MenuItemInfo(info.menuItem, info.number));
        }

        return rsl;
    }

    @Override
    public Iterator<MenuItemInfo> iterator() {
        return new Iterator<>() {
            private final DFSIterator dfsIterator = new DFSIterator();

            @Override
            public boolean hasNext() {
                return dfsIterator.hasNext();
            }

            @Override
            public MenuItemInfo next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                ItemInfo itemInfo = dfsIterator.next();
                return new Menu.MenuItemInfo(itemInfo.menuItem, itemInfo.number);
            }
        };
    }

    private Optional<ItemInfo> findItem(String name) {

        Optional<ItemInfo> rsl = Optional.empty();
        if (name == null || name.isEmpty()) {
            return rsl;
        }

        DFSIterator dfsIterator = new DFSIterator();
        while (dfsIterator.hasNext()) {
            ItemInfo itemInfo = dfsIterator.next();
            if (itemInfo.menuItem.getName().equals(name)) {
                rsl = Optional.of(itemInfo);
                break;
            }
        }

        return rsl;
    }

    private static class SimpleMenuItem implements MenuItem {

        private final String name;
        private final List<MenuItem> children = new ArrayList<>();
        private final ActionDelegate actionDelegate;

        public SimpleMenuItem(String name, ActionDelegate actionDelegate) {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<MenuItem> getChildren() {
            return children;
        }

        @Override
        public ActionDelegate getActionDelegate() {
            return actionDelegate;
        }
    }

    private class DFSIterator implements Iterator<ItemInfo> {

        private final Deque<MenuItem> stack = new LinkedList<>();
        private final Deque<String> numbers = new LinkedList<>();

        DFSIterator() {
            int number = 1;
            for (MenuItem item : rootElements) {
                stack.addLast(item);
                numbers.addLast(String.valueOf(number++).concat("."));
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ItemInfo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MenuItem current = stack.removeFirst();
            String lastNumber = numbers.removeFirst();
            List<MenuItem> children = current.getChildren();
            int currentNumber = children.size();
            for (var i = children.listIterator(children.size()); i.hasPrevious();) {
                stack.addFirst(i.previous());
                numbers.addFirst(lastNumber.concat(String.valueOf(currentNumber--)).concat("."));
            }
            return new ItemInfo(current, lastNumber);
        }
    }

    private class ItemInfo {

        private final MenuItem menuItem;
        private final String number;

        public ItemInfo(MenuItem menuItem, String number) {
            this.menuItem = menuItem;
            this.number = number;
        }
    }
}