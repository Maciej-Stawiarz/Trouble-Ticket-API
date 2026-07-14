package ms.Trouble_Ticket_API.trouble_ticket_note;

import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleTicketNoteRepository extends JpaRepository<Note, String> {
}
