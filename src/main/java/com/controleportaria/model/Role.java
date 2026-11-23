package com.controleportaria.model;

public enum Role {
	
	ADMIN("Administrador"), USER("Padrão"), SUP("Supervisor");
	
	private Role(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;
	
	public String getDescricao() {
		return this.descricao;
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
