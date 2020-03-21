package com.robiumautomations.polyhex.repos;

import com.robiumautomations.polyhex.models.materials.SharedMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareMaterialRepo extends JpaRepository<SharedMaterial, String> {}
