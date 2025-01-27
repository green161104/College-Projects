package com.cmu.estg.cmu_geocaching.util

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import java.util.Locale

class LanguageContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, language: String): ContextWrapper {
            val config = context.resources.configuration
            val locale = when (language) {
                "Portuguese" -> Locale("pt", "PT")
                "Lithuanian" -> Locale("lt", "LT")
                else -> Locale("en", "US")
            }

            Locale.setDefault(locale)
            config.setLocale(locale)
            config.setLocales(LocaleList(locale))

            context.resources.updateConfiguration(config, context.resources.displayMetrics)

            return LanguageContextWrapper(context)
        }
    }
}