package app.com.chess.ai

import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import app.com.chess.ai.views.activities.BaseActivity
import app.com.chess.ai.utils.SharePrefData


class _AppController : MultiDexApplication() {

    companion object {
        //Constants
        const val showAlphabets = true
        const val levelsPerPage = 16
        const val timer: Long = 60000 //30000ms is 30 sec.

        //Objects
        var INSTANCE: _AppController? = null
        var baseActivity: BaseActivity<ViewDataBinding>? = null
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        INSTANCE = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        SharePrefData.instance.setContext(applicationContext)
    }

}