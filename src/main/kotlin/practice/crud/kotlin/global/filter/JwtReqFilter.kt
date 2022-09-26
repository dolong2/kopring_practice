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
import practice.crud.kotlin.global.exception.exception.TokenNotValidException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.RuntimeException

@Component
class JwtReqFilter(
    val tokenProvider: TokenProvider,
    val registerUserInfo: RegisterUserInfo,
): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val accessToken = request.getHeader("Authorization")
        if(accessToken!=null){
            if(tokenProvider.isTokenExpired(accessToken)){
                throw AccessTokenExpiredException(ErrorCode.TOKEN_EXPIRED)//토큰만료
            }else if(!tokenProvider.getTokenType(accessToken).equals("accessToken")){
                throw TokenNotValidException(ErrorCode.TOKEN_NOT_VALID)
            }
            registerUserInfo.registerSecurityContext(request, accessToken)
        }
        filterChain.doFilter(request, response)
    }
}