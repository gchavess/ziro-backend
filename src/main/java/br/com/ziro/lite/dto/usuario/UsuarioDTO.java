package br.com.ziro.lite.dto.usuario;

import br.com.ziro.lite.entity.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
  private Long id;
  private String nome;
  private String email;

  public UsuarioDTO() {}

  public UsuarioDTO(Long id, String nome, String email) {
    this.id = id;
    this.nome = nome;
    this.email = email;
  }

  public static UsuarioDTO fromEntity(Usuario usuario) {
    if (usuario == null) return null;
    return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
  }
}
