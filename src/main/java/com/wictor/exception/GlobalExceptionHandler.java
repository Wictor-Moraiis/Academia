package com.wictor.exception;

import com.wictor.dto.log.LogDto;
import com.wictor.enums.AcaoLog;
import com.wictor.model.User;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogService logService;

    @ExceptionHandler(RegraException.class)
    public ResponseEntity<?> handleRegra(RegraException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(403).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNaoEncontrado(NotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<?> handleConflito(ConflitoException ex) {
        return ResponseEntity.status(409).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(SizeException.class)
    public ResponseEntity<?> handleSize(SizeException ex){
        return ResponseEntity.status(413).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<?> handleInvalidImage(InvalidImageException ex) {
        return ResponseEntity.status(422).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<?> handleInternalError(InternalErrorException ex){
        return ResponseEntity.status(500).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        registrarErro(request, ex.getMessage());

        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e -> erros.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {

        registrarErro(request, ex.getMessage());

        return ResponseEntity.badRequest().body(Map.of("erro", "JSON inválido."));
    }

    private String extrairEntidade(String uri) {

        String[] partes = uri.split("/");

        if (partes.length >= 2) {
            return partes[1].toUpperCase();
        }

        return "DESCONHECIDO";
    }

    private void registrarErro(HttpServletRequest request, String descricao) {

        logService.registrar(new LogDto(
                obterUsuarioLogado(),
                obterAcao(request),
                extrairEntidade(request.getRequestURI()),
                null,
                descricao,
                false
        ));
    }

    private AcaoLog obterAcao(HttpServletRequest request) {

        return switch (request.getMethod()) {
            case "POST" -> AcaoLog.CADASTRO;
            case "PUT", "PATCH" -> AcaoLog.ALTERACAO;
            case "DELETE" -> AcaoLog.EXCLUSAO;
            case "GET" -> AcaoLog.CONSULTA;
            default -> AcaoLog.OUTRO;
        };
    }

    private User obterUsuarioLogado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {

            return customUserDetails.getUser();
        }

        return null;
    }


}
