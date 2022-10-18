package practice.crud.kotlin.domain.member.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.dto.res.SignInResDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.config.redis.RedisUtil
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
    private val currentMemberUtil: CurrentMemberUtil,
    private val redisUtil: RedisUtil,
){
    fun joinMember(memberReqDto: MemberReqDto): Long{
        if(memberRepository.existsByEmail(memberReqDto.email))
            throw MemberAlreadyExistException(ErrorCode.USER_ALREADY_EXIST)
        val encodedPassword = passwordEncoder.encode(memberReqDto.password)
        val member = memberReqDto.toEntity(encodedPassword)
        return memberRepository.save(member).id
    }

    @Transactional(rollbackFor = [Exception::class])
    fun signIn(signInDto: SignInReqDto):SignInResDto{
        if(!memberRepository.existsByEmail(signInDto.email))
            throw MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER)
        val member = memberRepository.findByEmail(signInDto.email)
            ?: throw MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER)
        if (!passwordEncoder.matches(signInDto.password, member.password))
            throw PasswordNotCorrectException(ErrorCode.PASSWORD_NOT_CORRECT)//패스워드 일치 X
        val accessToken = tokenProvider.createAccessToken(member.email, member.roles)
        val refreshToken = tokenProvider.createRefreshToken(member.email)
        redisUtil.setData(member.email, refreshToken, 1000L * 60 * 60 * 24 * 30 * 6)
        return SignInResDto(
            email = member.email,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    @Transactional(rollbackFor = [Exception::class])
    fun logout(){
        val member = currentMemberUtil.getCurrentMember()
        redisUtil.deleteData(member.email)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun withdrawal(){
        logout()
        val member = currentMemberUtil.getCurrentMember()
        memberRepository.delete(member)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun refresh(refreshToken: String):SignInResDto{
        if(tokenProvider.getTokenType(refreshToken) != "refreshToken")
            throw TokenNotValidException(ErrorCode.TOKEN_NOT_VALID)
        if(tokenProvider.isTokenExpired(refreshToken))
            throw RefreshTokenExpiredException(ErrorCode.TOKEN_EXPIRED)
        val email = tokenProvider.getUserEmail(refreshToken)

        val member = memberRepository.findByEmail(email)
            ?: throw MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER)
        val accessToken = tokenProvider.createAccessToken(email, member.roles)
        val newRefreshToken = tokenProvider.createRefreshToken(email)
        redisUtil.setData(member.email, newRefreshToken, 1000L * 60 * 60 *24 * 30 * 6)
        return SignInResDto(
            email = email,
            accessToken = accessToken,
            refreshToken = newRefreshToken
        )
    }
}