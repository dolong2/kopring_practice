package practice.crud.kotlin.domain.posting.dto.req

import practice.crud.kotlin.domain.member.Member
import practice.crud.kotlin.domain.posting.Posting
import java.time.LocalDate
import javax.validation.constraints.NotBlank

class PostingReqDto(
    @field:NotBlank
    val title:String,
    @field:NotBlank
    val content:String,
) {
    fun toEntity(member: Member): Posting{
        return Posting(
            writer = member,
            title = this.title,
            content = this.content,
            date = LocalDate.now(),
        )
    }
}