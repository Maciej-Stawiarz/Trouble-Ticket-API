package ms.Trouble_Ticket_API.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.exceptions.model.dtos.Error;
import ms.Trouble_Ticket_API.exceptions.model.enums.ErrorCode;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ms.Trouble_Ticket_API.security.RequestIdFilter.MDC_KEY;

@Component
@RequiredArgsConstructor
public class RequestAccessDeniedHandler implements AccessDeniedHandler {
	
	private final ObjectMapper objectMapper;
	
	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		Error error = Error.builder()
				.code(ErrorCode.FORBIDDEN.name())
				.message("Użytkownik nie ma uprawnień do wykonania tej operacji.")
				.requestID(MDC.get(MDC_KEY))
				.build();
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8);
		
		objectMapper.writeValue(response.getWriter(), error);
	}
}