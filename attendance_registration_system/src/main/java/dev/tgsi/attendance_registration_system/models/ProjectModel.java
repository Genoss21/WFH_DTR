    package dev.tgsi.attendance_registration_system.models;
    import java.util.Set;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;


    @Entity
    @Table(name = "tbl_project_mst")
    @Data
    @NoArgsConstructor
    public class ProjectModel {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proj_id")
    private Integer projectId;

    @Column(name = "proj_name")
    private String projectName;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<UserProjectModel> userProjects;

    @Override
    public String toString() {
        return "ProjectModel{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                '}';
    }
        
    }
