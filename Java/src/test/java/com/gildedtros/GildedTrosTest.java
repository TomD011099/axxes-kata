package com.gildedtros;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedTrosTest {

    @Test
    void updateQuality_defaultItem_decreasesBy1() {
        GildedTros app = new GildedTros(
                new Item("Shardblade", 5, 10)
        );

        updateAndAssert(app, 4, 9);
    }

    @Test
    void updateQuality_goodWine_decreasesBy2() {
        GildedTros app = new GildedTros(
                new Item("Good Wine", 0, 10)
        );

        updateAndAssert(app, -1, 11);
    }

    @Test
    void updateQuality_legendary_noChangesAndQuality80() {
        GildedTros app = new GildedTros(
                new Item("B-DAWG Keychain", 4, 10)
        );

        updateAndAssert(app, 4, 80);
    }

    @Test
    void updateQuality_backstageMoreThan10Days_increasesBy1() {
        GildedTros app = new GildedTros(
                new Item("Backstage passes: Devoxx", 15, 10)
        );

        updateAndAssert(app, 14, 11);
    }

    @Test
    void updateQuality_backstageBetween10And5Days_increasesBy2() {
        GildedTros app = new GildedTros(
                new Item("Backstage passes: Devoxx", 7, 10)
        );

        updateAndAssert(app, 6, 12);
    }

    @Test
    void updateQuality_backstageLessThan5Days_increasesBy3() {
        GildedTros app = new GildedTros(
                new Item("Backstage passes: Devoxx", 4, 10)
        );

        updateAndAssert(app, 3, 13);
    }

    @Test
    void updateQuality_backstagePassed_increasesBy3() {
        GildedTros app = new GildedTros(
                new Item("Backstage passes: Devoxx", -1, 10)
        );

        updateAndAssert(app, -2, 0);
    }

    @Test
    void updateQuality_fullTest() {
        GildedTros app = new GildedTros(
                new Item("Backstage passes: Devoxx", 12, 10)
        );

        updateAndAssert(app, 11, 11);
        updateAndAssert(app, 10, 12);
        updateAndAssert(app, 9, 14);
        updateAndAssert(app, 8, 16);
        updateAndAssert(app, 7, 18);
        updateAndAssert(app, 6, 20);
        updateAndAssert(app, 5, 22);
        updateAndAssert(app, 4, 25);
        updateAndAssert(app, 3, 28);
        updateAndAssert(app, 2, 31);
        updateAndAssert(app, 1, 34);
        updateAndAssert(app, 0, 37);
        updateAndAssert(app, -1, 0);
    }

    @Test
    void updateQuality_smellyItem_decreasesBy2() {
        GildedTros app = new GildedTros(
                new Item("Duplicate Code", 5, 10)
        );

        updateAndAssert(app, 4, 8);
    }

    /**
     * Run updateQuality on the provided {@link GildedTros} and run assertions.
     * Only works with one {@link Item}
     *
     * @param app The {@link GildedTros} object with all {@link Item}s
     * @param expectedSellIn The expected sellIn value after the update
     * @param expectedQuality The expected quality value after the update
     */
    private static void updateAndAssert(GildedTros app,
                                        int expectedSellIn,
                                        int expectedQuality) {
        app.updateQuality();
        assertEquals(expectedSellIn, app.getItems().get(0).sellIn);
        assertEquals(expectedQuality, app.getItems().get(0).quality);
    }
}
