package practice.crud.kotlin.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import practice.crud.kotlin.domain.member.service.MemberService
import practice.crud.kotlin.global.response.SuccessResponse

@RestController
@RequestMapping("/v1/member")
class MemberController(
    private val memberService: MemberService,
){

    @PostMapping("/logout")
    fun logout():ResponseEntity<SuccessResponse>{
        memberService.logout()
        return ResponseEntity(SuccessResponse, HttpStatus.OK)
    }

    @DeleteMapping("/withdrawal")
    fun withdrawal():ResponseEntity<SuccessResponse>{
        memberService.withdrawal()
        return ResponseEntity(SuccessResponse, HttpStatus.OK)
    }
}