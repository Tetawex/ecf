package org.tetawex.ecf.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tetawex on 28.01.17.
 */
public class Bundle {
    private Map<String, Object> items;

    public Bundle() {
        items = new HashMap<String, Object>();
    }

    public void putItem(String key, Object item) {
        items.put(key, item);
    }

    public <T> T getItem(String key, Class<T> c) {
        return c.cast(items.get(key));
    }

    public <T> T getItem(String key, Class<T> c, T defaultValue) {
        if (items.containsKey(key))
            return c.cast(items.get(key));
        return defaultValue;
    }
}
