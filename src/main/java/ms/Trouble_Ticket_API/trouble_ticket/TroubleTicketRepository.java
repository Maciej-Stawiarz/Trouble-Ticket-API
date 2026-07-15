package ms.Trouble_Ticket_API.trouble_ticket;

import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TroubleTicketRepository extends JpaRepository<TroubleTicket, String> {
	
	Optional<TroubleTicket> findByTenantIdAndId(String tenantId, String id);
	
	Optional<TroubleTicket> findByTenantIdAndExternalId(String tenantId, String externalId);
	
	List<TroubleTicket> findAllByTenantId(String tenantId);
}
