package practice.crud.kotlin.domain.member

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ROLE_ADMIN, ROLE_MEMBER;

    override fun getAuthority(): String {
        return name
    }
}