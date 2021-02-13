package app.com.chess.ai

import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import app.com.chess.ai.utils.SharePrefData
import app.com.chess.ai.activities.BaseActivity


class _AppController : MultiDexApplication() {

    companion object {
        var token: String = ""
        var INSTANCE: _AppController? = null
        var baseActivity: BaseActivity<ViewDataBinding>? = null

        fun setActivity(activity: BaseActivity<ViewDataBinding>) {
            baseActivity = activity
        }

        fun getActivity(): BaseActivity<ViewDataBinding>? {
            return baseActivity
        }

        fun getInstance(): _AppController? {
            return INSTANCE
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        INSTANCE = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        SharePrefData.instance.setContext(applicationContext)
    }

}