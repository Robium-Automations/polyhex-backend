package com.robiumautomations.polyhex.models.universityentities

import lombok.Data
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "faculties")
@Data
class Faculty(
    @Id
    @Column(name = "faculty_id")
    val facultyId: String? = UUID.randomUUID().toString(),
    @Column(name = "faculty_name")
    val facultyName: String?,
    @Column(name = "university_id")
    val universityId: String?
) {

  constructor() : this(facultyName = null, universityId = null)
}