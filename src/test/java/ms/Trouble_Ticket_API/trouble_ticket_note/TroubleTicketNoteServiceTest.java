package ms.Trouble_Ticket_API.trouble_ticket_note;

import ms.Trouble_Ticket_API.exceptions.model.exceptions.TicketNotFoundException;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.ValidationException;
import ms.Trouble_Ticket_API.security.TenantContext;
import ms.Trouble_Ticket_API.trouble_ticket.TroubleTicketRepository;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.dtos.NoteCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TroubleTicketNoteServiceTest {
	
	private TroubleTicketNoteService noteService;
	
	@Mock
	private TroubleTicketNoteRepository noteRepository;
	@Mock
	private TroubleTicketRepository ticketRepository;
	@Mock
	private TenantContext tenantContext;
	
	
	@BeforeEach
	void init() {
		noteService = new TroubleTicketNoteService(
				noteRepository,
				ticketRepository,
				tenantContext);
	}
	
	
	@Test
	@DisplayName("Should throw ValidationException when request is null")
	void add_ShouldThrowValidationException_WhenRequestIsNull() {
		String ticketId = "ticket-123";
		
		assertThatThrownBy(() -> noteService.add(ticketId, null))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Pole text ma niedozwoloną wartość dla tej operacji.");
		
		verifyNoInteractions(ticketRepository, tenantContext, noteRepository);
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "   "})
	@DisplayName("Should throw ValidationException when note text is null, empty, or blank")
	void add_ShouldThrowValidationException_WhenTextIsInvalid(String invalidText) {
		String ticketId = "ticket-123";
		NoteCreateRequest request = new NoteCreateRequest();
		request.text = invalidText;
		
		assertThatThrownBy(() -> noteService.add(ticketId, request))
				.isInstanceOf(ValidationException.class)
				.hasMessage("Pole text ma niedozwoloną wartość dla tej operacji.");
		
		verifyNoInteractions(ticketRepository, tenantContext, noteRepository);
	}
	
	@Test
	@DisplayName("Should throw TicketNotFoundException when ticket does not exist or tenant mismatch")
	void add_ShouldThrowTicketNotFoundException_WhenTicketNotFound() {
		String ticketId = "ticket-123";
		String tenantId = "tenant-abc";
		
		NoteCreateRequest request = new NoteCreateRequest();
		request.text = "This is a valid note.";
		
		when(tenantContext.currentTenantId())
				.thenReturn(tenantId);
		when(ticketRepository.findByTenantIdAndId(tenantId, ticketId))
				.thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> noteService.add(ticketId, request))
				.isInstanceOf(TicketNotFoundException.class)
				.hasMessage("Zgłoszenie nie istnieje albo nie jest widoczne w tenant scope użytkownika.");
		
		verify(noteRepository, never()).save(any());
	}
	
	@Test
	@DisplayName("Should successfully save and return the note when request is valid")
	void add_ShouldSaveAndReturnNote_WhenRequestIsValid() {
		String ticketId = "ticket-123";
		String tenantId = "tenant-abc";
		String noteText = "Everything looks good!";
		
		NoteCreateRequest request = new NoteCreateRequest();
		request.text = noteText;
		
		TroubleTicket existingTicket = new TroubleTicket();
		
		Note expectedSavedNote = Note.builder()
				.text(noteText)
				.troubleTicket(existingTicket)
				.date(OffsetDateTime.now())
				.build();
		
		when(tenantContext.currentTenantId())
				.thenReturn(tenantId);
		when(ticketRepository.findByTenantIdAndId(tenantId, ticketId))
				.thenReturn(Optional.of(existingTicket));
		when(noteRepository.save(any(Note.class)))
				.thenReturn(expectedSavedNote);
		
		Note result = noteService.add(ticketId, request);
		
		assertThat(result)
				.isNotNull();
		assertThat(result.getText())
				.isEqualTo(noteText);
		assertThat(result.getTroubleTicket())
				.isEqualTo(existingTicket);
		
		verify(noteRepository, times(1))
				.save(any(Note.class));

	}
}