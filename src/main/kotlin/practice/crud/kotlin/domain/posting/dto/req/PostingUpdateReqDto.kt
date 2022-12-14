package practice.crud.kotlin.domain.posting.dto.req

import org.jetbrains.annotations.NotNull
import practice.crud.kotlin.domain.member.Member
import practice.crud.kotlin.domain.posting.Posting
import java.time.LocalDate
import javax.validation.constraints.NotBlank

class PostingUpdateReqDto(
    @field:NotNull
    @field:NotBlank
    val title:String,
    @field:NotNull
    @field:NotBlank
    val content:String,
)