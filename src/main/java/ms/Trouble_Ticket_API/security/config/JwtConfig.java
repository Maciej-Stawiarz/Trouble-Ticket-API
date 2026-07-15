package ms.Trouble_Ticket_API.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {
	
	private final String KEY_ALGORITHM = "HmacSHA256";
	
	@Bean
	public JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret) {
		SecretKeySpec key = new SecretKeySpec(
				secret.getBytes(StandardCharsets.UTF_8),
				KEY_ALGORITHM);
		
		return NimbusJwtDecoder
				.withSecretKey(key)
				.build();
	}
}
