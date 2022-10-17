package practice.crud.kotlin.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import practice.crud.kotlin.domain.member.Member

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String?):Member?

    fun existsByEmail(email: String):Boolean
}