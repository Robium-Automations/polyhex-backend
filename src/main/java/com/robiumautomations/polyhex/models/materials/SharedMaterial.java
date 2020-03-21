package com.robiumautomations.polyhex.models.materials;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "shared_materials")
@Data
public class SharedMaterial {

  @Id
  @Column(name = "shared_material_id")
  private String sharedMaterialId;

  @Column(name = "material_id")
  private String materialId;

  @Column(name = "group_id")
  private String groupId;

  public SharedMaterial() {}

  public SharedMaterial(String materialId, String groupId) {
    sharedMaterialId = UUID.randomUUID().toString();
    this.materialId = materialId;
    this.groupId = groupId;
  }

  public String getSharedMaterialId() {
    return sharedMaterialId;
  }

  public void setSharedMaterialId(String sharedMaterialId) {
    this.sharedMaterialId = sharedMaterialId;
  }

  public String getMaterialId() {
    return materialId;
  }

  public void setMaterialId(String materialId) {
    this.materialId = materialId;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }
}
