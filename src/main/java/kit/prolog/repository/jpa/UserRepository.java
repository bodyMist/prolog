package kit.prolog.repository.jpa;

import kit.prolog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    User findOneById(Long id);
    User findOneByEmail(String email);
    User findOneByAccountAndPassword(String account, String password);
    User findOneByAccountAndEmail(String account, String email);

    User findOneBySnsAndSocialKey(Integer sns, String social_key);
}
