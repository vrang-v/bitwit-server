package com.server.bitwit.module.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmailAccountRequest {
    
    @NotBlank
    private String name;
    
    @Email
    private String email;
    
    @NotBlank
    @Length(min = 8, max = 20, message = "비밀번호는 8~20 자리로 설정해야 합니다.")
    private String password;
}
