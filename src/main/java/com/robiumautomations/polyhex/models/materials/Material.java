package com.robiumautomations.polyhex.models.materials;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "materials")
@Data
public class Material {

    @Id
    @GeneratedValue
    @Column(name = "material_id")
    private String materialId;
    @Column(name = "path")
    private String path;
    @Column(name = "name")
    private String name;
    @Column(name = "data_type")
    private String dataType;
    @Column(name = "author__id")
    private String authorId;
    @Column(name = "creating_date")
    private String creatingDate;

    public Material(){}
    public Material(String materialId, String path, String name, String DataType, String authorId, String creatingDate){
        this.materialId=materialId;
        this.path=path;
        this.name=name;
        this.dataType=dataType;
        this.authorId=authorId;
        this.creatingDate=creatingDate;
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
        public String name() {
            return name;
        }

        public void setName(String name) {

            this.name = name;
        }
        public String getDatatype() {
            return dataType;
        }

        public void setDatatype(String dataType) {

            this.dataType = dataType;
        }
        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {

            this.authorId = authorId;
        }
        public String getCreatingDate() {
            return creatingDate;
        }

        public void setCreatingDate(String creatingDate) {

            this.creatingDate = creatingDate;
        }

}


  // mandatory empty constructor



