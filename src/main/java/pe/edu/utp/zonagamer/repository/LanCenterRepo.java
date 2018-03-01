package pe.edu.utp.zonagamer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pe.edu.utp.zonagamer.entity.LanCenter;

/**
 * @author UTP
 */
public interface LanCenterRepo extends CrudRepository<LanCenter, Long> {

    @Query(value = "SELECT * FROM lancenter WHERE user = ?1", nativeQuery = true)
    LanCenter findByUser(Long user);
}
