package semit.saoluis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semit.saoluis.model.Projetos;

@Repository
public interface ProjetosRepository extends JpaRepository<Projetos, Long> {
}
