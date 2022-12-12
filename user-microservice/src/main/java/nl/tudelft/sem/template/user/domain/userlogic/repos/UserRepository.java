package nl.tudelft.sem.template.user.domain.userlogic.repos;

import nl.tudelft.sem.template.user.domain.userlogic.AppUser;
import nl.tudelft.sem.template.user.domain.userlogic.NetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
    /**
     * A DDD repository for quering and persisting user aggregate roots.
     */
    @Repository
    public interface UserRepository extends JpaRepository<AppUser, String> {
        /**
         * Find user by NetID.
         */
        Optional<AppUser> findByNetId(NetId netId);

        /**
         * Check if an existing user already uses a NetID.
         */
        boolean existsByNetId(NetId netId);
    }
