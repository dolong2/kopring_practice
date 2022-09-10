package practice.crud.kotlin.domain.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import practice.crud.kotlin.domain.member.dto.req.MemberReqDto


@SpringBootTest
@AutoConfigureMockMvc
internal class AuthControllerTest @Autowired constructor(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    private val objectMapper: ObjectMapper,
) {

    @Test
    fun signUp() {
        //given
        val memberReqDto = MemberReqDto(
            email = "test1@gmail.com",
            name = "test",
            password = "12345678"
        )
        val bodyData = objectMapper.writeValueAsString(memberReqDto)

        //when
        mockMvc.perform(post("/v1/signup")
            .content(bodyData)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))

        //then
            .andExpect(status().isOk)
    }

    @Test
    fun signIn() {
    }

    @Test
    fun refresh() {
    }
}