package app.com.chess.ai.viewmodels

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var dialogLiveData = MutableLiveData<Boolean>()
        protected set

    private var mLastClickTime: Long = 0L

    var viewClickedLiveData = MutableLiveData<Int>()
        protected set

    fun isMultipleCall(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
            return false
        }
    }
}