package br.com.ziro.lite.util.password;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
  public String hashSHA256(final String input) throws Exception {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

      return HexFormat.of().formatHex(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Erro ao gerar hash SHA-256", e);
    }
  }
}
