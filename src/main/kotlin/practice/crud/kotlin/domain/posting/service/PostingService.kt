package practice.crud.kotlin.domain.posting.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import practice.crud.kotlin.domain.member.Role
import practice.crud.kotlin.domain.posting.dto.req.PostingReqDto
import practice.crud.kotlin.domain.posting.dto.req.PostingUpdateReqDto
import practice.crud.kotlin.domain.posting.dto.res.PostingResDto
import practice.crud.kotlin.domain.posting.dto.res.PostingListResDto
import practice.crud.kotlin.domain.posting.repository.PostingRepository
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.NotWriterException
import practice.crud.kotlin.global.exception.exception.PostingNotExistException
import practice.crud.kotlin.global.util.CurrentMemberUtil

@Service
class PostingService(
    private val postingRepository: PostingRepository,
    private val currentMemberUtil: CurrentMemberUtil,
){

    fun writePosting(postingReqDto: PostingReqDto): Long {
        val posting = postingReqDto.toEntity(currentMemberUtil.getCurrentMember())
        return postingRepository.save(posting).id
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deletePosting(postingIdx: Long) {
        val posting = postingRepository.findById(postingIdx)
            .orElseThrow { throw PostingNotExistException(ErrorCode.POSTING_NOT_EXIST) }
        if(posting.writer != currentMemberUtil.getCurrentMember() && !currentMemberUtil.getCurrentMember().roles.contains(Role.ROLE_ADMIN)){
            throw NotWriterException(ErrorCode.NOT_WRITER_EXCEPTION)
        }
        postingRepository.deleteById(postingIdx)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updatePosting(postingIdx: Long, postingUpdateReqDto: PostingUpdateReqDto) {
        val posting = postingRepository.findById(postingIdx).orElseThrow { RuntimeException() }
        val writer = currentMemberUtil.getCurrentMember()
        if(posting.writer != writer){
            throw NotWriterException(ErrorCode.NOT_WRITER_EXCEPTION)
        }
        posting.update(postingUpdateReqDto)
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    fun getAllPosting(): PostingListResDto{
        val postings = postingRepository.findAll();
        return PostingListResDto(
            postings.map { PostingResDto(it) }
        )
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    fun getOnePosting(postingIdx: Long): PostingResDto {
        val posting = postingRepository.findById(postingIdx).orElseThrow { RuntimeException() }
        return PostingResDto(posting)
    }
}