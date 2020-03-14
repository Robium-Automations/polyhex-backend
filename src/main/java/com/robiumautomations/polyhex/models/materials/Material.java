package com.robiumautomations.polyhex.models.materials;

import static com.robiumautomations.polyhex.utils.Constants.MATERIALS_DIRECTORY;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.robiumautomations.polyhex.enums.DataType;

import lombok.Data;

@Entity
@Table(name = "materials")
@Data
public class Material {

  @Id
  @Column(name = "material_id")
  private String materialId;

  @Column(name = "path")
  private String path;

  @Column(name = "name")
  private String name;

  @Column(name = "data_type")
  @Enumerated(EnumType.STRING)
  private DataType dataType;

  @Column(name = "author_id")
  private String authorId;

  @Column(name = "creating_date")
  private Date creatingDate;

  public Material() {}

  public Material(String name, DataType dataType, String authorId) {
    final String id = UUID.randomUUID().toString();
    this.materialId = id;
    this.path = MATERIALS_DIRECTORY + id;
    this.name = name;
    this.dataType = dataType;
    this.authorId = authorId;
    this.creatingDate = new Date();
  }

  public String getMaterialId() {
    return materialId;
  }

  public void setMaterialId(String materialId) {
    this.materialId = materialId;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  public Date getCreatingDate() {
    return creatingDate;
  }

  public void setCreatingDate(Date creatingDate) {
    this.creatingDate = creatingDate;
  }
}
