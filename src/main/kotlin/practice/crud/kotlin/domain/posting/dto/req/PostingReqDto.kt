package practice.crud.kotlin.domain.posting.dto.req

import org.jetbrains.annotations.NotNull
import practice.crud.kotlin.domain.posting.Posting
import java.time.LocalDate
import javax.validation.constraints.NotBlank

class PostingReqDto(
    @field:NotNull
    val title:String,
    @field:NotNull
    val content:String,
) {
    fun toEntity(): Posting{
        return Posting(
            title = this.title,
            content = this.content,
            date = LocalDate.now(),
        )
    }
}