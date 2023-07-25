package br.estudos.minhasfinancas.controller;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.estudos.minhasfinancas.model.dto.UsuarioRecordDTO;
import br.estudos.minhasfinancas.model.entity.Usuario;
import br.estudos.minhasfinancas.service.LancamentoService;
import br.estudos.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody UsuarioRecordDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        try {
			usuario = usuarioService.salvarUsuario(usuario);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioRecordDTO usuarioDTO) {
		Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        
        
        try {
        	usuario = usuarioService.autenticar(usuario.getEmail(), usuario.getSenha());
			return new ResponseEntity(usuario, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
        
        
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity<Object> obterSaldo(@PathVariable("id") Long id) {
		try {
			BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
			return new ResponseEntity(saldo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
