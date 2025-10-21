package br.com.ziro.lite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  //    @Bean
  //    public SecurityFilterChain securityFilterChain(
  //            HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
  //        http.cors()
  //                .and()
  //                .csrf()
  //                .disable()
  //                .sessionManagement()
  //                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
  //                .and()
  //                .authorizeHttpRequests(
  //                        auth ->
  // auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated());
  //
  //        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  //
  //        return http.build();
  //    }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // Remover o cors() do Spring Boot
    // http.cors();

    return http.build();
  }

  //  @Bean
  //  public CorsConfigurationSource corsConfigurationSource() {
  //    CorsConfiguration configuration = new CorsConfiguration();
  //    configuration.setAllowedOrigins(
  //        List.of("http://localhost:5173", "https://ziro-frontend.vercel.app")); // ou "*"
  //    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
  //    configuration.setAllowedHeaders(List.of("*"));
  //    configuration.setAllowCredentials(true);
  //
  //    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  //    source.registerCorsConfiguration("/**", configuration);
  //    return source;
  //  }
}
