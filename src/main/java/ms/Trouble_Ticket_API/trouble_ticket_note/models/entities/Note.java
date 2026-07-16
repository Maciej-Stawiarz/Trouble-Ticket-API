package ms.Trouble_Ticket_API.trouble_ticket_note.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;

import java.time.OffsetDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Note {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@Column(nullable = false)
	private String text;
	
	@Column(nullable = false)
	private OffsetDateTime date;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "trouble_ticket_id", nullable = false, updatable = false)
	@JsonIgnore
	@Setter private TroubleTicket troubleTicket;
	
}
