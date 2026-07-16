package ms.Trouble_Ticket_API.trouble_ticket_note;

import ms.Trouble_Ticket_API.trouble_ticket_note.models.dtos.NoteCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TroubleTicketNoteControllerTest {
	@Mock
	private TroubleTicketNoteService service;
	@InjectMocks
	private TroubleTicketNoteController controller;
	
	@Test
	void testAdd() {
		NoteCreateRequest request = new NoteCreateRequest();
		
		when(service.add("1", request))
				.thenReturn(new Note());
		
		
		ResponseEntity<Note> response = controller.addTroubleTicketNote("1", request);
		
		
		assertThat(response.getStatusCode())
				.isEqualTo(HttpStatus.CREATED);
	}
}