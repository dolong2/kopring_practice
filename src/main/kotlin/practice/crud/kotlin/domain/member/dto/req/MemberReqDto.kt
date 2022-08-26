package practice.crud.kotlin.domain.member.dto.req

import practice.crud.kotlin.domain.member.Member
import practice.crud.kotlin.domain.member.Role
import java.util.Collections

class MemberReqDto(
    val name: String,
    val email: String,
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