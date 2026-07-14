package ms.Trouble_Ticket_API.trouble_ticket_note.models.dtos;

import jakarta.validation.constraints.NotBlank;

public class NoteCreateRequest {
	
	@NotBlank
	public String text;
}
