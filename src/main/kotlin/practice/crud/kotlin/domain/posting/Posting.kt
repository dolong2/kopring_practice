package practice.crud.kotlin.domain.posting

import org.jetbrains.annotations.NotNull
import practice.crud.kotlin.domain.posting.dto.req.PostingUpdateReqDto
import java.time.LocalDate
import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class Posting(
    @field:NotNull
    var title:String,
    @field:NotNull
    var content:String,
    var date: LocalDate,
    var fixed :Boolean = false,
){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long=0
    fun update(title:String, content: String ){
        this.title = title
        this.content = content
        this.fixed = true
    }
}