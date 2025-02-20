package dev.tgsi.attendance_registration_system.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tbl_project_mst")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proj_id")
    private Integer projectId;

    @Column(name = "proj_name")
    private String projectName;

    public ProjectModel(Integer projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<UserProjectModel> userProjects;
        
    @Override
    public String toString() {
        return "ProjectModel{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                '}';
    }
        
}
