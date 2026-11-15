package org.ihab.authservice.data.model
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "users")
class User(
    @Column(unique = true, nullable = false)
    var email: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    var role: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: UUID? = null
}