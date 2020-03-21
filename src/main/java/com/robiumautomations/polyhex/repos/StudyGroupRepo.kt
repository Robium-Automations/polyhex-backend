package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.StudyGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StudyGroupRepo : JpaRepository<StudyGroup, String> {

  @Query(
      value = """SELECT 1 FROM study_groups SG """ +
          """WHERE SG.study_group_name = :groupName AND SG.subject_id = :subjectId AND SG.semester_id = :semesterId ;""",
      nativeQuery = true
  )
  fun checkIfGroupNameIsAvailable(groupName: String, subjectId: String, semesterId: String): List<Int>

  /**
   * @return [StudyGroup] object and ensure [currentUserId] belongs to this university
   */
  @Query(
      value = """SELECT SG.* FROM study_groups SG JOIN semesters S ON SG.semester_id = S.semester_id """ +
          """WHERE SG.study_group_id = :groupId AND S.university_id = (SELECT U.university_id FROM users U WHERE U.user_id = :currentUserId );""",
      nativeQuery = true
  )
  fun findGroupByGroupIdAndUsersUniversity(groupId: String, currentUserId: String): StudyGroup?
}