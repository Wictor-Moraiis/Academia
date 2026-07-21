package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Treino_Exercicio")
public class TreinoExercicio {

    @EmbeddedId
    private TreinoExercicioId id;

    @ManyToOne
    @MapsId("treinoId")
    @JoinColumn(name = "Treino_id", nullable = false)
    private Treino treino;

    @ManyToOne
    @MapsId("exercId")
    @JoinColumn(name = "Exerc_id", nullable = false)
    private Exercicio exercicio;

    @Column(name = "ExercT_ordem", nullable = false)
    private Integer ordem;

    @Column(name = "ExercT_carga", nullable = false)
    private String carga;

    @Column(name = "ExercT_series", nullable = false)
    private String series;

    @Column(name = "ExercT_rep", nullable = false)
    private String rep;

    @Column(name = "ExercT_obs")
    private String obs;
}
