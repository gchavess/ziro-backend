package br.com.ziro.lite.config;

import static org.junit.jupiter.api.Assertions.*;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

class OpenApiConfigTest {

  private final OpenApiConfig config = new OpenApiConfig();

  @Test
  void openAPI_deveConfigurarSecuritySchemeEGeral() {
    OpenAPI openAPI = config.openAPI();

    // Verifica se o componente existe
    assertNotNull(openAPI.getComponents());
    assertNotNull(openAPI.getComponents().getSecuritySchemes());

    // Verifica se o security scheme "bearerAuth" existe
    SecurityScheme scheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
    assertNotNull(scheme);
    assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
    assertEquals("bearer", scheme.getScheme());
    assertEquals("JWT", scheme.getBearerFormat());

    // Verifica se existe pelo menos 1 security requirement
    assertNotNull(openAPI.getSecurity());
    assertFalse(openAPI.getSecurity().isEmpty());

    // Verifica se o security requirement global usa "bearerAuth"
    SecurityRequirement requirement = openAPI.getSecurity().get(0);
    assertTrue(requirement.containsKey("bearerAuth"));
  }
}
