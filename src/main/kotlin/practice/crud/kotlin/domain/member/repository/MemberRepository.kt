package practice.crud.kotlin.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import practice.crud.kotlin.domain.member.Member
import java.util.Optional

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String?):Optional<Member>
}