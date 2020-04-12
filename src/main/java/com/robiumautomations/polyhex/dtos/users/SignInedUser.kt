package com.robiumautomations.polyhex.dtos.users

import com.robiumautomations.polyhex.models.User

class SignInedUser(
    var token: String,
    user: User
) : User(
    userId = user.userId,
    username = user.username,
    firstName = user.firstName,
    lastName = user.lastName,
    birthday = user.birthday,
    studyProgram = user.studyProgram,
    points = user.points,
    avatar = user.avatar,
    universityId = user.universityId
)