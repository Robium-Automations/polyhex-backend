package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<User, String>