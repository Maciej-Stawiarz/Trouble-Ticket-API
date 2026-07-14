package ms.Trouble_Ticket_API.trouble_ticket;

import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleTicketRepository extends JpaRepository<TroubleTicket, String> {
}
