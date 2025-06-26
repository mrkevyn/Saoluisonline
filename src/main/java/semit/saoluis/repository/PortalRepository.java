package semit.saoluis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semit.saoluis.model.Portal;

import java.util.List;

@Repository
public interface PortalRepository extends JpaRepository<Portal, Long> {
    List<Portal> findByIsVisibleTrue();
}
