package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.universityentities.Faculty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FacultyRepo : JpaRepository<Faculty, String> {

  @Query(value = "SELECT * FROM faculties F WHERE F.university_id = :universityId ;", nativeQuery = true)
  fun getByUniversityId(universityId: String): List<Faculty>
}