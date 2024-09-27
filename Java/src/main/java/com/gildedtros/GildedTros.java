package com.gildedtros;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to update quality and amount of provided {@link Item}s
 *
 * @author Tom De bruyn
 */
class GildedTros {
    private final Map<ItemType, List<Item>> items;

    public GildedTros(Item... items) {
        // Group items by itemType for later use
        this.items = Stream.of(items).collect(Collectors.groupingBy(ItemType::parse));
    }

    /**
     * Update the quality for all items to simulate one day has passed.
     */
    public void updateQuality() {
        items
                .forEach((type, items) -> {
                    Consumer<Item> incrementFunction = type.getIncrementFunction(); // Get the consumer for the type
                    items.forEach(incrementFunction); // Apply it to all items of that type
                });
    }

    public List<Item> getItems() {
        return items.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Enum to keep track of all types of items, along with specific rule of what to do when a day rolls over
     */
    private enum ItemType {
        DEFAULT(
                item -> {
                    item.sellIn--;
                    changeQuality(item, -1);
                },
                item -> false
        ),
        GOOD_WINE(
                item -> {
                    item.sellIn--;
                    changeQuality(item, 1);
                },
                nameMatches("Good Wine")
        ),
        LEGENDARY(
                item -> item.quality = 80,
                nameMatches("B-DAWG Keychain")
        ),
        BACKSTAGE_PASSES(
                item -> {
                    item.sellIn--;

                    if (item.sellIn < 0) {
                        item.quality = 0;
                        return;
                    }

                    int change = 1;
                    if (item.sellIn < 10) {
                        change = 2;
                    }
                    if (item.sellIn < 5) {
                        change = 3;
                    }
                    changeQuality(item, change);
                },
                nameContains("Backstage passes")
        ),
        SMELLY_ITEMS(
                item -> {
                    item.sellIn--;
                    changeQuality(item, -2);
                },
                nameMatches("Duplicate Code", "Long Methods", "Ugly Variable Names")
        );

        private final Consumer<Item> incrementFunction;
        private final Predicate<Item> parser;

        ItemType(Consumer<Item> incFunc, Predicate<Item> parser) {
            this.incrementFunction = incFunc;
            this.parser = parser;
        }

        /**
         * Get the {@link Consumer} that will execute the update logic (by reference on the provided {@link Item} object)
         *
         * @return A {@link Consumer} that will execute the update logic (by reference on the provided {@link Item} object)
         */
        public Consumer<Item> getIncrementFunction() {
            return incrementFunction;
        }

        /**
         * Get the {@link ItemType} for a specific {@link Item}. This will make use of the {@code parser} predicate. If no {@link ItemType} matches, {@code ItemType.DEFAULT} will be returned.
         *
         * @param item The {@link Item} for which the item type will be checked
         * @return The {@link ItemType} of the provided {@link Item}
         */
        public static ItemType parse(Item item) {
            return Arrays.stream(ItemType.values())
                    .filter(itemType -> itemType.parser.test(item))
                    .findFirst()
                    .orElse(ItemType.DEFAULT);
        }

        /**
         * Method used to check if the name of the item is a full match for any of the provided names.
         *
         * @param names All names that will be matched against
         * @return A {@link Predicate} that will check if the name of the item is a full match for any of the provided names
         */
        private static Predicate<Item> nameMatches(String... names) {
            return item -> List.of(names).contains(item.name);
        }

        /**
         * Method used to check if the name of the item is a partial match for any of the provided names.
         *
         * @param names All names that will be matched against
         * @return A {@link Predicate} that will check if the name of the item is a partial match for any of the provided names
         */
        private static Predicate<Item> nameContains(String... names) {
            return item -> Stream.of(names).anyMatch(name -> item.name.contains(name));
        }

        /**
         * Updates the quality of a product. This assumes the sellIn has already been lowered.
         *
         * @param item   The {@link Item} that will be updated by reference.
         * @param amount The amount of quality that needs to be changed
         */
        private static void changeQuality(Item item, int amount) {
            // Decrease quality by specified amount
            item.quality += amount;

            // Sell date has passed, quality degrades twice as fast
            // (not if quality improves) TODO maybe, in original, wine increments by 2 after sell by. Intentional? Remove && amount < 0 if yes
            if (item.sellIn < 0 && amount < 0) {
                item.quality += amount;
            }

            // quality cannot be lower than 0
            if (item.quality < 0) {
                item.quality = 0;
            }
            // quality cannot be higher than 50
            if (item.quality > 50) {
                item.quality = 50;
            }
        }
    }
}
