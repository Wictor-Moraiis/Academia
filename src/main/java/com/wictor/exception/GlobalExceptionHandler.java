package com.wictor.exception;

import com.wictor.dto.log.LogDto;
import com.wictor.enums.AcaoLog;
import com.wictor.model.User;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogService logService;

    @ExceptionHandler(RegraException.class)
    public ResponseEntity<?> handleRegra(RegraException ex) {

        return erro(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {

        return erro(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {

        return erro(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNaoEncontrado(NotFoundException ex) {

        return erro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<?> handleConflito(ConflitoException ex) {

        return erro(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(SizeException.class)
    public ResponseEntity<?> handleSize(SizeException ex) {

        return erro(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage());
    }

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<?> handleInvalidImage(InvalidImageException ex) {

        return erro(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<?> handleInternalError(InternalErrorException ex) {

        return erro(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        registrarErro(request, ex.getMessage());

        Map<String, String> erros = new HashMap<>();

        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erros.put(erro.getField(), erro.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {

        registrarErro(request, ex.getMessage());

        return erro(HttpStatus.BAD_REQUEST, "JSON inválido.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {

        registrarErro(request, ex.getMessage());

        return erro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.");
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {

        return ResponseEntity.status(status).body(Map.of("erro", mensagem));
    }

    private String extrairEntidade(String uri) {

        return Arrays.stream(uri.split("/"))
                .filter(parte -> !parte.isBlank())
                .findFirst()
                .orElse("DESCONHECIDO")
                .toUpperCase();
    }

    private void registrarErro(HttpServletRequest request, String descricao){

        logService.registrar(new LogDto(
                obterUsuarioLogado(),
                obterAcao(request),
                extrairEntidade(request.getRequestURI()),
                null,
                descricao,
                false
        ));
    }

    private AcaoLog obterAcao(HttpServletRequest request){

        return switch (request.getMethod()) {
            case "POST" -> AcaoLog.CADASTRO;
            case "PUT", "PATCH" -> AcaoLog.ALTERACAO;
            case "DELETE" -> AcaoLog.EXCLUSAO;
            case "GET" -> AcaoLog.CONSULTA;
            default -> AcaoLog.OUTRO;
        };
    }

    private User obterUsuarioLogado(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {

            return customUserDetails.getUser();
        }
        return null;
    }
}