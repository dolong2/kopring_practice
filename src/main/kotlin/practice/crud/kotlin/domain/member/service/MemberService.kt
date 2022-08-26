package practice.crud.kotlin.domain.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.dto.res.SignInResDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import kotlin.RuntimeException

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder,
){
    fun joinMember(memberReqDto: MemberReqDto): Long{
        if(memberRepository.existsByEmail(memberReqDto.email))
            throw RuntimeException()//유저가 이미 존재함
        val encodedPassword = passwordEncoder.encode(memberReqDto.password)
        val member = memberReqDto.toEntity(encodedPassword)
        return memberRepository.save(member).id
    }

    fun signIn(signInDto: SignInReqDto):SignInResDto{
        if(!memberRepository.existsByEmail(signInDto.email))
            throw RuntimeException()//유저가 존재하지 않음
        val member = memberRepository.findByEmail(signInDto.email)
            .orElseThrow { RuntimeException() }
        if (!passwordEncoder.matches(signInDto.password, member.password))
            throw RuntimeException()//패스워드 일치 X
        val accessToken = tokenProvider.createAccessToken(member.email)
        val refreshToken = tokenProvider.createRefreshToken(member.email)
        return SignInResDto(
            email = member.email,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}