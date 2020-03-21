package com.robiumautomations.polyhex.dtos.semesters

import java.util.*

class CreateSemesterRequestDto(
    val semesterName: String? = null,
    val semesterDescription: String? = null,
    val startDate: Date? = null,
    val endDate: Date? = null
)