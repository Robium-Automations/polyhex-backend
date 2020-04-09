package com.robiumautomations.polyhex.models

import lombok.Data
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

typealias UserId = String

@Entity
@Table(name = "users")
@Data
class User(
    @Id
    @Column(name = "user_id")
    val userId: UserId?,
    @Column(name = "username")
    val username: String?,
    @Column(name = "fname")
    val firstName: String?,
    @Column(name = "lname")
    val lastName: String?,
    @Column(name = "bday")
    val birthday: Date?,
    @Column(name = "study_program")
    val studyProgram: String?,
    @Column(name = "points")
    val points: Int,
    @Column(name = "avatar")
    val avatar: String?,
    @Column(name = "university_id")
    var universityId: String?
) {

  @JvmOverloads
  constructor(
      userId: String,
      username: String,
      firstName: String? = null,
      lastName: String? = null,
      studyProgram: String? = null,
      birthday: Date? = null
  ) : this(
      userId = userId,
      username = username,
      firstName = firstName,
      lastName = lastName,
      birthday = birthday,
      studyProgram = studyProgram,
      points = 0,
      avatar = null,
      universityId = null
  )

  constructor() : this(
      userId = null,
      username = null,
      firstName = null,
      lastName = null,
      birthday = null,
      studyProgram = null,
      points = 0,
      avatar = null,
      universityId = null
  )
}