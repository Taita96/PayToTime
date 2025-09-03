package gm.PaynTime.presentacion;

import org.springframework.context.ApplicationContext;

public class ApplicationContextHolder {
    private static ApplicationContext context;

    private ApplicationContextHolder() {
        // Evitar instanciación
    }

    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            throw new IllegalStateException("El ApplicationContext aún no ha sido inicializado");
        }
        return context;
    }
}
