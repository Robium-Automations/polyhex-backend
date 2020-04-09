package com.robiumautomations.polyhex.dtos.users

import java.util.*

class UpdatedUser(
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: Date? = null,
    val studyProgram: String? = null,
    val points: Int = 0,
    val avatar: String? = null
) {

  constructor() : this(null, null, null, null, 0, null)
}
