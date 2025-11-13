package br.com.ziro.lite.util.password;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordUtilTest {

  private PasswordUtil passwordUtil;

  @BeforeEach
  void setup() {
    passwordUtil = new PasswordUtil();
  }

  @Test
  void deveGerarHashCorretoParaInputConhecido() throws Exception {
    String input = "senha123";
    String expectedHash = "55a5e9e78207b4df8699d60886fa070079463547b095d1a05bc719bb4e6cd251";

    String hash = passwordUtil.hashSHA256(input);

    assertEquals(expectedHash, hash);
  }

  @Test
  void diferentesInputsDevemGerarHashesDiferentes() throws Exception {
    String input1 = "senha123";
    String input2 = "senha456";

    String hash1 = passwordUtil.hashSHA256(input1);
    String hash2 = passwordUtil.hashSHA256(input2);

    assertNotEquals(hash1, hash2);
  }

  @Test
  void mesmaSenhaSempreGeraMesmoHash() throws Exception {
    String input = "minhaSenhaSegura";

    String hash1 = passwordUtil.hashSHA256(input);
    String hash2 = passwordUtil.hashSHA256(input);

    assertEquals(hash1, hash2);
  }

  @Test
  void excecaoNaoDeveAcontecerParaSHA256() {
    assertDoesNotThrow(() -> passwordUtil.hashSHA256("teste123"));
  }
}
