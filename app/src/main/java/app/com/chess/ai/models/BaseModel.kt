package app.com.chess.ai.models

import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
abstract class BaseModel {

}