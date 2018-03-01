package pe.edu.utp.zonagamer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pe.edu.utp.zonagamer.entity.Gamer;

/**
 * @author UTP
 */
public interface GamerRepo extends CrudRepository<Gamer, Long> {

    @Query(value = "SELECT * FROM gamer WHERE user = ?1", nativeQuery = true)
    Gamer findByUser(Long user);
}
