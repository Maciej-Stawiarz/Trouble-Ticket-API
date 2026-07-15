package ms.Trouble_Ticket_API.trouble_ticket_note;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.TicketNotFoundException;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.ValidationException;
import ms.Trouble_Ticket_API.security.TenantContext;
import ms.Trouble_Ticket_API.trouble_ticket.TroubleTicketRepository;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.dtos.NoteCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TroubleTicketNoteService {
	
	private final TroubleTicketNoteRepository noteRepository;
	private final TroubleTicketRepository ticketRepository;
	private final TenantContext tenantContext;
	
	@Transactional
	public Note add(String ticketId, NoteCreateRequest request) {
		if (request == null ||
			request.text == null ||
			request.text.isBlank()) {
			throw new ValidationException("Pole text ma niedozwoloną wartość dla tej operacji.");
		}
		
		Optional<TroubleTicket> ticket = ticketRepository
				.findByTenantIdAndId(
						tenantContext.currentTenantId(),
						ticketId);
		
		if (ticket.isEmpty()) {
			throw new TicketNotFoundException("Zgłoszenie nie istnieje albo nie jest widoczne w tenant scope użytkownika.");
		}
		
		Note note = Note.builder()
				.date(OffsetDateTime.now())
				.text(request.text)
				.troubleTicket(ticket.get())
				.build();
		
		return noteRepository.save(note);
	}
}
