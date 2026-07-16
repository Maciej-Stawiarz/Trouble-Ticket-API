package ms.Trouble_Ticket_API.trouble_ticket_note;

import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.dtos.NoteCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TroubleTicketNoteController implements TroubleTicketNoteAPI {
	
	private final TroubleTicketNoteService service;
	
	@Override
	public ResponseEntity<Note> addTroubleTicketNote(String id, NoteCreateRequest request) {
		return new ResponseEntity<>(
				service.add(id, request),
				HttpStatus.CREATED);
	}
}
