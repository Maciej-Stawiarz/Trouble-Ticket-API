package ms.Trouble_Ticket_API.trouble_ticket;

import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.exceptions.TicketNotFoundException;
import ms.Trouble_Ticket_API.exceptions.ValidationException;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCloseStatusRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketSummary;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketCloseStatus;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TroubleTicketService {
	
	private final TroubleTicketRepository repository;
	
	// TODO: Validate whether is in scope of the user privileges
	public TroubleTicket create(TroubleTicketCreateRequest request) {
		if (request == null ||
			request.status == null ||
			!request.status.equals(TroubleTicketStatus.NEW)) {
			
			throw new ValidationException("Pole status ma niedozwoloną wartość dla tej operacji.");
		}
		
		// TODO: Validate service ID
//		if (request.serviceId) {
//			throw new ServiceNotFoundException("Wskazana usługa nie istnieje, nie jest aktywna albo nie należy do tenant scope użytkownika.");
//		}
		
		Optional<TroubleTicket> ticket = repository.findById(request.externalId);
		
		if (ticket.isPresent()) {
			return ticket.get();
		} else {
			TroubleTicket newTicket = TroubleTicket.builder()
					.externalId(request.externalId)
					.serviceId(request.serviceId)
					.description(request.description)
					.status(TroubleTicketStatus.ACKNOWLEDGED)
					.build();
			
			Note newNote = Note.builder()
					.date(OffsetDateTime.now())
					.text(request.note)
					.troubleTicket(newTicket)
					.build();
			
			newTicket.setNotes(List.of(newNote));
					
			return repository.save(newTicket);
		}
	}
	
	// TODO: Validate whether is in scope of the user privileges
	public List<TroubleTicketSummary> getAll() {
		return repository
				.findAll().stream()
				.map(troubleTicket -> new TroubleTicketSummary(
						troubleTicket.getExternalId(),
						troubleTicket.getServiceId(),
						troubleTicket.getDescription(),
						troubleTicket.getStatus()))
				.collect(Collectors.toList());
	}
	
	// TODO: Validate whether is in scope of the user privileges
	public TroubleTicket get(String id) {
		return repository
				.findById(id)
				.orElseThrow(() -> new TicketNotFoundException("Zgłoszenie nie istnieje albo nie jest widoczne w tenant scope użytkownika."));
	}
	
	// TODO: Validate whether is in scope of the user privileges
	public TroubleTicket close(String id, TroubleTicketCloseStatusRequest request) {
		if (request == null ||
			request.status == null ||
			!request.status.equals(TroubleTicketCloseStatus.CLOSED)) {
			
			throw new ValidationException("Pole status ma niedozwoloną wartość dla tej operacji.");
		}
		
		TroubleTicket ticket = get(id);
		
		ticket.setStatus(TroubleTicketStatus.CLOSED);
		return repository.save(ticket);
	}
}
