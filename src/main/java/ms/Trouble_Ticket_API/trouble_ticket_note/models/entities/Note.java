package ms.Trouble_Ticket_API.trouble_ticket_note.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
public class Note {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	private String text;
	private LocalDateTime date;
}
