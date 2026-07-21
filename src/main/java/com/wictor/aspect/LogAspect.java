package com.wictor.aspect;

import com.wictor.annotation.Auditar;
import com.wictor.audit.AuditoriaContext;
import com.wictor.audit.AuditoriaInfo;
import com.wictor.audit.ComparadorAuditoria;
import com.wictor.dto.log.LogDto;
import com.wictor.model.User;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final LogService logService;
    private final HttpServletRequest request;
    private final ComparadorAuditoria comparadorAuditoria;

    @Around("@annotation(auditar)")
    public Object registrarLog(ProceedingJoinPoint joinPoint, Auditar auditar) throws Throwable {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User usuario = null;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {

            usuario = customUserDetails.getUser();
        }

        try {

            Object retorno = joinPoint.proceed();

            AuditoriaInfo info = AuditoriaContext.get();

            String descricao = gerarDescricao(info, auditar);

            logService.registrar(
                    new LogDto(
                            usuario,
                            auditar.acao(),
                            obterEntidade(info, joinPoint),
                            info != null ? info.getEntidadeId() : null,
                            descricao,
                            true
                    )
            );

            return retorno;

        } catch (Exception e) {

            AuditoriaInfo info = AuditoriaContext.get();

            logService.registrar(
                    new LogDto(
                            usuario,
                            auditar.acao(),
                            obterEntidade(info, joinPoint),
                            info != null ? info.getEntidadeId() : null,
                            e.getMessage(),
                            false
                    )
            );

            throw e;

        } finally {

            AuditoriaContext.limpar();
        }
    }

    private String gerarDescricao(AuditoriaInfo info, Auditar auditar) {

        if (info != null) {

            if (info.getDescricao() != null) {

                return info.getDescricao();
            }

            return comparadorAuditoria.comparar(info.getAntes(), info.getDepois());
        }

        if (!auditar.descricao().isBlank()) {

            return auditar.descricao();
        }

        return "Operação realizada.";
    }

    private String obterEntidade(AuditoriaInfo info, ProceedingJoinPoint joinPoint) {

        if (info != null && info.getEntidade() != null) {

            return info.getEntidade();
        }

        String[] partes = request.getRequestURI().split("/");

        if (partes.length >= 2) {
            String entidade = partes[1];

            if (entidade.endsWith("s")) {
                entidade = entidade.substring(0, entidade.length() - 1);
            }

            return Character.toUpperCase(entidade.charAt(0)) + entidade.substring(1);
        }

        return joinPoint.getTarget()
                .getClass()
                .getSimpleName()
                .replace("Service", "");
    }
}