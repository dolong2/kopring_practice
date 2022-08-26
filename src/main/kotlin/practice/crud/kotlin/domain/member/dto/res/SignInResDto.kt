package practice.crud.kotlin.domain.member.dto.res

class SignInResDto(
    val email: String,
    val accessToken: String,
    val refreshToken: String,
)