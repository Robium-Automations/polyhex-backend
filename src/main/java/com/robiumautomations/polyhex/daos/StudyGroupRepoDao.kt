package com.robiumautomations.polyhex.daos

import com.robiumautomations.polyhex.models.StudyGroup
import com.robiumautomations.polyhex.models.UserId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.*

@Component
open class StudyGroupRepoDao @Autowired constructor(
    @PersistenceContext private val entityManager: EntityManager
) {

  @JvmOverloads
  fun getGroups(
      userId: UserId,
      facultyId: String? = null,
      subjectId: String? = null,
      semesterId: String? = null,
      groupName: String? = null,
      subjectName: String? = null,
      joined: Boolean = false,
      offset: Int = 0,
      limit: Int = 10
  ): List<StudyGroup> {
    var queryString = "SELECT SG.* FROM study_groups SG " +
        "JOIN subjects S ON SG.subject_id = S.subject_id " +
        "JOIN faculties F ON S.faculty_id = F.faculty_id " +
        "WHERE F.university_id = (SELECT US.university_id FROM users US WHERE US.user_id = '$userId' ) "
    facultyId?.let {
      queryString += "AND F.faculty_id = '$it' "
    }

    subjectId?.let {
      queryString += "AND S.subject_id = '$it' "
    }

    semesterId?.let {
      queryString += "AND SG.semester_id = '$it' "
    }

    groupName?.let {
      queryString += "AND SG.study_group_name ILIKE '%$it%' "
    }

    subjectName?.let {
      queryString += "AND S.subject_name ILIKE '%$it%' "
    }

    if (joined) {
      queryString += "AND SG.study_group_id in (SELECT DISTINCT(UG.study_group_id) FROM users_groups UG WHERE UG.user_id = '$userId' ) "
    }

    queryString += "ORDER BY SG.study_group_name LIMIT $limit OFFSET $offset ;"

    val query = entityManager.createNativeQuery(queryString)

    val resultList = query.resultList
    return resultList.toGroupList()
  }

  private fun List<*>.toGroupList(): List<StudyGroup> {
    val result = mutableListOf<StudyGroup>()
    for (obj in this) {
      if (obj != null && obj is Array<*>) {
        result.add(StudyGroup(obj[0] as String?, obj[1] as String?, obj[2] as String?, obj[3] as String?))
      }
    }
    return result
  }
}