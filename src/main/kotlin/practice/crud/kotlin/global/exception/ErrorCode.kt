package practice.crud.kotlin.global.exception

enum class ErrorCode(
    val code: Int,
    val msg: String
){
    BAD_REQUEST(400, "올바르지 않은 요청입니다."),
    FORBIDDEN(403, "금지된 요청입니다."),
    UNAUTHORIZED(401, "권한이 없습니다."),
    NOT_FOUND(404, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 에러"),

    NOT_WRITER_EXCEPTION(401, "자신의 게시물이 아닙니다."),
    NOT_EXIST_MEMBER(404, "해당 유저가 존재하지 않습니다."),
    USER_ALREADY_EXIST(400, "유저가 이미 존재합니다."),
    PASSWORD_NOT_CORRECT(400, "패스워드가 일치하지 않습니다."),
}