package ms.Trouble_Ticket_API.trouble_ticket;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.TicketNotFoundException;
import ms.Trouble_Ticket_API.exceptions.model.exceptions.ValidationException;
import ms.Trouble_Ticket_API.security.TenantContext;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TicketCreationResult;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCloseStatusRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketSummary;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketCloseStatus;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketCreateStatus;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;
import ms.Trouble_Ticket_API.trouble_ticket_note.models.entities.Note;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TroubleTicketService {
	
	private final TroubleTicketRepository repository;
	private final TenantContext tenantContext;
	
	@Transactional
	public TicketCreationResult create(TroubleTicketCreateRequest request) {
		if (request == null ||
			request.status == null ||
			!request.status.equals(TroubleTicketCreateStatus.NEW)) {
			
			throw new ValidationException("Pole status ma niedozwoloną wartość dla tej operacji.");
		}
		
		String tenantId = tenantContext.currentTenantId();
		
		Optional<TroubleTicket> ticket = repository
				.findByTenantIdAndExternalId(
						tenantId,
						request.externalId);
		
		if (ticket.isPresent()) {
			return new TicketCreationResult(ticket.get(), false);
		}
		
		TroubleTicket newTicket = TroubleTicket.builder()
				.externalId(request.externalId)
				.serviceId(request.serviceId)
				.description(request.description)
				.status(TroubleTicketStatus.ACKNOWLEDGED)
				.tenantId(tenantId)
				.build();
		
		Note newNote = Note.builder()
				.date(OffsetDateTime.now())
				.text(request.note)
				.build();
		
		newTicket.addNote(newNote);
		
		try {
			return new TicketCreationResult(repository.saveAndFlush(newTicket), true);
		} catch (DataIntegrityViolationException exception) {
			return repository
					.findByTenantIdAndExternalId(
							tenantId,
							request.externalId)
					.map(t -> new TicketCreationResult(t, false))
					.orElseThrow(() -> exception);
		}
	}
	
	public List<TroubleTicketSummary> getAll() {
		return repository
				.findAllByTenantId(tenantContext.currentTenantId()).stream()
				.map(troubleTicket -> new TroubleTicketSummary(
						troubleTicket.getExternalId(),
						troubleTicket.getServiceId(),
						troubleTicket.getDescription(),
						troubleTicket.getStatus()))
				.collect(Collectors.toList());
	}
	
	public TroubleTicket get(String id) {
		return repository
				.findByTenantIdAndId(tenantContext.currentTenantId(), id)
				.orElseThrow(() -> new TicketNotFoundException("Zgłoszenie nie istnieje albo nie jest widoczne w tenant scope użytkownika."));
	}
	
	@Transactional
	public TroubleTicket close(String id, TroubleTicketCloseStatusRequest request) {
		if (request == null ||
			request.status == null ||
			!request.status.equals(TroubleTicketCloseStatus.CLOSED)) {
			
			throw new ValidationException("Pole status ma niedozwoloną wartość dla tej operacji.");
		}
		
		TroubleTicket ticket = get(id);
		
		ticket.setStatus(TroubleTicketStatus.CLOSED);
		return repository.save(ticket);
	}
}
