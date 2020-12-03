package com.engenhariadesoftware.biblioteca.model;

import lombok.Data;

@Data
public class Usuario {
    private String nome;
    private String matricula;
    private int livrosLocados;

    public Usuario() {
        this.setLivrosLocados(0);
    }

    public boolean adicionaLivroLocado() {
        if (this.livrosLocados < 2) {
            this.livrosLocados += 1;
            return true;
        } else {
            return false;
        }
    }

    public boolean devolveLivroLocado() {
        if (this.livrosLocados > 0) {
            this.livrosLocados -= 1;
            return true;
        } else {
            return false;
        }
    }
}
