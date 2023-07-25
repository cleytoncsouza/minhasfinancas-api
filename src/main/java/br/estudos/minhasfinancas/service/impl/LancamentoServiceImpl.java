package br.estudos.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.estudos.minhasfinancas.exception.RegraNegocioException;
import br.estudos.minhasfinancas.model.entity.Lancamento;
import br.estudos.minhasfinancas.model.enums.StatusLancamento;
import br.estudos.minhasfinancas.model.enums.TipoLancamento;
import br.estudos.minhasfinancas.model.repository.LancamentoRepository;
import br.estudos.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	private LancamentoRepository repository;
	
	@Autowired
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);;
		return repository.save(lancamento);
	}

	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		Objects.requireNonNull(lancamento.getUsuario().getId());
		return repository.save(lancamento);
	}

	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
	}

	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example<Lancamento> example = Example.of(lancamentoFiltro, 
					ExampleMatcher.matching()
									.withIgnoreCase()
										.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descrição válida");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um mês válido");
		}
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um ano válido");
		}
		
		if (lancamento.getUsuario() == null) {
			throw new RegraNegocioException("Informe um usuário");
		}
		
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor válido para o lancamento");
		}
		
		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de lançamento");
		}
		
	}

	public Lancamento obterPeloId(Long id) {
		Optional<Lancamento> resultado = repository.findById(id);
		
		if (resultado.isEmpty())
			throw new RegraNegocioException("Lancamento não encontrado");
		
		return resultado.get();
	}

	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long idUsuario) {
		BigDecimal receita = repository.obterSaldoPorTipoLancamentoEUsuario(idUsuario, TipoLancamento.RECEITA);
		BigDecimal despesa = repository.obterSaldoPorTipoLancamentoEUsuario(idUsuario, TipoLancamento.DESPESA);
		if (receita != null)
			return receita.subtract(despesa);
		else if (despesa != null)
			return despesa.negate();
		else
			throw new RegraNegocioException("não há lancamentos para esse usuário");
	}

}
