package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.materials.Material
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaterialRepo : JpaRepository<Material, String>