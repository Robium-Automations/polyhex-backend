package com.robiumautomations.polyhex.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.robiumautomations.polyhex.commons.FileResponse;
import com.robiumautomations.polyhex.services.IStorageService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileController {

    private IStorageService IStorageService;

    // hey, Guga
    // split into two: /users/{user_id}/files and /groups/{group_id}/files
    @GetMapping("/allfiles")
    public String listAllFiles(Model model) {

        model.addAttribute("files", IStorageService.loadAll().map(
                path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(path.getFileName().toString())
                        .toUriString())
                .collect(Collectors.toList()));

        return "listAllFiles";
    }

    //@GetMapping("/files/{fileId}")
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource resource = IStorageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /*@PostMapping("/files")
    @ResponseBody
    public ResponseEntity<Material> uploadFile(@RequestParam("file") MultipartFile file) {
        AuthenticationUtils.INSTANCE.getCurrentUserId();
        final Material newMaterial = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

    //        return new FileResponse(name, uri, file.getContentType(), file.getSize());
    return ResponseEntity.created(newMaterial.getPath).body(newMaterial);
    } */
    /*@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"}) */
    @PostMapping("/upload-file")
    @ResponseBody
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String name = IStorageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return new FileResponse(name, uri, file.getContentType(), file.getSize());
    }

    @PostMapping("/upload-multiple-files")
    @ResponseBody
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
}

/**
 * TODO:
 * 1. rename endpoints according to REST convention:
 * 2.
 *
 *
 *
 *
 *
 *
 *
 *
 */
