package org.tetawex.ecf.util;

import org.tetawex.ecf.model.Language;

import java.util.Map;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class FontCharacters {
    public static Language[] getSupportedLanguages() {
        return supportedLanguages;
    }

    public static void setSupportedLanguages(Language[] supportedLanguages) {
        FontCharacters.supportedLanguages = supportedLanguages;
    }

    public static Map<String, String> codeToLanguageMap;
    public static Language[] supportedLanguages;

    public static final String ru = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
            + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
            + "1234567890.,:;_¡!¿?'+-*/()[";
    public static final String en = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "1234567890.,:;_¡!¿?'+-*/()[";
}
