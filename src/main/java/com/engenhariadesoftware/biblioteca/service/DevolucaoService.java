package com.engenhariadesoftware.biblioteca.service;

import com.engenhariadesoftware.biblioteca.model.Emprestimo;
import com.engenhariadesoftware.biblioteca.model.Livro;
import com.engenhariadesoftware.biblioteca.model.Usuario;
import com.engenhariadesoftware.biblioteca.util.EmprestimoException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
public class DevolucaoService {
    Emprestimo emprestimo;

    public DevolucaoService(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public boolean realizaDevolucao(Livro livro, Usuario usuario) throws EmprestimoException {
        LocalDate dataDevolucao = emprestimo.getDataDevolucao();
        LocalDate dataPrevista = emprestimo.getDataPrevista();

        if (livro.isEmprestado()) {
            if (dataDevolucao.isAfter(dataPrevista)) {
                emprestimo.setValor(calculaValorComMulta());
            } else {
                emprestimo.setValor(BigDecimal.valueOf(5));
            }

            livro.setEmprestado(false);

            if (usuario.devolveLivroLocado()) {
                return true;
            } else {
                String erro = "Usuario não possui livros locados.";
                throw new EmprestimoException(erro);
            }
        }
        return false;
    }

    public BigDecimal calculaValorComMulta() {
        int diasDeAtraso = calculaDiasDeAtraso();
        log.info("dias atraso" + String.valueOf(diasDeAtraso));
        BigDecimal precoBase = BigDecimal.valueOf(5);
        BigDecimal multa = BigDecimal.valueOf(0.40 * diasDeAtraso);
        BigDecimal valorMaximo = precoBase.multiply(BigDecimal.valueOf(0.6));

        if (multa.compareTo(valorMaximo) <= 0) {
            return precoBase.add(multa);
        } else {
            return precoBase.add(valorMaximo);
        }
    }

    public int calculaDiasDeAtraso() {
        LocalDate dataDevolucao = emprestimo.getDataDevolucao();
        LocalDate dataPrevista = emprestimo.getDataPrevista();
        long diasDeAtraso = ChronoUnit.DAYS.between(dataPrevista, dataDevolucao);
        return (int) diasDeAtraso;
    }

}
