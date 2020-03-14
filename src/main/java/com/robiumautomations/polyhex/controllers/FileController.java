package com.robiumautomations.polyhex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

  // 1. uploadFile
  // 2. uploadFile (for groups)
  // 3. getUserFiles
  // 4. getGroupFiles
  // 5. downloadFile

  @PostMapping("/files")
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
}
