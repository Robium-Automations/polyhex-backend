package com.robiumautomations.polyhex.models.universityentities

import lombok.Data
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "subjects")
@Data
class Subject(
    @Id
    @Column(name = "subject_id")
    val subjectId: String? = UUID.randomUUID().toString(),
    @Column(name = "subject_name")
    val subjectName: String?,
    @Column(name = "subject_description")
    val subjectDescription: String?,
    @Column(name = "faculty_id")
    val facultyId: String?
) {

  constructor() : this(subjectName = null, subjectDescription = null, facultyId = null)
}