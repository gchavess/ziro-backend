package br.com.ziro.lite.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Desabilita CSRF
        .csrf(csrf -> csrf.disable())

        // Habilita CORS usando o bean corsConfigurationSource()
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))

        // Configura autorização
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/auth/**")
                    .permitAll() // login/cadastro
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll() // preflight
                    .anyRequest()
                    .authenticated())

        // Adiciona filtro JWT antes do filtro padrão
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // Bean CORS para Spring Security
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("https://ziro-frontend.vercel.app")); // frontend Vercel
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*")); // Authorization, Content-Type etc.
    configuration.setAllowCredentials(true); // permite envio de cookies ou JWT no header

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // Bean para AuthenticationManager
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
