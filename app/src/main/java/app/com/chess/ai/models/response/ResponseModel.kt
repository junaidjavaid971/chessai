package app.com.chess.ai.models.response

import app.com.chess.ai.models.BaseModel
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseModel<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?,
    @SerializedName("ftb") val ftb : Int? = 0
) : BaseModel(), Serializable
{
    @SerializedName("hash")
    var hash: String?=null
    var requireDecryption: Boolean = true
}