package practice.crud.kotlin.domain.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.domain.member.service.MemberService
import practice.crud.kotlin.global.config.security.jwt.TokenProvider

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
internal class MemberControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
    @Autowired
    private val memberService: MemberService,
    @Autowired
    private val memberRepository: MemberRepository,
    @Autowired
    private val tokenProvider: TokenProvider,
) {
    var refreshToken:String = ""
    var accessToken:String = ""

    @BeforeEach
    fun initMember(){
        val memberReqDto = MemberReqDto(email = "test@gmail.com", name = "test", "12345678")
        memberService.joinMember(memberReqDto)

        val signInReqDto = SignInReqDto(email = "test@gmail.com", password = "12345678")
        val signInResDto = memberService.signIn(signInReqDto)

        accessToken=signInResDto.accessToken
        refreshToken=signInResDto.refreshToken
    }
    @AfterEach
    fun resetMember(){
        memberRepository.deleteAll()
    }

    @Test
    fun logout() {
        //when
        mockMvc.perform(
            post("/v1/member/logout")
            .header("authorization",accessToken))

        //then
            .andExpect(status().isOk)
    }

    @Test
    fun withdrawal() {
        //when
        mockMvc.perform(
            delete("/v1/member/withdrawal")
            .header("authorization",accessToken))
        //then
            .andExpect(status().isOk)
    }
}