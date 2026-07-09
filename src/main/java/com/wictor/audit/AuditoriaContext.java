package com.wictor.audit;

public class AuditoriaContext {

    private static final ThreadLocal<AuditoriaInfo> CONTEXT = new ThreadLocal<>();

    private AuditoriaContext() {}

    public static void registrar(AuditoriaInfo info) {
        CONTEXT.set(info);
    }

    public static AuditoriaInfo get() {
        return CONTEXT.get();
    }

    public static void limpar() {
        CONTEXT.remove();
    }

}