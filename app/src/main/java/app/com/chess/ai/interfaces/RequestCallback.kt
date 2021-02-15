package app.com.chess.ai.interfaces

import app.com.chess.ai.models.response.ResponseModel

abstract class RequestCallback {
    open fun <t> onResponse(response: ResponseModel<t>?, code: Int) {}
    fun onResponse(isSuccess: Boolean?, code: Int) {}
}