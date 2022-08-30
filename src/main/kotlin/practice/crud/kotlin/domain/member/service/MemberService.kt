package practice.crud.kotlin.domain.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.dto.res.SignInResDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.MemberAlreadyExistException
import practice.crud.kotlin.global.exception.exception.MemberNotExistException
import practice.crud.kotlin.global.exception.exception.PasswordNotCorrectException
import practice.crud.kotlin.global.util.CurrentMemberUtil
import kotlin.RuntimeException

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val currentMemberUtil: CurrentMemberUtil
){
    fun joinMember(memberReqDto: MemberReqDto): Long{
        if(memberRepository.existsByEmail(memberReqDto.email))
            throw MemberAlreadyExistException(ErrorCode.USER_ALREADY_EXIST)
        val encodedPassword = passwordEncoder.encode(memberReqDto.password)
        val member = memberReqDto.toEntity(encodedPassword)
        return memberRepository.save(member).id
    }

    fun signIn(signInDto: SignInReqDto):SignInResDto{
        if(!memberRepository.existsByEmail(signInDto.email))
            throw MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER)
        val member = memberRepository.findByEmail(signInDto.email)
            .orElseThrow { MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER) }
        if (!passwordEncoder.matches(signInDto.password, member.password))
            throw PasswordNotCorrectException(ErrorCode.PASSWORD_NOT_CORRECT)//패스워드 일치 X
        val accessToken = tokenProvider.createAccessToken(member.email)
        val refreshToken = tokenProvider.createRefreshToken(member.email)
        member.updateRefreshToken(refreshToken)
        return SignInResDto(
            email = member.email,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun logout(){
        val member = currentMemberUtil.getCurrentMember()
        member.updateRefreshToken(null)
    }

    fun withdrawal(){
        logout()
        val member = currentMemberUtil.getCurrentMember()
        memberRepository.delete(member)
    }
}