package com.robiumautomations.polyhex.repos;

import com.robiumautomations.polyhex.models.materials.SharedMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareMaterialRepo extends JpaRepository<SharedMaterial, String> {

  @Modifying
  @Query(
      value = "DELETE FROM shared_materials WHERE material_id = :fileId ;",
      nativeQuery = true
  )
  void deleteByMaterialId(@Param("fileId") String fileId);
}
