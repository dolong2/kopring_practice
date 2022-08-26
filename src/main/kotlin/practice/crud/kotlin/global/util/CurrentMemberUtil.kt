package practice.crud.kotlin.global.util

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import practice.crud.kotlin.domain.member.Member
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.config.security.auth.AuthDetails

@Component
class CurrentMemberUtil(
    private val memberRepository: MemberRepository
){
    private fun getCurrentEmail():String{
        val principal = SecurityContextHolder.getContext().authentication.principal
        val email: String
        if(principal is UserDetails){
            email = (principal as AuthDetails).getEmail()
        }else{
            email = principal.toString()
        }
        return email
    }

    fun getCurrentMember():Member =
        memberRepository.findByEmail(getCurrentEmail())
            .orElseThrow{RuntimeException()}
}