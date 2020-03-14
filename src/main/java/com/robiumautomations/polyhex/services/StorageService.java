package com.robiumautomations.polyhex.services;

import com.robiumautomations.polyhex.enums.DataType;
import com.robiumautomations.polyhex.models.materials.Material;
import com.robiumautomations.polyhex.storage.FileNotFoundException;
import com.robiumautomations.polyhex.storage.StorageException;
import com.robiumautomations.polyhex.storage.StorageProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StorageService {

  private final Path rootLocation;

  @Autowired
  public StorageService(StorageProperties properties) {
    rootLocation = Paths.get(properties.getLocation());
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage location", e);
    }
  }

    public Material store(final MultipartFile file, final String creatorId) throws Exception {
    if (file == null) {
      throw new Exception("File is null.");
    }
    if (creatorId == null) {
      throw new Exception("CreatorId cannot be null.");
    }

    String filename = StringUtils.cleanPath(file.getOriginalFilename());

    final Material newMaterial =
        new Material(
            filename,
            DataType.Companion.resolveDataType(filename),
            creatorId);

    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + filename);
      }
      if (filename.contains("..")) {
        // This is a security check
        throw new StorageException(
            "Cannot store file with relative path outside current directory " + filename);
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(
            inputStream,
            rootLocation.resolve(newMaterial.getMaterialId()),
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + filename, e);
    }

    return newMaterial;
  }

  public Stream<Path> loadAll() {
    try {
      return Files.walk(rootLocation, 1)
          .filter(path -> !path.equals(rootLocation))
          .map(rootLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }
  }

  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new FileNotFoundException("Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new FileNotFoundException("Could not read file: " + filename, e);
    }
  }

  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }
}
