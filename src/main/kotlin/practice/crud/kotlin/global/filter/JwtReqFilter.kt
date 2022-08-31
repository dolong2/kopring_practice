package practice.crud.kotlin.global.filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import practice.crud.kotlin.global.config.security.auth.AuthDetailService
import practice.crud.kotlin.global.config.security.jwt.TokenProvider
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.AccessTokenExpiredException
import java.lang.RuntimeException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtReqFilter(
    val tokenProvider: TokenProvider,
    val authDetailService: AuthDetailService,
): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val accessToken = request.getHeader("Authorization")
        if(accessToken!=null && tokenProvider.getTokenType(accessToken).equals("accessToken")){
            if(tokenProvider.isTokenExpired(accessToken)){
                throw AccessTokenExpiredException(ErrorCode.TOKEN_EXPIRED)//토큰만료
            }
            registerSecurityContext(request, accessToken)
        }
        filterChain.doFilter(request, response)
    }

    private fun registerSecurityContext(request: HttpServletRequest, accessToken: String){
        val userDetail = authDetailService.loadUserByUsername(tokenProvider.getUserEmail(accessToken))
        val authenticationToken = UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authenticationToken
    }
}