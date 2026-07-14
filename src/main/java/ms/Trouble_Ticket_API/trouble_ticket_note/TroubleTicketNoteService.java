package ms.Trouble_Ticket_API.trouble_ticket_note;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.exceptions.TicketNotFoundException;
import ms.Trouble_Ticket_API.exceptions.ValidationException;
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
	
	// TODO: Get tenant ID out of the context to validate, wheter user can actually view the note
	// TODO: Possibly return some NoteResponse dto, to not use the entity class
	@Transactional
	public Note add(String ticketId, NoteCreateRequest request) {
		if (request == null ||
			request.text == null ||
			request.text.isBlank()) {
			throw new ValidationException("Pole text ma niedozwoloną wartość dla tej operacji.");
		}
		
		Optional<TroubleTicket> ticket = ticketRepository.findById(ticketId);
		
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
