package com.robiumautomations.polyhex.dtos.users

import java.util.*

class RegistrationUser(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: Date? = null,
    val studyProgram: String? = null
) {

  constructor() : this(null, null, null, null, null, null, null)
}
