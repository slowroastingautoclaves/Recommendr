package edu.gatech.slowroastingautoclaves.recommendr.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daernwyn on 2/23/16.
 */
public class Movies {
    public static final List<Movie> ITEMS = new ArrayList<>();

    public static final Map<String, Movie> ITEM_MAP = new HashMap<>();

    public static void addItem(Movie item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.toString(), item);
    }

    private static String makeDetails(int position) {
        return ITEMS.get(position).getDescription();
    }

    public static void clear() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }
}
