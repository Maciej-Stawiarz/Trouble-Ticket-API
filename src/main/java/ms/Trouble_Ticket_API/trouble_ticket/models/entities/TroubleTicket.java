package ms.Trouble_Ticket_API.trouble_ticket.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;

import java.util.List;

@Entity
@RequiredArgsConstructor
public class TroubleTicket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	private String externalID;
	private Integer serviceID;
	private String description;
	private TroubleTicketStatus status;
	private List<Note> notes;
}
