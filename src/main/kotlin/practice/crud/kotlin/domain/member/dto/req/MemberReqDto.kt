package practice.crud.kotlin.domain.member.dto.req

import practice.crud.kotlin.domain.member.Member
import practice.crud.kotlin.domain.member.Role
import java.util.Collections
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class MemberReqDto(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val name: String,
    @field:NotBlank
    @field:Size(min = 8, max = 85)
    val password: String,
){
    fun toEntity(password: String): Member =
        Member(
            email = email,
            password = password,
            name = name,
            roles = Collections.singletonList(Role.ROLE_MEMBER)
        )
}