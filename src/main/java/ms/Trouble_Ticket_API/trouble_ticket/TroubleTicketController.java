package ms.Trouble_Ticket_API.trouble_ticket;

import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TroubleTicketController implements TroubleTicketAPI {
	
	@Override
	public ResponseEntity<TroubleTicket> createTroubleTicket(TroubleTicketCreateRequest request) {
		return null;
	}
	
	@Override
	public ResponseEntity<List<TroubleTicket>> listTroubleTicket() {
		return null;
	}
	
	@Override
	public ResponseEntity<TroubleTicket> getTroubleTicketById() {
		return null;
	}
	
	@Override
	public ResponseEntity<TroubleTicket> closeTroubleTicket() {
		return null;
	}
}
