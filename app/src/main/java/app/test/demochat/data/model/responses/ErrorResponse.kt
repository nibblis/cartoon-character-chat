package app.test.demochat.data.model.responses

data class ErrorResponse(
    val detail: Detail
) {
    data class Detail(
        val message: String
    )
}