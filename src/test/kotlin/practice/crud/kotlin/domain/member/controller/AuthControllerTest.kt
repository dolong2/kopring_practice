package practice.crud.kotlin.domain.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.domain.member.service.MemberService
import practice.crud.kotlin.global.config.security.jwt.TokenProvider


@SpringBootTest
@AutoConfigureMockMvc
internal class AuthControllerTest @Autowired constructor(
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

    @BeforeEach
    fun init(){
        val memberReqDto = MemberReqDto(
            email = "test@gmail.com",
            name = "test",
            password = "12345678"
        )
        memberService.joinMember(memberReqDto)
    }

    @AfterEach
    fun resetMember(){
        memberRepository.deleteAll()
    }

    @Test
    fun signUp() {
        //given
        val memberReqDto = MemberReqDto(
            email = "test1@gmail.com",
            name = "test",
            password = "12345678"
        )
        val bodyData = objectMapper.writeValueAsString(memberReqDto)

        //when
        mockMvc.perform(post("/v1/signup")
            .content(bodyData)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))

        //then
            .andExpect(status().isOk)
    }

    @Test
    fun signIn() {
        //given
        val signInReqDto = SignInReqDto(
            email = "test@gmail.com",
            password = "12345678"
        )
        val bodyData = objectMapper.writeValueAsString(signInReqDto)

        //when
        mockMvc.perform(post("/v1/signin")
            .content(bodyData)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))

        //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value(signInReqDto.email))
            .andExpect(jsonPath("$.accessToken").isNotEmpty)
            .andExpect(jsonPath("$.refreshToken").isNotEmpty)

    }

    @Test
    fun refresh() {
        //given
        val signInReqDto = SignInReqDto(
            email = "test@gmail.com",
            password = "12345678"
        )
        val signInResDto = memberService.signIn(signInReqDto)

        //when
        mockMvc.perform(post("/v1/refresh")
            .header("refreshToken", signInResDto.refreshToken).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))

        //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").isNotEmpty)
            .andExpect(jsonPath("$.accessToken").isString)
    }
}