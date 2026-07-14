package com.wictor.service;

import com.wictor.dto.log.LogDto;
import com.wictor.model.Log;
import com.wictor.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final HttpServletRequest request;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(LogDto dto) {

        Log log = Log.builder()
                .usuario(dto.usuario())
                .acao(dto.acao())
                .entidade(dto.entidade())
                .entidadeId(dto.entidadeId() != null ? dto.entidadeId().toString() : null)
                .descricao(dto.descricao())
                .sucesso(dto.sucesso())
                .metodo(request.getMethod())
                .url(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .data(LocalDateTime.now())
                .build();

        logRepository.save(log);
    }
}
