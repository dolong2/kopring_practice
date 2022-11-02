package practice.crud.kotlin.domain.posting.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.repository.MemberRepository
import practice.crud.kotlin.domain.member.service.MemberService
import practice.crud.kotlin.domain.posting.dto.req.PostingReqDto
import practice.crud.kotlin.domain.posting.dto.req.PostingUpdateReqDto
import practice.crud.kotlin.domain.posting.repository.PostingRepository
import practice.crud.kotlin.global.config.security.auth.AuthDetailService
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import java.time.LocalDate
import kotlin.RuntimeException

@SpringBootTest
class PostingServiceTest(
    @Autowired
    private val memberService: MemberService,
    @Autowired
    private val tokenProvider: TokenProvider,
    @Autowired
    private val authDetailService: AuthDetailService,
    @Autowired
    private val postingService: PostingService,
    @Autowired
    private val postingRepository: PostingRepository,
    @Autowired
    private val memberRepository: MemberRepository,
){
    @BeforeEach
    fun initMember(){
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
        val signInResDto = memberService.signIn(signInReqDto)
        val userDetail = authDetailService.loadUserByUsername(tokenProvider.getUserEmail(signInResDto.accessToken))
        val authenticationToken = UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
        SecurityContextHolder.getContext().authentication = authenticationToken
    }

    @AfterEach
    fun resetMember(){
        memberRepository.deleteAll()
    }


    @Test
    fun writePosting(){
        //given
        val postingReqDto = PostingReqDto(title = "title", content = "content")

        //when
        val postingIdx = postingService.writePosting(postingReqDto)

        //then
        val posting = postingRepository.findById(postingIdx).orElseThrow { RuntimeException() }

        assertThat(posting.title).isEqualTo(postingReqDto.title)
        assertThat(posting.content).isEqualTo(postingReqDto.content)
        assertThat(posting.date).isEqualTo(LocalDate.now())
    }

    @Test
    fun deletePosting(){
        //given
        val postingIdx = writeTestPosting()

        //when
        postingService.deletePosting(postingIdx)

        //then
        assertThatThrownBy {postingRepository.findById(postingIdx).orElseThrow { RuntimeException() } }
    }

    @Test
    fun updatePosting(){
        //given
        val postingIdx = writeTestPosting()
        val postingUpdateReqDto = PostingUpdateReqDto(title = "updatedTitle", content = "updatedContent")

        //when
        postingService.updatePosting(postingIdx, postingUpdateReqDto)

        //then
        val posting = postingRepository.findById(postingIdx).orElseThrow { RuntimeException() }
        assertThat(posting.title).isEqualTo(postingUpdateReqDto.title)
        assertThat(posting.content).isEqualTo(postingUpdateReqDto.content)
        assertThat(posting.fixed).isEqualTo(true)
    }

    @Test
    fun getAllPosting(){
        //given
        writeTestPosting()
        writeTestPosting()
        writeTestPosting()

        //when
        val allPosting = postingService.getAllPosting()

        //then
        assertThat(allPosting.list.size).isEqualTo(3)
    }

    @Test
    fun getOnePosting(){
        //given
        val postingIdx = writeTestPosting()

        //when
        val postingResDto = postingService.getOnePosting(postingIdx)

        //then
        assertThat(postingResDto.title).isEqualTo("title")
        assertThat(postingResDto.content).isEqualTo("content")
    }

    private fun writeTestPosting():Long{
        val postingReqDto = PostingReqDto(title = "title", content = "content")
        return postingService.writePosting(postingReqDto)
    }
}