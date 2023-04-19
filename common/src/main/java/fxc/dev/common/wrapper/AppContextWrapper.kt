package fxc.dev.common.wrapper

import android.content.Context
import android.content.ContextWrapper

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface AppContextWrapper {
    fun wrap(baseContext: Context, language: String): ContextWrapper
}