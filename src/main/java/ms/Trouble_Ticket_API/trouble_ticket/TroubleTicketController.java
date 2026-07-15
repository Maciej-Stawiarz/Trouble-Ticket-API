package ms.Trouble_Ticket_API.trouble_ticket;

import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TicketCreationResult;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCloseStatusRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketSummary;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TroubleTicketController implements TroubleTicketAPI {
	
	private final TroubleTicketService service;
	
	@Override
	public ResponseEntity<TroubleTicket> createTroubleTicket(TroubleTicketCreateRequest request) {
		TicketCreationResult ticket = service.create(request);
		
		if (ticket.wasCreated) {
			return new ResponseEntity<>(
					ticket.troubleTicket,
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(
					ticket.troubleTicket,
					HttpStatus.OK);
		}
	}
	
	@Override
	public ResponseEntity<List<TroubleTicketSummary>> listTroubleTicket() {
		return ResponseEntity
				.ok()
				.body(service.getAll());
	}
	
	@Override
	public ResponseEntity<TroubleTicket> getTroubleTicketById(String id) {
		return ResponseEntity
				.ok()
				.body(service.get(id));
	}
	
	@Override
	public ResponseEntity<TroubleTicket> closeTroubleTicket(String id, TroubleTicketCloseStatusRequest request) {
		return ResponseEntity
				.ok()
				.body(service.close(id, request));
	}
}
