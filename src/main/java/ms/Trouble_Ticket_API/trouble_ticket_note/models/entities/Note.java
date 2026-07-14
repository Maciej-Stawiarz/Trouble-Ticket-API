package ms.Trouble_Ticket_API.trouble_ticket_note.models.entities;

import jakarta.persistence.*;
import lombok.Builder;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;

import java.time.OffsetDateTime;

@Entity
@Builder
public class Note {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	private String text;
	private OffsetDateTime date;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "trouble_ticket_id", nullable = false, updatable = false)
	private TroubleTicket troubleTicket;
	
}
