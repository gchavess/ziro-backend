package br.com.ziro.lite.service.ia;

import br.com.ziro.lite.dto.lancamento.LancamentoGraficoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiClientService {

  @Value("${gemini.api.key}")
  private String geminiApiKey;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public List<Map<String, Object>> gerarInsights(
      LancamentoGraficoDTO dadosGrafico, List<String> contexto) {
    try {
      String contextoStr = String.join("\n\n", contexto);
      String novoDadoJson = objectMapper.writeValueAsString(dadosGrafico);

      String prompt =
          """
                    Você é um analista financeiro experiente.

                    Contexto histórico:
                    %s

                    Novo dado a analisar:
                    %s

                    Instruções:
                    1. Gere múltiplos insights financeiros distintos (3 a 5) com:
                       - fato
                       - causa
                       - ação
                    2. Use apenas informações do contexto histórico ou do novo dado.
                    3. Se não houver histórico suficiente, use raciocínio próprio e marque 'inferencia': true.
                    4. Responda APENAS com JSON válido. **Não use markdown, não inclua ```json ou ```**.

                    Formato JSON esperado:
                    [
                      {
                        "fato": "...",
                        "causa": ["...", "..."],
                        "acao": ["...", "..."],
                        "inferencia": true|false
                      }
                    ]
                    """
              .formatted(contextoStr, novoDadoJson);

      Client client = Client.builder().apiKey(geminiApiKey).build();

      GenerateContentResponse response =
          client.models.generateContent("gemini-2.5-flash", prompt, null);

      String resultado = response.text();

      resultado =
          resultado
              .replaceAll("^```json\\s*", "")
              .replaceAll("^```\\s*", "")
              .replaceAll("```\\s*$", "")
              .trim();

      JsonNode parsedJson = objectMapper.readTree(resultado);
      if (!parsedJson.isArray()) {
        throw new RuntimeException("JSON retornado pelo Gemini não é um array: " + resultado);
      }

      List<Map<String, Object>> resultados = new ArrayList<>();
      for (JsonNode node : parsedJson) {
        if (node.isObject()) {
          Map<String, Object> mapa = new HashMap<>();
          mapa.put("fato", node.path("fato").asText(""));
          mapa.put("causa", objectMapper.convertValue(node.path("causa"), List.class));
          mapa.put("acao", objectMapper.convertValue(node.path("acao"), List.class));
          mapa.put("inferencia", node.path("inferencia").asBoolean(false));
          resultados.add(mapa);
        }
      }

      return resultados;

    } catch (Exception e) {
      throw new RuntimeException("Erro ao chamar Gemini: " + e.getMessage(), e);
    }
  }
}
