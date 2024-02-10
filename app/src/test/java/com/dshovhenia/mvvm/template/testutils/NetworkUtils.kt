package com.dshovhenia.mvvm.template.testutils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okio.Buffer
import java.nio.charset.Charset

fun bufferFromFile(fileName: String) =
    Buffer().readFrom(
        input = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName),
    )

fun textFromFile(fileName: String) =
    bufferFromFile(fileName).readString(Charset.defaultCharset())

inline fun <reified T> Gson.deserializeFromFile(fileName: String): T =
    fromJson(textFromFile(fileName), object : TypeToken<T>() {}.type)

inline fun <reified T> deserializeFromFile(fileName: String): T =
    Gson().deserializeFromFile(fileName)
