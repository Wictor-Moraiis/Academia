package com.wictor.model;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TreinoExercicioId implements Serializable {

    private Integer treinoId;
    private Integer exercId;

    @Override
    public String toString() {
        return treinoId + "-" + exercId;
    }
}
