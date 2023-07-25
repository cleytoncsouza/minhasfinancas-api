package br.estudos.minhasfinancas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.estudos.minhasfinancas.model.dto.LancamentoRecordDTO;
import br.estudos.minhasfinancas.model.entity.Lancamento;
import br.estudos.minhasfinancas.model.entity.Usuario;
import br.estudos.minhasfinancas.model.enums.StatusLancamento;
import br.estudos.minhasfinancas.model.enums.TipoLancamento;
import br.estudos.minhasfinancas.service.LancamentoService;
import br.estudos.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private Lancamento criarLancamentoFromDTO(LancamentoRecordDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDataCadastro(LocalDate.now());
		
		BeanUtils.copyProperties(dto, lancamento);
		
		lancamento.setStatus(StatusLancamento.valueOf(dto.status()));
		lancamento.setTipo(TipoLancamento.valueOf(dto.tipo()));
		
		Usuario usuario = usuarioService.obterPorId(dto.usuario());
		lancamento.setUsuario(usuario);
		
		return lancamento;
	}
	
	
	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody LancamentoRecordDTO dto) {
		try {
			Lancamento lancamento = criarLancamentoFromDTO(dto);
			
			lancamento = lancamentoService.salvar(lancamento);
			
			return new ResponseEntity<Object>(lancamento, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("{id}")//precisa especificar que argumentos serão passados pela URL
	public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody LancamentoRecordDTO dto) {
		try {

			Lancamento lancamentoSalvo = criarLancamentoFromDTO(dto);
			lancamentoSalvo.setId(id);
			/*
			Lancamento lancamentoSalvo = lancamentoService.obterPeloId(id);

			lancamentoSalvo.setDescricao(Objects.requireNonNullElse(dto.descricao(), lancamentoSalvo.getDescricao()));
			lancamentoSalvo.setMes(Objects.requireNonNullElse(dto.mes(), lancamentoSalvo.getMes()));
			lancamentoSalvo.setAno(Objects.requireNonNullElse(dto.ano(), lancamentoSalvo.getAno()));
			
			if (dto.usuario() != null)
				lancamentoSalvo.setUsuario(usuarioService.obterPorId(dto.usuario()));
			
			lancamentoSalvo.setValor(Objects.requireNonNullElse(dto.valor(), lancamentoSalvo.getValor()));
			
			if (dto.status() != null)
				lancamentoSalvo.setStatus(StatusLancamento.valueOf(dto.status()));
			
			if (dto.tipo() != null)
				lancamentoSalvo.setTipo(TipoLancamento.valueOf(dto.tipo()));
			
			lancamentoSalvo = lancamentoService.atualizar(lancamentoSalvo);
			*/
			
			return new ResponseEntity<Object>(lancamentoSalvo, HttpStatus.OK);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Object> deletar(@PathVariable Long id) {
		try {
			Lancamento lancamentoRecuperado = lancamentoService.obterPeloId(id);
			lancamentoService.deletar(lancamentoRecuperado);
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> buscar(
			@RequestParam(value="descricao", required=false) String descricao,
			@RequestParam(value="mes", required=false) Integer mes,
			@RequestParam(value="ano", required=false) Integer ano,
			@RequestParam(value="usuario") Long idUsuario) {//idUsuario é o único parâmetro que é obrigatório
		//acima, outra alternativa seria receber um mapa com os parâmetros:
		//@RequestParam Map<String, String> parametros
		
		try {
			Lancamento lancamentoFiltro = new Lancamento();
			lancamentoFiltro.setDescricao(descricao);
			lancamentoFiltro.setMes(mes);
			lancamentoFiltro.setAno(ano);
			
			Usuario usuario = usuarioService.obterPorId(idUsuario);
			lancamentoFiltro.setUsuario(usuario);
			
			List<Lancamento> resultado = lancamentoService.buscar(lancamentoFiltro);
			
			return new ResponseEntity<Object>(resultado, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}/atualizar-status")
	public ResponseEntity<Object> atualizarStatus(@PathVariable Long id, @RequestParam("status") String novoStatus) {
		try {
			Lancamento lancamento = lancamentoService.obterPeloId(id);
			lancamentoService.atualizarStatus(lancamento, StatusLancamento.valueOf(novoStatus));
			return ResponseEntity.ok(lancamento);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}	
	
}
