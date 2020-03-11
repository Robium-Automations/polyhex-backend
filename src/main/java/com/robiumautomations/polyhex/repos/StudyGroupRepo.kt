package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.StudyGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StudyGroupRepo : JpaRepository<StudyGroup, String> {

  @Query(
      value = "SELECT 1 FROM study_groups SG " +
          "WHERE SG.study_group_name = :groupName AND SG.subject_id = :subjectId AND SG.semester_id = :semesterId ;",
      nativeQuery = true
  )
  fun checkIfGroupNameIsAvailable(groupName: String, subjectId: String, semesterId: String): List<Int>
}