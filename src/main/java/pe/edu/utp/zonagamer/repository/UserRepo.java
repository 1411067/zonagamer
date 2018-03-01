package pe.edu.utp.zonagamer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pe.edu.utp.zonagamer.entity.User;

/**
 * @author UTP
 */
public interface UserRepo extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM user WHERE email = ?1 AND password = ?2", nativeQuery = true)
    User login(String email, String password);

    @Query(value = "SELECT * FROM user WHERE email = ?1", nativeQuery = true)
    User findByEmail(String email);
}
