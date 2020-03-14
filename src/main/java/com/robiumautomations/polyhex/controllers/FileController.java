package com.robiumautomations.polyhex.controllers;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.robiumautomations.polyhex.models.dtos.files.CreateFileResponseDto;
import com.robiumautomations.polyhex.models.materials.Material;
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils;
import com.robiumautomations.polyhex.services.MaterialService;

@Controller
public class FileController {

  @Autowired private MaterialService materialService;

  // 1. uploadFile DONE
  // 2. uploadFile (for groups) DONE
  // 3. getUserFiles DONE
  // 4. getGroupFiles DONE
  // 5. downloadFile

  @PostMapping(value = "/files", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      final Material material =
          materialService.createMaterial(file, AuthenticationUtils.INSTANCE.getCurrentUserId());
      return ResponseEntity.ok(
          new CreateFileResponseDto(
              material.getMaterialId(),
              material.getName(),
              material.getDataType(),
              file.getSize(),
              material.getAuthorId(),
              material.getCreatingDate().getTime()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().header("Message", e.getMessage()).build();
    }
  }

  @GetMapping("/files")
  @ResponseBody
  public ResponseEntity getUserFiles(
      @RequestParam(value = "fileName", required = false, defaultValue = "")
          final String fileName) {
    return ResponseEntity.ok(
        materialService.getUserFiles(
            fileName.isEmpty() ? null : fileName, AuthenticationUtils.INSTANCE.getCurrentUserId()));
  }

  @PostMapping(value = "/groups/{group_id}/files", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity uploadFileForGroup(
      @PathVariable("group_id") String groupId, @RequestParam("file") MultipartFile file) {
    try {
      final Material material =
          materialService.createMaterialForGroup(
              file, AuthenticationUtils.INSTANCE.getCurrentUserId(), groupId);
      return ResponseEntity.ok(
          new CreateFileResponseDto(
              material.getMaterialId(),
              material.getName(),
              material.getDataType(),
              file.getSize(),
              material.getAuthorId(),
              material.getCreatingDate().getTime()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().header("Message", e.getMessage()).build();
    }
  }

  @GetMapping(value = "/groups/{group_id}/files", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity getGroupFiles(
      @PathVariable("group_id") String groupId,
      @RequestParam(value = "fileName", required = false, defaultValue = "")
          final String fileName) {
    try {
      return ResponseEntity.ok(
          materialService.getGroupFiles(
              groupId,
              AuthenticationUtils.INSTANCE.getCurrentUserId(),
              fileName.isEmpty() ? null : fileName));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().header("Message", e.getMessage()).build();
    }
  }

  @GetMapping("/files/{fileId}")
  @ResponseBody
  public ResponseEntity downloadFile(@PathVariable("fileId") String fileId) {
    final Resource resource =
        materialService.getFile(fileId, AuthenticationUtils.INSTANCE.getCurrentUserId());

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
