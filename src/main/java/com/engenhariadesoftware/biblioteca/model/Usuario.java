package com.engenhariadesoftware.biblioteca.model;

import lombok.Data;

@Data
public class Usuario {
    private String nome;
    private String matricula;
    private int livrosLocados;

    public Usuario(){
        this.setLivrosLocados(0);
    }

    public void adicionaLivroLocado(){
        this.livrosLocados += 1;
    }

    public void devolveLivroLocado(){
        this.livrosLocados -= 1;
    }
}
