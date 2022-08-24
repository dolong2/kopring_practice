package practice.crud.kotlin.domain.posting.repository

import org.springframework.data.jpa.repository.JpaRepository
import practice.crud.kotlin.domain.posting.Posting

interface PostingRepository : JpaRepository<Posting, Long> {
}