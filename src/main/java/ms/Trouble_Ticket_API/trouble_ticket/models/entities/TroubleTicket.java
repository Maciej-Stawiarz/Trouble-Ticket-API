package ms.Trouble_Ticket_API.trouble_ticket.models.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;

import java.util.List;

@Entity
@Builder
@Getter
public class TroubleTicket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@Setter private String externalId;
	@Setter private Long serviceId;
	@Setter private String description;
	
	@Enumerated(EnumType.STRING)
	@Setter private TroubleTicketStatus status;
	
	@OneToMany(mappedBy = "troubleTicket", cascade = CascadeType.PERSIST)
	@OrderBy("date ASC")
	@Setter private List<Note> notes;
}
