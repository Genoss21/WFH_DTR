package dev.tgsi.attendance_registration_system.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tbl_proj_manager")
@Data
@NoArgsConstructor

public class ProjectManagerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pmid")
    private Integer pMid;

    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "proj_id")
    private ProjectModel project;
    
}
