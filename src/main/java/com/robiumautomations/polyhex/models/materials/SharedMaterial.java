package com.robiumautomations.polyhex.models.materials;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shared_materials")
@Data
public class SharedMaterial {
    @Id
    @Column(name = "material_id")
    private String materialId;
    @Column(name = "group_id")
    private String groupId;

    public SharedMaterial(String materialId, String groupId) {
        this.materialId=materialId;
        this.groupId=groupId;
    }
        public String getMaterialId () {
            return materialId;
        }

        public void setMaterialId (String materialId){

            this.materialId = materialId;
        }
        public String getGroupId () {
            return groupId;
        }

        public void setGroupId (String groupId){

            this.groupId = groupId;
        }

}

