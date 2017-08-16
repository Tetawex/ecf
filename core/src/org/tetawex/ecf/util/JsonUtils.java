package org.tetawex.ecf.util;

import com.google.gson.Gson;

/**
 * Created by tetawex on 16.08.17.
 */
public class JsonUtils {
    private static Gson gson;

    public static Gson getSerializer() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }
}
