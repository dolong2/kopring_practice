package practice.crud.kotlin.global.config.security.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import practice.crud.kotlin.domain.member.repository.MemberRepository

@Service
class AuthDetailService(
    private val memberRepository: MemberRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return AuthDetails(memberRepository.findByEmail(username).orElseThrow{RuntimeException()})
    }
}