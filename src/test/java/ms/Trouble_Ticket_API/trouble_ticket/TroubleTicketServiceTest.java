package ms.Trouble_Ticket_API.trouble_ticket;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TroubleTicketServiceTest {
	
	private TroubleTicketService ticketService;
	
	@Mock
	private TroubleTicketRepository repository;
	@Mock
	private TenantContext tenantContext;
	
	
	@BeforeEach
	void init() {
		ticketService = new TroubleTicketService(
				repository,
				tenantContext);
	}
	
	@Nested
	@DisplayName("Create Ticket Tests")
	class CreateTicketTests {
		
		@Test
		@DisplayName("Should throw ValidationException when request is null")
		void create_ShouldThrowValidationException_WhenRequestIsNull() {
			assertThatThrownBy(() -> ticketService.create(null))
					.isInstanceOf(ValidationException.class)
					.hasMessage("Pole status ma niedozwoloną wartość dla tej operacji.");
			
			verifyNoInteractions(repository, tenantContext);
		}
		
		@Test
		@DisplayName("Should throw ValidationException when status is not NEW")
		void create_ShouldThrowValidationException_WhenStatusIsNotNew() {
			TroubleTicketCreateRequest request = new TroubleTicketCreateRequest();
			request.status = null;
			
			assertThatThrownBy(() -> ticketService.create(request))
					.isInstanceOf(ValidationException.class)
					.hasMessage("Pole status ma niedozwoloną wartość dla tej operacji.");
			
			verifyNoInteractions(repository, tenantContext);
		}
		
		@Test
		@DisplayName("Should return existing ticket without creating new when externalId already exists")
		void create_ShouldReturnExistingTicket_WhenTicketAlreadyExists() {
			String tenantId = "tenant-1";
			TroubleTicketCreateRequest request = new TroubleTicketCreateRequest();
			request.status = TroubleTicketCreateStatus.NEW;
			request.externalId = "ext-100";
			
			TroubleTicket existingTicket = TroubleTicket.builder()
					.externalId("ext-100")
					.tenantId(tenantId)
					.build();
			
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findByTenantIdAndExternalId(tenantId, request.externalId))
					.thenReturn(Optional.of(existingTicket));
			
			
			TicketCreationResult result = ticketService.create(request);
			

			assertThat(result.wasCreated)
					.isFalse();
			assertThat(result.troubleTicket)
					.isEqualTo(existingTicket);
			
			verify(repository, never())
					.saveAndFlush(any());
		}
		
		@Test
		@DisplayName("Should successfully create, add note, and return a new ticket")
		void create_ShouldSaveNewTicketWithNote_WhenRequestIsValid() {
			String tenantId = "tenant-1";
			TroubleTicketCreateRequest request = new TroubleTicketCreateRequest();
			request.status = TroubleTicketCreateStatus.NEW;
			request.externalId = "ext-100";
			request.serviceId = 1L;
			request.description = "Broken line";
			request.note = "First setup note";
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findByTenantIdAndExternalId(tenantId, request.externalId))
					.thenReturn(Optional.empty());
			
			TroubleTicket savedTicketMock = TroubleTicket.builder()
					.id("ticket-generated-id")
					.externalId("ext-100")
					.status(TroubleTicketStatus.ACKNOWLEDGED)
					.notes(new ArrayList<>())
					.build();
			
			when(repository.saveAndFlush(any(TroubleTicket.class)))
					.thenReturn(savedTicketMock);
			
			
			TicketCreationResult result = ticketService.create(request);
			
			
			assertThat(result.wasCreated)
					.isTrue();
			assertThat(result.troubleTicket)
					.isEqualTo(savedTicketMock);
			
			verify(repository, times(1))
					.saveAndFlush(any(TroubleTicket.class));
		}
		
		@Test
		@DisplayName("Should handle concurrent database saves gracefully and return existing ticket on DataIntegrityViolationException")
		void create_ShouldReturnExistingTicket_WhenDataIntegrityViolationOccurs() {
			String tenantId = "tenant-1";
			TroubleTicketCreateRequest request = new TroubleTicketCreateRequest();
			request.status = TroubleTicketCreateStatus.NEW;
			request.externalId = "ext-100";
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findByTenantIdAndExternalId(tenantId, request.externalId))
					.thenReturn(Optional.empty());

			TroubleTicket concurrentlyCreatedTicket = TroubleTicket.builder()
					.externalId("ext-100")
					.tenantId(tenantId)
					.build();
			
			when(repository.findByTenantIdAndExternalId(tenantId, request.externalId))
					.thenReturn(Optional.of(concurrentlyCreatedTicket));
			
			
			TicketCreationResult result = ticketService.create(request);
			
			assertThat(result.wasCreated)
					.isFalse();
			assertThat(result.troubleTicket)
					.isEqualTo(concurrentlyCreatedTicket);
		}
		
		@Test
		@DisplayName("Should throw original DataIntegrityViolationException if lookup fails after constraint violation")
		void create_ShouldThrowException_WhenDataIntegrityViolationOccursAndTicketStillNotFound() {
			String tenantId = "tenant-1";
			TroubleTicketCreateRequest request = new TroubleTicketCreateRequest();
			request.status = TroubleTicketCreateStatus.NEW;
			request.externalId = "ext-100";
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findByTenantIdAndExternalId(tenantId, request.externalId))
					.thenReturn(Optional.empty());
			
			DataIntegrityViolationException expectedDbException = new DataIntegrityViolationException("DB Error");
			when(repository.saveAndFlush(any(TroubleTicket.class)))
					.thenThrow(expectedDbException);
			
			when(repository.findByTenantIdAndExternalId(tenantId, request.externalId))
					.thenReturn(Optional.empty());
			
			assertThatThrownBy(() -> ticketService.create(request))
					.isEqualTo(expectedDbException);
		}
	}
	
	@Nested
	@DisplayName("Get and GetAll Tests")
	class GetAndGetAllTests {
		
		@Test
		@DisplayName("Should map and return all trouble ticket summaries for tenant context")
		void getAll_ShouldReturnListOfSummaries() {
			String tenantId = "tenant-1";
			TroubleTicket ticket1 = TroubleTicket.builder()
					.externalId("ext-1")
					.serviceId(1L)
					.description("Issue 1")
					.status(TroubleTicketStatus.ACKNOWLEDGED)
					.build();
			
			TroubleTicket ticket2 = TroubleTicket.builder()
					.externalId("ext-2")
					.serviceId(2L)
					.description("Issue 2")
					.status(TroubleTicketStatus.CLOSED)
					.build();
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findAllByTenantId(tenantId))
					.thenReturn(List.of(ticket1, ticket2));
			
			
			List<TroubleTicketSummary> result = ticketService.getAll();
			
			
			assertThat(result).hasSize(2);
			
			assertThat(result.get(0).externalId).isEqualTo("ext-1");
			assertThat(result.get(0).serviceId).isEqualTo(1L);
			assertThat(result.get(0).description).isEqualTo("Issue 1");
			assertThat(result.get(0).status).isEqualTo(TroubleTicketStatus.ACKNOWLEDGED);
			
			assertThat(result.get(1).externalId).isEqualTo("ext-2");
			assertThat(result.get(1).serviceId).isEqualTo(2L);
			assertThat(result.get(1).description).isEqualTo("Issue 2");
			assertThat(result.get(1).status).isEqualTo(TroubleTicketStatus.CLOSED);
		}
		
		@Test
		@DisplayName("Should return empty list when no tickets are found")
		void getAll_ShouldReturnEmptyList_WhenNoTicketsFound() {
			String tenantId = "tenant-1";
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findAllByTenantId(tenantId))
					.thenReturn(Collections.emptyList());
			
			
			List<TroubleTicketSummary> result = ticketService.getAll();
			
			
			assertThat(result).isEmpty();
		}
		
		@Test
		@DisplayName("Should return ticket when it exists and matches tenant context")
		void get_ShouldReturnTicket_WhenTicketExists() {
			String tenantId = "tenant-1";
			String ticketId = "ticket-123";
			TroubleTicket expectedTicket = TroubleTicket.builder().id(ticketId).tenantId(tenantId).build();
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findByTenantIdAndId(tenantId, ticketId))
					.thenReturn(Optional.of(expectedTicket));
			
			
			TroubleTicket result = ticketService.get(ticketId);
			
			
			assertThat(result).isEqualTo(expectedTicket);
		}
		
		@Test
		@DisplayName("Should throw TicketNotFoundException when requested ticket is missing or belongs to another tenant")
		void get_ShouldThrowTicketNotFoundException_WhenTicketNotFound() {
			String tenantId = "tenant-1";
			String ticketId = "ticket-123";
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findByTenantIdAndId(tenantId, ticketId))
					.thenReturn(Optional.empty());
			
			assertThatThrownBy(() -> ticketService.get(ticketId))
					.isInstanceOf(TicketNotFoundException.class)
					.hasMessage("Zgłoszenie nie istnieje albo nie jest widoczne w tenant scope użytkownika.");
		}
	}
	
	@Nested
	@DisplayName("Close Ticket Tests")
	class CloseTicketTests {
		
		@Test
		@DisplayName("Should throw ValidationException when close status request is null")
		void close_ShouldThrowValidationException_WhenRequestIsNull() {
			assertThatThrownBy(() -> ticketService.close("ticket-123", null))
					.isInstanceOf(ValidationException.class)
					.hasMessage("Pole status ma niedozwoloną wartość dla tej operacji.");
			
			verifyNoInteractions(repository, tenantContext);
		}
		
		@Test
		@DisplayName("Should throw ValidationException when status is not CLOSED")
		void close_ShouldThrowValidationException_WhenStatusIsNotClosed() {
			TroubleTicketCloseStatusRequest request = new TroubleTicketCloseStatusRequest();
			request.status = null;
			
			assertThatThrownBy(() -> ticketService.close("ticket-123", request))
					.isInstanceOf(ValidationException.class)
					.hasMessage("Pole status ma niedozwoloną wartość dla tej operacji.");
			
			verifyNoInteractions(repository, tenantContext);
		}
		
		@Test
		@DisplayName("Should successfully set status to CLOSED and return updated ticket")
		void close_ShouldUpdateStatusToClosed_WhenRequestIsValid() {
			String tenantId = "tenant-1";
			String ticketId = "ticket-123";
			TroubleTicketCloseStatusRequest request = new TroubleTicketCloseStatusRequest();
			request.status = TroubleTicketCloseStatus.CLOSED;
			
			TroubleTicket existingTicket = TroubleTicket.builder()
					.id(ticketId)
					.tenantId(tenantId)
					.status(TroubleTicketStatus.ACKNOWLEDGED)
					.build();
			
			when(tenantContext.currentTenantId())
					.thenReturn(tenantId);
			when(repository.findByTenantIdAndId(tenantId, ticketId))
					.thenReturn(Optional.of(existingTicket));
			when(repository.save(any(TroubleTicket.class)))
					.thenAnswer(invocation -> invocation.getArgument(0));
			
			
			TroubleTicket result = ticketService.close(ticketId, request);
			
			
			assertThat(result.getStatus())
					.isEqualTo(TroubleTicketStatus.CLOSED);
			verify(repository, times(1))
					.save(existingTicket);
		}
	}
}