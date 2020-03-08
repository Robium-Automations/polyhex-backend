package com.robiumautomations.polyhex.models

import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "study_groups")
@Data
class StudyGroup(
    @Id
    @Column(name = "study_group_id")
    val studyGroupId: String?,
    @Column(name = "study_group_name")
    val studyGroupName: String?,
    @Column(name = "subject_id")
    val subjectId: String?,
    @Column(name = "semester_id")
    val semesterId: String?
) {

  constructor() : this(null, null, null, null)
}