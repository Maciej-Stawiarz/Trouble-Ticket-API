package ms.Trouble_Ticket_API.trouble_ticket.models.entities;

import jakarta.persistence.*;
import lombok.*;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "trouble_ticket", uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "external_id"}))
public class TroubleTicket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@Column(nullable = false)
	private String externalId;
	
	@Column(nullable = false)
	private Long serviceId;
	
	@Column(nullable = false)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Setter private TroubleTicketStatus status;
	
	@OneToMany(mappedBy = "troubleTicket", cascade = CascadeType.PERSIST)
	@OrderBy("date ASC")
	@Builder.Default
	private List<Note> notes = new ArrayList<>();
	
	@Column(name = "tenant_id", updatable = false, nullable = false)
	private String tenantId;
	
	
	
	public void addNote(Note noteToAdd) {
		notes.add(noteToAdd);
		noteToAdd.setTroubleTicket(this);
	}
}
