package practice.crud.kotlin.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto
import practice.crud.kotlin.domain.member.dto.req.SignInReqDto
import practice.crud.kotlin.domain.member.dto.res.SignInResDto
import practice.crud.kotlin.domain.member.service.MemberService
import practice.crud.kotlin.global.response.SuccessResponse

@RestController
@RequestMapping("/v1")
class AuthController(
    private val memberService: MemberService,
){

    @PostMapping("/signup")
    fun signUp(@Validated @RequestBody memberReqDto: MemberReqDto):ResponseEntity<SuccessResponse>{
        memberService.joinMember(memberReqDto)
        return ResponseEntity(SuccessResponse, HttpStatus.OK)
    }

    @PostMapping("/signin")
    fun signIn(@Validated @RequestBody signInReqDto: SignInReqDto):ResponseEntity<SignInResDto>{
        val result = memberService.signIn(signInReqDto)
        return ResponseEntity(result, HttpStatus.OK)
    }
}