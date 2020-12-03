package com.engenhariadesoftware.biblioteca.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Emprestimo {
    private Long id;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevista;
    private LocalDate dataDevolucao;
    private Long livroId;
    private BigDecimal valor;

    public Emprestimo(LocalDate dataEmprestimo){
        this.setDataEmprestimo(dataEmprestimo);
        this.setDataPrevista(dataEmprestimo.plusDays(7));
        this.setValor(BigDecimal.valueOf(5));
    }
}
