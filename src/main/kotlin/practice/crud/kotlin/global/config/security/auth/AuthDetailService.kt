package practice.crud.kotlin.global.config.security.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.MemberNotExistException

@Service
class AuthDetailService(
    private val memberRepository: MemberRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails =
         AuthDetails(memberRepository.findByEmail(username)
             .orElseThrow{MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER)})
}