package com.iteesoft.drone.model;

import com.iteesoft.drone.enums.Model;
import com.iteesoft.drone.enums.State;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Drone extends Base {

    @Size(min = 16, max = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private Model model;

    @Max(500)
    private Integer weightLimit;

    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Medication> items = new ArrayList<>();
}
