package br.estudos.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.query.Param;

import br.estudos.minhasfinancas.model.entity.Lancamento;
import br.estudos.minhasfinancas.model.enums.StatusLancamento;
import br.estudos.minhasfinancas.model.enums.TipoLancamento;

public interface LancamentoService {

	public Lancamento salvar(Lancamento lancamento);
	
	public Lancamento atualizar(Lancamento lancamento);
	
	public void deletar(Lancamento lancamento);
	
	public List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	public void validar(Lancamento lancamento);
	
	public Lancamento obterPeloId(Long id);
	
	public BigDecimal obterSaldoPorUsuario(Long idUsuario);
	
}
