package br.estudos.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.estudos.minhasfinancas.exception.ErroAutenticacaoException;
import br.estudos.minhasfinancas.exception.RegraNegocioException;
import br.estudos.minhasfinancas.model.entity.Usuario;
import br.estudos.minhasfinancas.model.repository.UsuarioRepository;
import br.estudos.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}

	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> resultado = repository.findByEmailAndSenha(email, senha);
		
		if (!resultado.isPresent()) {
			throw new ErroAutenticacaoException();
		} 
		
		return resultado.get();
		
	}

	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	public boolean validarEmail(String email) {
		boolean jaExiste = repository.existsByEmail(email);
		if (jaExiste) 
			throw new RegraNegocioException("Usuário duplicado");
		return jaExiste;
	}

	public Usuario obterPorId(Long id) {
		
		Optional<Usuario> resultado = repository.findById(id);
		
		
		if (resultado.isEmpty())
			throw new RegraNegocioException("Usuário não encontrado");
		
		return resultado.get();
	}
 
	
	
}
