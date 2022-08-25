package practice.crud.kotlin.global.config.security.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import practice.crud.kotlin.domain.member.Member

class AuthDetails(
    val member: Member,
) :UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return member.roles
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.name
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return false
    }
}