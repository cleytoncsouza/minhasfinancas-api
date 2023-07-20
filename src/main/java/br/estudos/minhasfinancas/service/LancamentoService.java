package br.estudos.minhasfinancas.service;

import java.util.List;

import br.estudos.minhasfinancas.model.entity.Lancamento;
import br.estudos.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	public Lancamento salvar(Lancamento lancamento);
	
	public Lancamento atualizar(Lancamento lancamento);
	
	public void deletar(Lancamento lancamento);
	
	public List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	public void validar(Lancamento lancamento);
}
