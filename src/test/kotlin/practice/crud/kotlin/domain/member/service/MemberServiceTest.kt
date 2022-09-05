package practice.crud.kotlin.domain.member.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.MemberNotExistException
import practice.crud.kotlin.global.util.CurrentMemberUtil

@SpringBootTest
class MemberServiceTest(
    @Autowired
    private val memberService: MemberService,
    @Autowired
    private val memberRepository: MemberRepository,
    @Autowired
    private val currentMemberUtil: CurrentMemberUtil,
    @Autowired
    private val tokenProvider: TokenProvider,
) {

    @BeforeEach
    fun initMember(){
        val memberReqDto = MemberReqDto(
            email = "test@gmail.com",
            name = "test",
            password = "12345678"
        )
        memberService.joinMember(memberReqDto)
    }
    @AfterEach
    fun resetUser(){
        memberRepository.deleteAll()
    }


    @Test
    fun joinMember() {
        //given
        val memberReqDto = MemberReqDto(
            email = "test1@gmail.com",
            name = "test",
            password = "12345678"
        )

        //when
        val memberIdx = memberService.joinMember(memberReqDto)

        //then
        val member =
            memberRepository.findById(memberIdx).orElseThrow { MemberNotExistException(ErrorCode.NOT_EXIST_MEMBER) }
        assertThat(member.email).isEqualTo(memberReqDto.email)
        assertThat(member.name).isEqualTo(memberReqDto.name)

    }

    @Test
    fun signIn() {
        //given
        val signInReqDto = SignInReqDto(
            email = "test@gmail.com",
            password = "12345678"
        )

        //when
        val signInResDto = memberService.signIn(signInReqDto)

        //then
        assertThat(signInResDto.email).isEqualTo(signInReqDto.email)

        assertThat(tokenProvider.isTokenExpired(signInResDto.accessToken)).isEqualTo(false)
        assertThat(tokenProvider.getTokenType(signInResDto.accessToken)).isEqualTo("accessToken")
        assertThat(tokenProvider.getUserEmail(signInResDto.accessToken)).isEqualTo("test@gmail.com")

        assertThat(tokenProvider.isTokenExpired(signInResDto.refreshToken)).isEqualTo(false)
        assertThat(tokenProvider.getTokenType(signInResDto.refreshToken)).isEqualTo("refreshToken")
        assertThat(tokenProvider.getUserEmail(signInResDto.refreshToken)).isEqualTo("test@gmail.com")
    }

    @Test
    fun logout() {
    }

    @Test
    fun withdrawal() {
    }

    @Test
    fun refresh() {
    }
}