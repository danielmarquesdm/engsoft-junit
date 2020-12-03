package com.engenhariadesoftware.biblioteca.service;

import com.engenhariadesoftware.biblioteca.model.Emprestimo;
import com.engenhariadesoftware.biblioteca.model.Livro;
import com.engenhariadesoftware.biblioteca.model.Usuario;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DevolucaoServiceTest {
    private DevolucaoService devolucaoService;
    private Livro livro;
    private Emprestimo emprestimo;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        livro = new Livro();
        livro.setId(1L);
        livro.setEmprestado(true);
        livro.setReservado(false);
        livro.setAutor("Machado de Assis");
        livro.setTitulo("Dom Casmurro");
        livro.setHistorico(new ArrayList<>());

        emprestimo = new Emprestimo(LocalDate.now());
        usuario = new Usuario();
        usuario.setNome("Jose Maria");
        usuario.setMatricula("ED7569");
        devolucaoService = new DevolucaoService(emprestimo);
    }

    @Test
    public void deveRealizarDevolucaoAntesDaDataPrevista() {
        emprestimo.setDataEmprestimo(LocalDate.now().minusDays(6));
        devolucaoService.realizaDevolucao(livro, usuario);
        assertFalse(livro.isEmprestado());
        assertEquals(BigDecimal.valueOf(5), emprestimo.getValor());
    }

    @Test
    public void deveRealizarDevolucaoNaDataPrevista() {

    }

    @Test
    public void devePagarMultaComUmDiaDeAtraso() {

    }

    @Test
    public void naoDeveRealizarDevolucao30DiasAntes() {

    }

}
