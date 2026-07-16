package ms.Trouble_Ticket_API.trouble_ticket;

import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TicketCreationResult;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCloseStatusRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketCreateRequest;
import ms.Trouble_Ticket_API.trouble_ticket.models.dtos.TroubleTicketSummary;
import ms.Trouble_Ticket_API.trouble_ticket.models.entities.TroubleTicket;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketCloseStatus;
import ms.Trouble_Ticket_API.trouble_ticket.models.enums.TroubleTicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TroubleTicketControllerTest {
	
	private TroubleTicketController controller;
	
	@Mock
	private TroubleTicketService ticketService;
	
	
	@BeforeEach
	void init() {
		controller = new TroubleTicketController(ticketService);
	}
	
	@Test
	@DisplayName("createTroubleTicket - should return 201 Created when a new ticket is made")
	void createTroubleTicket_ShouldReturn201_WhenTicketIsNewlyCreated() {
		TroubleTicketCreateRequest request = new TroubleTicketCreateRequest();
		TroubleTicket ticket = TroubleTicket.builder()
				.id("ticket-123")
				.build();
		TicketCreationResult result = new TicketCreationResult(ticket, true);
		
		
		when(ticketService.create(request))
				.thenReturn(result);
		
		
		ResponseEntity<TroubleTicket> response = controller.createTroubleTicket(request);
		
		
		assertThat(response.getStatusCode())
				.isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody())
				.isEqualTo(ticket);
		verify(ticketService, times(1))
				.create(request);
	}
	
	@Test
	@DisplayName("createTroubleTicket - should return 200 OK when ticket already existed")
	void createTroubleTicket_ShouldReturn200_WhenTicketAlreadyExists() {
		TroubleTicketCreateRequest request = new TroubleTicketCreateRequest();
		TroubleTicket ticket = TroubleTicket.builder()
				.id("ticket-123")
				.build();
		TicketCreationResult result = new TicketCreationResult(ticket, false);
		
		when(ticketService.create(request))
				.thenReturn(result);
		
		
		ResponseEntity<TroubleTicket> response = controller.createTroubleTicket(request);
		
		
		assertThat(response.getStatusCode())
				.isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isEqualTo(ticket);
		verify(ticketService, times(1))
				.create(request);
	}
	
	@Test
	@DisplayName("listTroubleTicket - should return 200 OK and all ticket summaries")
	void listTroubleTicket_ShouldReturnSummariesAnd200() {
		List<TroubleTicketSummary> summaries = List.of(
				new TroubleTicketSummary("ext-1", 1L, "Desc 1", TroubleTicketStatus.ACKNOWLEDGED),
				new TroubleTicketSummary("ext-2", 2L, "Desc 2", TroubleTicketStatus.CLOSED)
		);
		
		when(ticketService.getAll())
				.thenReturn(summaries);
		
		
		ResponseEntity<List<TroubleTicketSummary>> response = controller.listTroubleTicket();
		
		
		assertThat(response.getStatusCode())
				.isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isEqualTo(summaries);
		verify(ticketService, times(1))
				.getAll();
	}
	
	@Test
	@DisplayName("getTroubleTicketById - should return 200 OK and requested ticket")
	void getTroubleTicketById_ShouldReturnTicketAnd200() {
		String ticketId = "ticket-123";
		TroubleTicket ticket = TroubleTicket.builder().id(ticketId).build();
		
		when(ticketService.get(ticketId))
				.thenReturn(ticket);
		
		ResponseEntity<TroubleTicket> response = controller.getTroubleTicketById(ticketId);
		

		assertThat(response.getStatusCode())
				.isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isEqualTo(ticket);
		verify(ticketService, times(1))
				.get(ticketId);
	}
	
	@Test
	@DisplayName("closeTroubleTicket - should return 200 OK and closed ticket")
	void closeTroubleTicket_ShouldReturnClosedTicketAnd200() {
		String ticketId = "ticket-123";
		TroubleTicketCloseStatusRequest request = new TroubleTicketCloseStatusRequest();
		request.status = TroubleTicketCloseStatus.CLOSED;
		
		TroubleTicket closedTicket = TroubleTicket.builder()
				.id(ticketId)
				.status(TroubleTicketStatus.CLOSED)
				.build();
		
		when(ticketService.close(ticketId, request))
				.thenReturn(closedTicket);
		
		
		ResponseEntity<TroubleTicket> response = controller.closeTroubleTicket(ticketId, request);
		
		
		assertThat(response.getStatusCode())
				.isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isEqualTo(closedTicket);
		verify(ticketService, times(1))
				.close(ticketId, request);
	}
}