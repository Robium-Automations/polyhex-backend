package com.robiumautomations.polyhex.models.universityentities

import lombok.Data
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "universities")
@Data
class University(
    @Id
    @Column(name = "university_id")
    val universityId: String? = UUID.randomUUID().toString(),
    @Column(name = "university_name")
    val universityName: String?
) {

  constructor() : this(universityName = null)
}