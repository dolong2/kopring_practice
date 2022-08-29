package practice.crud.kotlin.domain.member.dto.req

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class SignInReqDto(
    @field:Email
    @field:NotBlank
    val email:String,
    @Size(min = 8, max = 85)
    @field:NotBlank
    val password:String
)