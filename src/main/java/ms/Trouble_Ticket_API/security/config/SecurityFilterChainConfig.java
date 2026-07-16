package ms.Trouble_Ticket_API.security.config;

import lombok.RequiredArgsConstructor;
import ms.Trouble_Ticket_API.exceptions.RequestAccessDeniedHandler;
import ms.Trouble_Ticket_API.exceptions.RequestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfig {
	
	private final RequestAuthenticationEntryPoint entryPoint;
	private final RequestAccessDeniedHandler accessDeniedHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/**").authenticated())
			.oauth2ResourceServer(oauth2 -> oauth2
					.jwt(jwt -> jwt.decoder(jwtDecoder))
					.authenticationEntryPoint(entryPoint)
					.accessDeniedHandler(accessDeniedHandler));
		
		return http.build();
	}
}