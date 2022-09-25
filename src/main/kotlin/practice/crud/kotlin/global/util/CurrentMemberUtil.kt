package practice.crud.kotlin.global.util

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import practice.crud.kotlin.domain.member.Member
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.config.security.auth.AuthDetails

@Component
class CurrentMemberUtil(
    private val memberRepository: MemberRepository
){
    private fun getCurrentEmail():String =
        (SecurityContextHolder.getContext().authentication.principal as AuthDetails)
            .getEmail()

    fun getCurrentMember():Member =
        memberRepository.findByEmail(getCurrentEmail())
            .orElseThrow{RuntimeException()}
}