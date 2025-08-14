package br.com.ziro.lite.entity.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private Long usuarioId;
    private String email;
}
