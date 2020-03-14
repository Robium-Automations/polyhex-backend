package com.robiumautomations.polyhex.services;

import com.robiumautomations.polyhex.models.materials.Material;
import com.robiumautomations.polyhex.repos.MaterialRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MaterialService {

  @Autowired private StorageService storageService;

  @Autowired private MaterialRepo materialRepo;

  public Material createMaterial(final MultipartFile file, final String creatorId)
      throws Exception {
    final Material newMaterial = storageService.store(file, creatorId);
    materialRepo.save(newMaterial);
    return newMaterial;
  }
}
