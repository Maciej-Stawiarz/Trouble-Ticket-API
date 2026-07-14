package ms.Trouble_Ticket_API.trouble_ticket_note;

import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.dtos.NoteCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("troubleTicket")
public interface TroubleTicketNoteAPI {
	
	
	@PostMapping("{id}/note")
	ResponseEntity<Note> addTroubleTicketNote(@RequestBody NoteCreateRequest request);
}
