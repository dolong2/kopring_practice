package practice.crud.kotlin.domain.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.dto.res.SignInResDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.*
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
        val accessToken = tokenProvider.createAccessToken(member.email, member.roles)
        val refreshToken = tokenProvider.createRefreshToken(member.email)
        member.updateRefreshToken(refreshToken)
        return SignInResDto(
            email = member.email,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    @Transactional
    fun logout(){
        val member = currentMemberUtil.getCurrentMember()
        member.updateRefreshToken(null)
    }

    @Transactional
    fun withdrawal(){
        logout()
        val member = currentMemberUtil.getCurrentMember()
        memberRepository.delete(member)
    }

    @Transactional
    fun refresh(refreshToken: String):SignInResDto{
        if(tokenProvider.getTokenType(refreshToken) != "refreshToken")
            throw NotValidTokenExpiredException(ErrorCode.TOKEN_NOT_VALID)
        if(tokenProvider.isTokenExpired(refreshToken))
            throw RefreshTokenExpiredException(ErrorCode.TOKEN_EXPIRED)
        val email = tokenProvider.getUserEmail(refreshToken)

        val member = memberRepository.findByEmail(email)
            .orElseThrow { MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER) }
        val accessToken = tokenProvider.createAccessToken(email, member.roles)
        val newRefreshToken = tokenProvider.createRefreshToken(email)
        member.updateRefreshToken(newRefreshToken)
        return SignInResDto(
            email = email,
            accessToken = accessToken,
            refreshToken = newRefreshToken
        )
    }
}