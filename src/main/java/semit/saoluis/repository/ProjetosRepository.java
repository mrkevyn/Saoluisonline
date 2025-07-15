package semit.saoluis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import semit.saoluis.model.Projetos;

@Repository
public interface ProjetosRepository extends JpaRepository<Projetos, Long> {
	
	List<Projetos> findByIsVisibleTrue();
}

