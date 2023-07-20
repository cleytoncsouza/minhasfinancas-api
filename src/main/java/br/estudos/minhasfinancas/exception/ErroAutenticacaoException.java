package br.estudos.minhasfinancas.exception;

public class ErroAutenticacaoException extends RuntimeException {

	public ErroAutenticacaoException() {
		super("Dados inválidos");
	}
	
}
