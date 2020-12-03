package com.engenhariadesoftware.biblioteca.model;

import lombok.Data;

import java.util.List;

@Data
public class Livro {
    private Long id;
    private String autor;
    private String titulo;
    private boolean isEmprestado;
    private boolean isReservado;
    private List<Emprestimo> historico;

    public void adicionaAoHistorico(Emprestimo emprestimo){
        historico.add(emprestimo);
    }
}
