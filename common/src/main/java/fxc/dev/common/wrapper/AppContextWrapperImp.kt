package fxc.dev.common.wrapper

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.*

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

open class AppContextWrapperImp(
    base: Context
) : AppContextWrapper, ContextWrapper(base) {
    override fun wrap(baseContext: Context, language: String): ContextWrapper {
        var context = baseContext
        val config =
            context.resources.configuration
        var sysLocale: Locale? = null
        sysLocale = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            getSystemLocale(config)
        } else {
            getSystemLocaleLegacy(config)
        }
        if (language != "" && sysLocale.language != language) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale)
            } else {
                setSystemLocaleLegacy(config, locale)
            }
        }
        context = context.createConfigurationContext(config)
        return AppContextWrapperImp(context)
    }

    @SuppressWarnings("deprecation")
    private fun getSystemLocaleLegacy(config: Configuration): Locale {
        return config.locale
    }

    @SuppressWarnings("deprecation")
    private fun setSystemLocaleLegacy(
        config: Configuration,
        locale: Locale?
    ) {
        config.locale = locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun getSystemLocale(config: Configuration): Locale {
        return config.locales[0]
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun setSystemLocale(
        config: Configuration,
        locale: Locale?
    ) {
        config.setLocale(locale)
    }
}