package com.engenhariadesoftware.biblioteca.service;

import com.engenhariadesoftware.biblioteca.model.Emprestimo;
import com.engenhariadesoftware.biblioteca.model.Livro;
import com.engenhariadesoftware.biblioteca.model.Usuario;
import com.engenhariadesoftware.biblioteca.util.EmprestimoException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DevolucaoServiceTest {
    private DevolucaoService devolucaoService;
    private Livro livro;
    private Emprestimo emprestimo;
    private Usuario usuario;

    @Before
    public void setUp() {
        livro = new Livro();
        livro.setId(1L);
        livro.setEmprestado(true);
        livro.setReservado(false);
        livro.setAutor("Machado de Assis");
        livro.setTitulo("Dom Casmurro");

        usuario = new Usuario();
        usuario.setNome("Jose Maria");
        usuario.setMatricula("ED7569");

        emprestimo = new Emprestimo(LocalDate.now().minusDays(6));
        emprestimo.setLivroId(livro.getId());
        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setUsuario(usuario);
        emprestimo.setId(2L);

        livro.setHistorico(Collections.singletonList(emprestimo));

        devolucaoService = new DevolucaoService(emprestimo);
    }

    @Test
//    @Ignore
    public void deveRealizarDevolucaoAntesDaDataPrevista() throws EmprestimoException {
        usuario.setLivrosLocados(1);
        boolean retorno = devolucaoService.realizaDevolucao(livro, usuario);
        assertEquals(BigDecimal.valueOf(5), emprestimo.getValor());
        assertFalse(livro.isEmprestado());
        assertEquals(0, usuario.getLivrosLocados());
        assertTrue(retorno);
    }

    @Test
    public void deveRealizarDevolucaoNaDataPrevista() throws EmprestimoException {
        usuario.setLivrosLocados(1);
        emprestimo.setDataDevolucao(emprestimo.getDataEmprestimo().plusDays(7));
        BigDecimal valorPago = BigDecimal.valueOf(5);
        boolean retorno = devolucaoService.realizaDevolucao(livro, usuario);
        assertEquals(valorPago, emprestimo.getValor());
        assertFalse(livro.isEmprestado());
        assertEquals(0, usuario.getLivrosLocados());
        assertTrue(retorno);
    }

    @Test
    public void devePagarMultaComUmDiaDeAtraso() throws EmprestimoException {
        usuario.setLivrosLocados(1);
        emprestimo.setDataDevolucao(emprestimo.getDataEmprestimo().plusDays(8));
        BigDecimal valorPago = BigDecimal.valueOf(5.40);
        boolean retorno = devolucaoService.realizaDevolucao(livro, usuario);
        assertEquals(valorPago, emprestimo.getValor());
        assertFalse(livro.isEmprestado());
        assertEquals(0, usuario.getLivrosLocados());
        assertTrue(retorno);
    }

    @Test
    public void naoDeveRealizarDevolucao30DiasAntes() throws EmprestimoException {
        usuario.setLivrosLocados(1);
        emprestimo.setDataDevolucao(emprestimo.getDataEmprestimo().plusDays(30));
        BigDecimal valorPago = BigDecimal.valueOf(8.0);
        boolean retorno = devolucaoService.realizaDevolucao(livro, usuario);
        assertEquals(valorPago, emprestimo.getValor());
        assertFalse(livro.isEmprestado());
        assertEquals(0, usuario.getLivrosLocados());
        assertTrue(retorno);
    }

}
