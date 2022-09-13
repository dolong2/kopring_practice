package practice.crud.kotlin.global.filter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import practice.crud.kotlin.global.exception.ErrorCode
import practice.crud.kotlin.global.exception.exception.AccessTokenExpiredException
import practice.crud.kotlin.global.exception.exception.BasicException
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtExceptionFilter : OncePerRequestFilter() {

    private val objectMapper: ObjectMapper? = null
    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: AccessTokenExpiredException) {
            writeLog(request, response, e)
            return
        } catch (e: Exception) {
            writeLog(request, response, e)
        }
    }

    @Throws(IOException::class)
    private fun writeLog(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: BasicException,
    ) {
        log.error(request.requestURI)
        log.error(e.errorCode.msg)
        e.printStackTrace()
        writeBody(response, e)
    }

    @Throws(IOException::class)
    private fun writeBody(response: HttpServletResponse, e: BasicException) {
        val json = getJson(e)
        response.status = e.errorCode.code
        response.contentType = "Application/json"
        response.writer.write(json)
    }

    @Throws(JsonProcessingException::class)
    private fun getJson(e: BasicException): String {
        val map: MutableMap<String, Any?> = HashMap()
        map["msg"] = e.message
        map["status"] = e.errorCode.code
        return objectMapper!!.writeValueAsString(map)
    }

    @Throws(IOException::class)
    private fun writeLog(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: Exception,
    ) {
        log.error(request.requestURI)
        log.error("알수없는 에러")
        e.printStackTrace()
        writeBody(response, e)
    }

    @Throws(IOException::class)
    private fun writeBody(response: HttpServletResponse, e: Exception) {
        val json = getJson(e)
        response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        response.contentType = "Application/json"
        response.writer.write(json)
    }

    @Throws(JsonProcessingException::class)
    private fun getJson(e: Exception): String {
        val map: MutableMap<String, Any?> = HashMap()
        map["msg"] = e.message
        map["status"] = HttpStatus.INTERNAL_SERVER_ERROR.value()
        return objectMapper!!.writeValueAsString(map)
    }
}