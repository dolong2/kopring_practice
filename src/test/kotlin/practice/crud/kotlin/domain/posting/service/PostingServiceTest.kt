package practice.crud.kotlin.domain.posting.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.service.MemberService
import practice.crud.kotlin.domain.posting.dto.req.PostingReqDto
import practice.crud.kotlin.domain.posting.repository.PostingRepository
import practice.crud.kotlin.global.config.security.auth.AuthDetailService
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import java.lang.RuntimeException
import java.time.LocalDate

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


    @Test
    fun writePosting(){
        val postingReqDto: PostingReqDto = PostingReqDto(title = "title", content = "content")

        val postingIdx = postingService.writePosting(postingReqDto)

        val posting = postingRepository.findById(postingIdx).orElseThrow { RuntimeException() }

        assertThat(posting.title).isEqualTo(postingReqDto.title)
        assertThat(posting.content).isEqualTo(postingReqDto.content)
        assertThat(posting.date).isEqualTo(LocalDate.now())
    }
}