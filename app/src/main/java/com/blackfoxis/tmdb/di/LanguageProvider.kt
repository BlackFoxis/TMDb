package com.blackfoxis.tmdb.di

import android.util.Log
import javax.inject.Inject
import java.util.Locale
import javax.inject.Singleton

interface LanguageProvider {
    fun getTmdbLanguageCode(): String
}

@Singleton
class DefaultLanguageProvider @Inject constructor() : LanguageProvider {
    companion object {
        private const val DEFAULT_LANGUAGE = "en"
    }

    override fun getTmdbLanguageCode(): String {
        val systemLanguage = Locale.getDefault().language

        val supportedSimpleCodes = setOf("en", "ru", "de", "fr", "es", "it", "pt", "ja", "ko", "zh")

        val chosenLanguage = if (supportedSimpleCodes.contains(systemLanguage)) {
            systemLanguage
        } else {
            DEFAULT_LANGUAGE
        }
        return chosenLanguage
    }
}