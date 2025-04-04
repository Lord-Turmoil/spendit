package top.tony.spendit.api.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tony.spendit.api.models.AccountInvitation;

@Repository
public interface AccountInvitationRepository extends JpaRepository<AccountInvitation, Long> {
}
