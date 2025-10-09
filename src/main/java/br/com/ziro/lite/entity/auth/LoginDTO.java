package br.com.ziro.lite.entity.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

  private String email;
  private String senha;
}
