package br.estudos.minhasfinancas.service;

import java.math.BigDecimal;

import br.estudos.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	public Usuario autenticar(String email, String senha);
	
	public Usuario salvarUsuario(Usuario usuario);
	
	public boolean validarEmail(String email);
	
	public Usuario obterPorId(Long id);
	
}
