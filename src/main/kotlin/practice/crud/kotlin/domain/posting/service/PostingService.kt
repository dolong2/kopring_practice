package practice.crud.kotlin.domain.posting.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import practice.crud.kotlin.domain.posting.Posting
import practice.crud.kotlin.domain.posting.dto.req.PostingReqDto
import practice.crud.kotlin.domain.posting.dto.req.PostingUpdateReqDto
import practice.crud.kotlin.domain.posting.dto.res.PostingResDto
import practice.crud.kotlin.domain.posting.repository.PostingRepository
import practice.crud.kotlin.global.util.ResponseDtoUtil

@Service
class PostingService(
    private val postingRepository: PostingRepository,
){
    @Transactional
    fun writePosting(postingReqDto: PostingReqDto): Long {
        val posting = postingReqDto.toEntity()
        return postingRepository.save(posting).id
    }

    @Transactional
    fun deletePosting(postingIdx: Long) {
        postingRepository.deleteById(postingIdx)
    }

    @Transactional
    fun updatePosting(postingIdx: Long, postingUpdateReqDto: PostingUpdateReqDto) {
        val posting = postingRepository.findById(postingIdx).orElseThrow { RuntimeException() }
        posting.update(posting.title, posting.content)
    }

    @Transactional(readOnly = true)
    fun getAllPosting(): List<PostingResDto>{
        val postings = postingRepository.findAll();
        return postings.map { PostingResDto(it) }
    }
    
    @Transactional(readOnly = true)
    fun getOnePosting(postingIdx: Long): PostingResDto?{
        val posting = postingRepository.findById(postingIdx).orElseThrow { RuntimeException() }
        val result = ResponseDtoUtil.mapping(posting, PostingResDto::class.java)
        return result
    }
}