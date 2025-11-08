package nutar.back.security;// Create this new file in your project, e.g., under a "config" package

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Allow all requests to the H2 console
                        .requestMatchers(PathRequest.toH2Console()).permitAll()

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults()); // Use the default form login for other pages

        // Required for H2 console
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers(PathRequest.toH2Console())
        );

        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        return http.build();
    }
}