package com.robiumautomations.polyhex.models.universityentities

import lombok.Data
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "semesters")
@Data
class Semester(
    @Id
    @Column(name = "semester_id")
    val semesterId: String? = UUID.randomUUID().toString(),
    @Column(name = "semester_name")
    val semesterName: String?,
    @Column(name = "semester_description")
    val semesterDescription: String?,
    @Column(name = "start_date")
    val startDate: Date?,
    @Column(name = "end_date")
    val endDate: Date?,
    @Column(name = "university_id")
    val universityId: String?
) {

  constructor() : this(null, null, null, null, null, null)
}