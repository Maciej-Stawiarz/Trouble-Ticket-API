package ms.Trouble_Ticket_API.trouble_ticket_note;

import ms.Trouble_Ticket_API.trouble_ticket_note.models.dtos.NoteCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TroubleTicketNoteController implements TroubleTicketNoteAPI {
	
	@Override
	public ResponseEntity<Note> addTroubleTicketNote(String id, NoteCreateRequest request) {
		return null;
	}
}
