package com.robiumautomations.polyhex.models

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
@Data
class UserCredentials(
    @Id
    @Column(name = "user_id")
    val userId: String? = UUID.randomUUID().toString(),
    @Column(name = "username")
    val username: String?,
    @Column(name = "password")
    val password: String?,
    @Column(name = "user_role")
    val userRole: String?
) {
    constructor(): this(username = null, password = null, userRole = null)
}