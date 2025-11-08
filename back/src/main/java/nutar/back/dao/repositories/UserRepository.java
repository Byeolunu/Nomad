package nutar.back.dao.repositories;

import nutar.back.dao.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
    boolean existsByEmail(String email);
}
