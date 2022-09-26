package practice.crud.kotlin.global.filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import practice.crud.kotlin.global.config.security.auth.AuthDetailService
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import javax.servlet.http.HttpServletRequest

@Component
class RegisterUserInfo(
    private val authDetailService: AuthDetailService,
    private val tokenProvider: TokenProvider,
){
    fun registerSecurityContext(request: HttpServletRequest, accessToken: String){
        val userDetail = authDetailService.loadUserByUsername(tokenProvider.getUserEmail(accessToken))
        val authenticationToken = UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authenticationToken
    }
}