package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.models.universityentities.Faculty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FacultyRepo : JpaRepository<Faculty, String> {

  @Query(
      value = "SELECT * FROM faculties F WHERE F.university_id = (SELECT U.university_id FROM users U WHERE U.user_id = :userId ) " +
          "ORDER BY F.faculty_name LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getByUniversityId(
      userId: UserId,
      offset: Int = 0,
      limit: Int = 10
  ): List<Faculty>

  @Query(
      value = "SELECT * FROM faculties F WHERE F.university_id = (SELECT U.university_id FROM users U WHERE U.user_id = :userId ) " +
          " AND F.faculty_name ILIKE :facultyNameQuery ORDER BY F.faculty_name LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getByUniversityId(
      userId: UserId,
      facultyNameQuery: String,
      offset: Int = 0,
      limit: Int = 10
  ): List<Faculty>

  @Query(
      value = "SELECT 1 FROM faculties F WHERE F.university_id = :universityId AND F.faculty_name = :facultyName ;",
      nativeQuery = true
  )
  fun checkIfFacultyNameAvailable(universityId: String, facultyName: String): List<Int>
}