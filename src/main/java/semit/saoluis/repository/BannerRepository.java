package semit.saoluis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semit.saoluis.model.Banner;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    
    // 🔥 Método para listar somente onde isVisible = true
    List<Banner> findByIsVisibleTrue();
}
