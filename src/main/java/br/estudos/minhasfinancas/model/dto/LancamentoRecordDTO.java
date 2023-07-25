package br.estudos.minhasfinancas.model.dto;

import java.math.BigDecimal;

public record LancamentoRecordDTO(
		Long id, 
		String descricao, 
		Integer mes, 
		Integer ano, 
		Long usuario,
		BigDecimal valor,
		String tipo, 
		String status) { }
