package ms.Trouble_Ticket_API.exceptions.model.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public class Error {
	
	@NotBlank
	public String code;
	
	@NotBlank
	public String message;
	
	@NotBlank
	public String requestID;
}
