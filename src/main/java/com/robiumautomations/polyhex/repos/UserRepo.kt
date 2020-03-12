package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.User
import com.robiumautomations.polyhex.models.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<User, UserId>