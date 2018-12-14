package org.tetawex.ecf.util

import org.tetawex.ecf.model.Language

/**
 * Created by Tetawex on 03.06.2017.
 */
object FontCharacters {

    lateinit var codeToLanguageMap: MutableMap<String, String>
    lateinit var supportedLanguages: Array<Language>

    val ru = ("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
            + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
            + "1234567890.,:;_¡!¿?'+-*/()[")
    val en = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "1234567890.,:;_¡!¿?'+-*/()[")
}
