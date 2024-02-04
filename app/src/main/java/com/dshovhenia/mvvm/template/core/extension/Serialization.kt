package com.dshovhenia.mvvm.template.core.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.deserialize(string: String): T = fromJson(string, object : TypeToken<T>() {}.type)
