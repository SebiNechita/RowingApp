package nl.tudelft.sem.template.user.domain.userlogic.repos;

import nl.tudelft.sem.template.user.domain.userlogic.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.cert.Certificate;

@Repository
public interface UserCertificatesRepository extends JpaRepository<UserCertificate, String> {
}
