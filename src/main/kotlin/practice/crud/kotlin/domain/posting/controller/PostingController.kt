package practice.crud.kotlin.domain.posting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import practice.crud.kotlin.domain.posting.dto.req.PostingReqDto
import practice.crud.kotlin.domain.posting.dto.res.PostingResDto
import practice.crud.kotlin.domain.posting.service.PostingService
import practice.crud.kotlin.global.response.SuccessResponse
import javax.validation.Valid

@RestController
@RequestMapping("/v1/posting")
class PostingController(
    private val postingService: PostingService
){
    @PostMapping
    fun writePosting(@RequestBody @Valid postingReqDto: PostingReqDto): ResponseEntity<SuccessResponse>{
        postingService.writePosting(postingReqDto)
        return ResponseEntity(SuccessResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{postingIdx}")
    fun deletePosting(@PathVariable postingIdx: Long): ResponseEntity<SuccessResponse>{
        postingService.deletePosting(postingIdx)
        return ResponseEntity(SuccessResponse, HttpStatus.OK)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<PostingResDto>>{
        val result = postingService.getAllPosting();
        return ResponseEntity(result, HttpStatus.OK)
    }

    @GetMapping("/{postingIdx}")
    fun getOne(@PathVariable postingIdx: Long): ResponseEntity<PostingResDto>{
        val result = postingService.getOnePosting(postingIdx)
        return ResponseEntity(result, HttpStatus.OK)
    }
}