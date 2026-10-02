package pe.edu.upc.fixcampus.fixcampus.dtos;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank @Email
    private String correo;
    @NotBlank
    private String password;

    public LoginRequestDTO() {
    }

}
