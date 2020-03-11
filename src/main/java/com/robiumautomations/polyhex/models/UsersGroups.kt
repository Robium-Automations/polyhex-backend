package com.robiumautomations.polyhex.models

import lombok.Data
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users_groups")
@Data
class UsersGroups(
    @Id
    @Column(name = "id")
    val id: String = UUID.randomUUID().toString(),
    @Column(name = "user_id")
    val userId: UserId?,
    @Column(name = "study_group_id")
    val groupId: String?
) {

  constructor() : this(userId = null, groupId = null)
}