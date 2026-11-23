package com.controleportaria.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.controleportaria.model.Role;
import com.controleportaria.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	boolean existsByRole(Role role);
	
	public Usuario findByNomeUsuario(String nomeUsuario);
	
	@Query("select u from Usuario u where u.nome like %?1%")
	List<Usuario> findUsuarioByName(String nome);
	
	@Query("from Usuario u where u.role = :role")
	public List<Usuario> findUsuarioByRole(Role role);

}
