package practice.crud.kotlin.global.exception.exception

import practice.crud.kotlin.global.exception.ErrorCode

class PostingNotExistException(
    errorCode: ErrorCode
):BasicException(
    errorCode
)