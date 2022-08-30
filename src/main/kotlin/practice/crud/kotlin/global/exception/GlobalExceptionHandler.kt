package practice.crud.kotlin.global.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import practice.crud.kotlin.global.exception.exception.BasicException
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this.javaClass.simpleName)

    @ExceptionHandler(BasicException::class)
    fun printError(request: HttpServletRequest, ex: BasicException):ResponseEntity<ErrorResponse>{
        log.error(request.requestURI)
        log.error(ex.message)
        ex.printStackTrace()
        val errorResponse = ErrorResponse(ex.errorCode.msg, ex.errorCode.code)
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ex.errorCode.code))
    }

}