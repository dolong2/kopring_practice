package practice.crud.kotlin.domain.posting.facade

import org.springframework.stereotype.Component
import practice.crud.kotlin.domain.posting.Posting
import practice.crud.kotlin.domain.posting.repository.PostingRepository
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.PostingNotExistException
import practice.crud.kotlin.global.util.CurrentMemberUtil

@Component
class PostingFacade(
    private val postingRepository: PostingRepository,
    private val currentMemberUtil: CurrentMemberUtil,
){
    fun findPostingById(id: Long): Posting =
        postingRepository.findById(id)
            .orElseThrow { throw PostingNotExistException(ErrorCode.POSTING_NOT_EXIST) }

    fun isWriterSame(posting: Posting): Boolean =
        posting.writer == currentMemberUtil.getCurrentMember()
}