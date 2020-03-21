package com.robiumautomations.polyhex.models

import com.robiumautomations.polyhex.enums.UserRole
import lombok.Data
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_credentials")
@Data
class UserCredentials(
    @Id
    @Column(name = "user_id")
    val userId: UserId? = UUID.randomUUID().toString(),
    @Column(name = "username")
    val username: String?,
    @Column(name = "email")
    val email: String?,
    @Column(name = "password")
    val password: String?,
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    val userRole: UserRole?
) {

  constructor() : this(username = null, email = null, password = null, userRole = null)
}