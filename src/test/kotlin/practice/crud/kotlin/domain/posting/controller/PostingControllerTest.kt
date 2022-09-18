package practice.crud.kotlin.domain.posting.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.event.annotation.BeforeTestClass
import org.springframework.test.context.event.annotation.BeforeTestMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.dto.res.SignInResDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.domain.member.service.MemberService
import practice.crud.kotlin.domain.posting.Posting
import practice.crud.kotlin.domain.posting.dto.req.PostingReqDto
import practice.crud.kotlin.domain.posting.repository.PostingRepository
import practice.crud.kotlin.global.config.security.auth.AuthDetailService
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import practice.crud.kotlin.global.util.CurrentMemberUtil
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
internal class PostingControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
    @Autowired
    private val memberService: MemberService,
    @Autowired
    private val authDetailService: AuthDetailService,
    @Autowired
    private val memberRepository: MemberRepository,
    @Autowired
    private val tokenProvider: TokenProvider,
    @Autowired
    private val currentMemberUtil: CurrentMemberUtil,
    @Autowired
    private val postingRepository: PostingRepository,
) {

    var signInResDto: SignInResDto = SignInResDto(email = "", accessToken = "", refreshToken = "")

    @BeforeEach
    fun signInMember(){
        val memberReqDto = MemberReqDto(
            email = "test@gmail.com",
            name = "test",
            password = "12345678"
        )
        memberService.joinMember(memberReqDto)
        val signInReqDto = SignInReqDto(
            email = "test@gmail.com",
            password = "12345678"
        )
        signInResDto = memberService.signIn(signInReqDto)
        val userDetail = authDetailService.loadUserByUsername(tokenProvider.getUserEmail(signInResDto.accessToken))
        val authenticationToken = UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
        SecurityContextHolder.getContext().authentication = authenticationToken
    }

    @AfterEach
    fun resetMember(){
        memberRepository.deleteAll()
    }

    @Test
    fun writePosting() {
        //given
        val postingReqDto = PostingReqDto(title = "title", content = "content")
        val body = objectMapper.writeValueAsString(postingReqDto)

        //when
        mockMvc.perform(post("/v1/posting")
            .content(body)
            .header("Authorization", signInResDto.accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))

        //then
            .andExpect(status().isOk)
    }

    @Test
    fun deletePosting() {
        //given
        val posting = Posting(
            title = "title",
            content = "content",
            writer = currentMemberUtil.getCurrentMember(),
            date = LocalDate.now(),
            fixed = false
        )
        val id = postingRepository.save(posting).id

        //when
        mockMvc.perform(delete("/v1/posting/"+id)
            .header("Authorization", signInResDto.accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))

        //then
            .andExpect(status().isOk)

    }

    @Test
    fun getAll() {
    }

    @Test
    fun getOne() {
    }

    @Test
    fun updatePosting() {
    }
}