package app.com.chess.ai.models.request

import app.com.chess.ai.models.BaseModel
import com.google.gson.annotations.SerializedName

data class RequestModel<T>(
    @SerializedName("Data") var data: T?,
    @SerializedName("Sid") var sid: String?
) : BaseModel()