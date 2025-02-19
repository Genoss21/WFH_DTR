package dev.tgsi.attendance_registration_system.models;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;


    @Entity
    @Table(name = "tbl_user_project")
    @Data
    @NoArgsConstructor

    public class UserProjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upid")
    private Integer upid;

    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "proj_id")
    private ProjectModel project;
    
}
