package com.controleportaria.model;

public enum StatusEntrada {
	
	ABERTA("Saída pendente"), FINALIZADA("Finalizada");
	
	StatusEntrada (String descricao){
		this.descricao = descricao;
	}
	private String descricao;

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	@Override
	public String toString() {
		return this.descricao;
	}
	public String getName() {
		return this.name();
	}
}
