package gm.PaynTime.servicio;

import org.springframework.stereotype.Service;

import gm.PaynTime.modelo.Usuario;

@Service
public class SessionService {
    private Usuario usuarioLogueado;

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}

