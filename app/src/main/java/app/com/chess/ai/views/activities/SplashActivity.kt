package app.com.chess.ai.views.activities

import android.os.Bundle
import android.os.Handler
import app.com.chess.ai.R
import app.com.chess.ai.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_splash)
        supportActionBar?.hide()
        Handler().postDelayed({
            replaceActivity(MainActivity::class.java)
        }, 2000)
    }
}