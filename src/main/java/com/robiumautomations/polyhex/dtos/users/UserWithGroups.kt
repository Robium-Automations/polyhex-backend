package com.robiumautomations.polyhex.dtos.users

import com.robiumautomations.polyhex.models.User

data class UserWithGroups (
    val user: User,
    val userGroups: List<String>
)