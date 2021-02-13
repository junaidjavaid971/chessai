package app.com.chess.ai.viewmodels

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.com.chess.ai.interfaces.RequestCallback
import app.com.chess.ai.models.global.GenericValidationModel
import app.com.chess.ai.models.request.RequestModel
import app.com.chess.ai.models.response.ResponseModel
import app.com.chess.ai.utils.Event
import java.lang.reflect.Method
import java.util.regex.Pattern

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var retryCount: Int? = 0
    var dialogLiveData = MutableLiveData<Boolean>()
        protected set

    private var mLastClickTime: Long = 0L
    val setEventLiveData = MutableLiveData<Event<Boolean>>()

    val getEventLiveData: LiveData<Event<Boolean>>
        get() = setEventLiveData

    var errorLiveData = MutableLiveData<ResponseModel<*>>()
        protected set

    var validationLiveDate = MutableLiveData<GenericValidationModel>()
        protected set
    var backPressLiveData = MutableLiveData<Boolean>()
        protected set

    var viewClickedLiveData = MutableLiveData<Int>()
        protected set
    fun onBackPressed() {
        backPressLiveData.postValue(true)
    }

    fun <t : Any> callToRepo(
        repo: t,
        methodName: String,
        requestModel: RequestModel<*>,
        reqCallback: RequestCallback
    ) {
        if (retryCount == 2) {
            dialogLiveData.postValue(false)
            retryCount = 0
            return
        }
        val method: Method
        method = repo.javaClass.getDeclaredMethod(
            methodName,
            RequestCallback::class.java,
            RequestModel::class.java
        )


        dialogLiveData.postValue(true)

        method.invoke(repo, object : RequestCallback() {
            override fun <t> onResponse(response: ResponseModel<t>, code: Int) {
                dialogLiveData.postValue(false)
                if (response.success) reqCallback.onResponse(response, code)
                else {
                    errorLiveData.postValue(response)
                }
            }
        }, requestModel)
    }

    fun isMultipleCall(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
            return false
        }
    }
}