package com.engenhariadesoftware.biblioteca.service;

import com.engenhariadesoftware.biblioteca.model.Emprestimo;
import com.engenhariadesoftware.biblioteca.model.Livro;
import com.engenhariadesoftware.biblioteca.model.Usuario;
import com.engenhariadesoftware.biblioteca.util.EmprestimoException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

@Slf4j
public class EmprestimoServiceTest {
    private EmprestimoService emprestimoService;
    private Livro livro1;
    private Livro livro2;
    private Livro livro3;
    private Emprestimo emprestimo;
    private Usuario usuario;

    @Before
    public void setUp() {
        livro1 = new Livro();
        livro1.setId(1L);
        livro1.setEmprestado(false);
        livro1.setReservado(false);
        livro1.setAutor("Machado de Assis");
        livro1.setTitulo("Dom Casmurro");
        livro1.setHistorico(new ArrayList<>());

        livro2 = new Livro();
        livro2.setId(2L);
        livro2.setEmprestado(false);
        livro2.setReservado(false);
        livro2.setAutor("William Shakespeare");
        livro2.setTitulo("Hamlet");
        livro2.setHistorico(new ArrayList<>());

        livro3 = new Livro();
        livro3.setId(3L);
        livro3.setEmprestado(false);
        livro3.setReservado(false);
        livro3.setAutor("Mario de Andrade");
        livro3.setTitulo("Macunaíma");
        livro3.setHistorico(new ArrayList<>());

        emprestimo = new Emprestimo(LocalDate.now());
        usuario = new Usuario();
        usuario.setNome("Jose Maria");
        usuario.setMatricula("ED7569");
        emprestimoService = new EmprestimoService(emprestimo);
    }

    @Test
    public void AlugaLivroNaoReservado() throws EmprestimoException {
        emprestimoService.realizaEmprestimo(livro1, usuario);
        assertTrue(livro1.isEmprestado());
    }

    @Test(expected = EmprestimoException.class)
    public void tentaAlugarLivroReservado() throws EmprestimoException {
        livro1.setReservado(true);
        emprestimoService.realizaEmprestimo(livro1, usuario);
        assertThatThrownBy(() -> emprestimoService.realizaEmprestimo(livro1, usuario))
                .isInstanceOf(EmprestimoException.class)
                .hasMessage("Emprestimo nao realizado. Este livro ja esta reservado ou emprestado");
    }

    @Test
    public void verificaDataPrevistaAposLocacao() throws EmprestimoException {
        emprestimoService.realizaEmprestimo(livro1, usuario);
        assertTrue(livro1.isEmprestado());
        assertEquals(emprestimo.getDataEmprestimo().plusDays(7), emprestimo.getDataPrevista());
    }

    @Test
    public void deveRealizarPrimeiroEmprestimo() throws EmprestimoException {
        emprestimoService.realizaEmprestimo(livro1, usuario);
        assertTrue(livro1.isEmprestado());
        assertEquals(1, usuario.getLivrosLocados());
    }

    @Test
    public void deveRealizarSegundoEmprestimo() throws EmprestimoException {
        emprestimo.setUsuario(usuario);
        emprestimo.setLivroId(livro1.getId());
        livro1.setEmprestado(true);
        livro1.adicionaAoHistorico(emprestimo);
        usuario.adicionaLivroLocado();

        emprestimoService.realizaEmprestimo(livro2, usuario);

        log.info(emprestimo.getUsuario().getNome());
        log.info(emprestimo.getLivroId().toString());
        assertTrue(livro2.isEmprestado());
        assertEquals(2, usuario.getLivrosLocados());
    }

    @Test(expected = EmprestimoException.class)
    public void naoDeveRealizarTerceiroEmprestimo() throws EmprestimoException {
        emprestimo.setUsuario(usuario);
        emprestimo.setLivroId(livro1.getId());
        livro1.adicionaAoHistorico(emprestimo);
        usuario.adicionaLivroLocado();
        emprestimo = new Emprestimo(LocalDate.now());
        emprestimo.setUsuario(usuario);
        emprestimo.setLivroId(livro2.getId());
        livro2.adicionaAoHistorico(emprestimo);
        usuario.adicionaLivroLocado();

        emprestimoService.realizaEmprestimo(livro3, usuario);

        log.info(emprestimo.getUsuario().getNome());
        log.info(emprestimo.getLivroId().toString());
        assertFalse(livro3.isEmprestado());
        assertEquals(2, usuario.getLivrosLocados());
        assertThatThrownBy(() -> emprestimoService.realizaEmprestimo(livro1, usuario))
                .isInstanceOf(EmprestimoException.class)
                .hasMessage("Emprestimo nao realizado. Usuario atingiu limite maximo de emprestimos");
    }

}