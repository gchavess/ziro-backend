package br.com.ziro.lite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

                // =========================================
                // Comentando CORS no Spring: o Nginx vai controlar
                // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // =========================================

                // Configura autorização
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/api/auth/**")
                                        .permitAll() // endpoints de login
                                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                                        .permitAll() // permite preflight
                                        .anyRequest()
                                        .authenticated())

                // Adiciona filtro JWT antes do filtro de autenticação padrão
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =========================================
    // Comentar todo o bean de CORS, já que o Nginx está controlando
  /*
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Permite local e produção
    configuration.setAllowedOriginPatterns(
        List.of("http://localhost:*", "https://ziro-frontend.vercel.app"));

    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(
        List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "XXX-USUARIO-ID"));
    configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  */
    // =========================================

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
