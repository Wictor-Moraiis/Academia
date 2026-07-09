package com.wictor.audit;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuditoriaInfo {

    private Object antes;

    private Object depois;

    private String descricao;

    private String entidade;

    private Integer entidadeId;

}