package com.engenhariadesoftware.biblioteca.service;

import com.engenhariadesoftware.biblioteca.model.Emprestimo;
import com.engenhariadesoftware.biblioteca.model.Livro;
import com.engenhariadesoftware.biblioteca.model.Usuario;
import com.engenhariadesoftware.biblioteca.util.EmprestimoException;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {
    Emprestimo emprestimo;

    public EmprestimoService(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public void realizaEmprestimo(Livro livro, Usuario usuario) throws EmprestimoException {
        if (!livro.isReservado() && !livro.isEmprestado()) {
            if (usuario.getLivrosLocados() <= 1 && usuario.getLivrosLocados() >= 0) {
                this.emprestimo.setLivroId(livro.getId());
                this.emprestimo.setUsuario(usuario);
                livro.setEmprestado(true);
                livro.adicionaAoHistorico(this.emprestimo);
                usuario.adicionaLivroLocado();
            } else {
                livro.setEmprestado(false);
                String erro = "Emprestimo nao realizado. Usuario atingiu limite maximo de emprestimos";
                throw new EmprestimoException(erro);
            }
        } else {
            String erro = "Emprestimo nao realizado. Este livro ja esta reservado ou emprestado";
            throw new EmprestimoException(erro);
        }
    }
}
