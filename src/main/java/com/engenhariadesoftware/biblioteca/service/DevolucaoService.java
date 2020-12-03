package com.engenhariadesoftware.biblioteca.service;

import com.engenhariadesoftware.biblioteca.model.Emprestimo;
import com.engenhariadesoftware.biblioteca.model.Livro;
import com.engenhariadesoftware.biblioteca.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DevolucaoService {
    Emprestimo emprestimo;

    public DevolucaoService(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public void realizaDevolucao(Livro livro, Usuario usuario) {
        LocalDate dataDevolucao = emprestimo.getDataDevolucao();
        LocalDate dataPrevista = emprestimo.getDataPrevista();

        if (dataDevolucao.isAfter(dataPrevista)) {
            emprestimo.setValor(calculaValorDaMulta());
        } else {
            emprestimo.setValor(BigDecimal.valueOf(5));
        }

        livro.setEmprestado(false);
        usuario.devolveLivroLocado();

    }

    public BigDecimal calculaValorDaMulta() {
        int diasDeAtraso = calculaDiasDeAtraso();
        BigDecimal precoBase = BigDecimal.valueOf(5);
        BigDecimal multa = BigDecimal.valueOf(0.40 * diasDeAtraso);
        BigDecimal valorMaximo = precoBase.multiply(BigDecimal.valueOf(0.6));

        if (precoBase.add(multa).compareTo(valorMaximo) < 0) {
            return precoBase.add(multa);
        } else {
            return precoBase.add(precoBase.multiply(BigDecimal.valueOf(0.6)));
        }
    }

    public int calculaDiasDeAtraso() {
        LocalDate dataDevolucao = emprestimo.getDataDevolucao();
        LocalDate dataPrevista = emprestimo.getDataPrevista();
        Long diasDeAtraso = ChronoUnit.DAYS.between(dataDevolucao, dataPrevista);
        return diasDeAtraso.intValue();
    }

}
