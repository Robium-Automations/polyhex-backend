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
          "WHERE M.author_id = :userId AND M.name LIKE :fileNameQuery ORDER BY M.creating_date ;",
      nativeQuery = true
  )
  fun getUserFiles(userId: String, fileNameQuery: String): List<Material>

}