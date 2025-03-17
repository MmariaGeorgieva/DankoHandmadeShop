package com.danko.danko_handmade.product.model;

import lombok.Getter;

@Getter
public enum ProductSection {
    ALL("All"),
    CUPS_AND_MUGS("Cups and mugs"),
    HALLOWEEN("Halloween"),
    TEAPOTS("Teapots"),
    SUGAR_BOWLS("Sugar bowls, creamers, canisters"),
    PITCHERS("Pitchers, jugs, bottles"),
    SETS("Sets"),
    TRAYS("Trays, plates, wall art"),
    HOME_DECOR("Home decor"),
    TILES("Tiles");

    private final String displayName;

    ProductSection(String displayName) {
        this.displayName = displayName;
    }

}
