package com.robiumautomations.polyhex.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.naming.NoPermissionException;

import com.robiumautomations.polyhex.models.dtos.files.CreateFileResponseDto;
import com.robiumautomations.polyhex.models.materials.Material;
import com.robiumautomations.polyhex.models.materials.SharedMaterial;
import com.robiumautomations.polyhex.repos.MaterialRepo;
import com.robiumautomations.polyhex.repos.ShareMaterialRepo;
import com.robiumautomations.polyhex.storage.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MaterialService {

  @Autowired private StorageService storageService;

  @Autowired private MaterialRepo materialRepo;
  @Autowired private ShareMaterialRepo shareMaterialRepo;

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

  public Material createMaterialForGroup(
      final MultipartFile file, final String currentUserId, final String groupId) throws Exception {
    // check if user is member of the group
    final Material material = createMaterial(file, currentUserId);
    final SharedMaterial newSharedMaterial = new SharedMaterial(material.getMaterialId(), groupId);
    shareMaterialRepo.save(newSharedMaterial);
    return material;
  }

  public List<CreateFileResponseDto> getGroupFiles(
      String groupId, String userId, final String fileNameQuery) {
    if (fileNameQuery == null) {
      return fromMaterialsListToMaterialsDtoList(
          materialRepo.getGroupFilesForUser(groupId, userId));
    } else {
      return fromMaterialsListToMaterialsDtoList(
          materialRepo.getGroupFilesForUser(userId, groupId, fileNameQuery));
    }
  }

  public Resource getFile(String fileId, String currentUserId) {
    try {
      final Path file = storageService.load(fileId);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new FileNotFoundException("Could not read fileId: " + fileId);
      }
    } catch (MalformedURLException e) {
      throw new FileNotFoundException("Could not read fileId: " + fileId, e);
    }
  }

  @Transactional
  public void deleteFile(String fileId, String userId) throws NoPermissionException {
    Optional<Material> materialOptional = materialRepo.findById(fileId);
    if (!materialOptional.isPresent()) {
      throw new FileNotFoundException("There is now file with id: " + fileId);
    }
    Material material = materialOptional.get();
    if (!material.getAuthorId().equals(userId)) {
      throw new NoPermissionException("User with id: " + userId + " cannot delete file with id: " + fileId);
    }
    Path fileToDeletePath = Paths.get(material.getPath());
    try {
      Files.delete(fileToDeletePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    shareMaterialRepo.deleteByMaterialId(fileId);
    materialRepo.deleteById(fileId);
  }
}
