package com.wictor.scheduler;

import com.wictor.model.Aluno;
import com.wictor.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanoScheduler {

    private final AlunoRepository alunoRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void verificarPlanosVencidos() {

        List<Aluno> alunos = alunoRepository.findAll();

        for (Aluno aluno : alunos) {

            if (aluno.getVencimento() != null && aluno.getVencimento().isBefore(LocalDate.now()) && !aluno.isVencido()) {

                aluno.setVencido(true);
                alunoRepository.save(aluno);
            }
        }
    }
}