package ms.Trouble_Ticket_API.exceptions.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class Error {
	
	@NotBlank
	public String code;
	
	@NotBlank
	public String message;
	
	@Size(min = 1)
	public String requestID;
}
