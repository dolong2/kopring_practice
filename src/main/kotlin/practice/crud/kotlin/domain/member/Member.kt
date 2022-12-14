package practice.crud.kotlin.domain.member

import practice.crud.kotlin.domain.posting.Posting
import javax.persistence.*

@Entity
class Member(
    var email: String,
    var password: String,
    var name: String,
    @Enumerated(EnumType.STRING) @Column(name = "Role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Role", joinColumns = [JoinColumn(name = "member_id")])
    var roles: MutableList<Role>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
    @OneToMany(cascade = [CascadeType.REMOVE], mappedBy = "writer")
    val postings: List<Posting> = mutableListOf()
}