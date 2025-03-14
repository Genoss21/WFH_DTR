package dev.tgsi.attendance_registration_system.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_position_mst")
@Data
@NoArgsConstructor
public class PositionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Integer positionId;

    @Column(name = "position_name")
    private String positionName;

    @Column(name = "position_sh_name")
    private String positionShName;

    public PositionModel(String positionName, String positionShName) {
        this.positionName = positionName;
        this.positionShName = positionShName;
    }

    @Override
    public String toString() {
        return "PositionModel{" +
                "positionId=" + positionId +
                ", positionName='" + positionName + '\'' +
                ", positionShName='" + positionShName + '\'' +
                '}';
    }
}

