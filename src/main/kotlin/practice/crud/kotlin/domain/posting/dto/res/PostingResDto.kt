package practice.crud.kotlin.domain.posting.dto.res

import practice.crud.kotlin.domain.posting.Posting
import java.time.LocalDate

data class PostingResDto(
    val title:String,
    val content:String,
    val date: LocalDate,
    val fixed :Boolean
) {
    constructor(posting: Posting): this(
        posting.title,
        posting.content,
        posting.date,
        posting.fixed
    )
}
