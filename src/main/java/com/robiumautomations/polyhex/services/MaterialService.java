package com.robiumautomations.polyhex.services;

import java.util.LinkedList;
import java.util.List;

import com.robiumautomations.polyhex.models.dtos.files.CreateFileResponseDto;
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

  public List<CreateFileResponseDto> getUserFiles(final String fileNameQuery, final String userId) {
    if (fileNameQuery == null) {
      return fromMaterialsListToMaterialsDtoList(materialRepo.getUserFiles(userId));
    } else {
      return fromMaterialsListToMaterialsDtoList(
          materialRepo.getUserFiles(userId, "%" + fileNameQuery + "%"));
    }
  }

  private List<CreateFileResponseDto> fromMaterialsListToMaterialsDtoList(
      final List<Material> materials) {
    LinkedList<CreateFileResponseDto> result = new LinkedList<>();

    for (Material material : materials) {
      result.add(
          new CreateFileResponseDto(
              material.getMaterialId(),
              material.getName(),
              material.getDataType(),
              null,
              material.getAuthorId(),
              material.getCreatingDate().getTime()));
    }
    return result;
  }
}
