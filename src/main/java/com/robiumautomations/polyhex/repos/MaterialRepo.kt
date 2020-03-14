package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.materials.Material
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MaterialRepo : JpaRepository<Material, String> {

  @Query(
      value = "SELECT * FROM materials M WHERE M.author_id = :userId ORDER BY M.creating_date ;",
      nativeQuery = true
  )
  fun getUserFiles(userId: String): List<Material>

  @Query(
      value = "SELECT * FROM materials M " +
          "WHERE M.author_id = :userId AND M.name ILIKE :fileNameQuery ORDER BY M.creating_date ;",
      nativeQuery = true
  )
  fun getUserFiles(userId: String, fileNameQuery: String): List<Material>

  @Query(
      value = "select M.* from materials M join shared_materials SM on M.material_id = SM.material_id where SM.group_id = " +
          "(select UG.study_group_id from users_groups UG where UG.user_id = :userId and UG.study_group_id = :groupId ) ORDER BY M.creating_date ;",
      nativeQuery = true
  )
  fun getGroupFilesForUser(userId: String, groupId: String): List<Material>

  @Query(
      value = "select M.* from materials M join shared_materials SM on M.material_id = SM.material_id where M.name ilike :fileNameQuery and SM.group_id = " +
          "(select UG.study_group_id from users_groups UG where UG.user_id = :userId and UG.study_group_id = :groupId ) order by M.creating_date ;",
      nativeQuery = true
  )
  fun getGroupFilesForUser(userId: String, groupId: String, fileNameQuery: String): List<Material>
}