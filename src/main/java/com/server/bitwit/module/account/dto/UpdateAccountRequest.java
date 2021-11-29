package com.server.bitwit.module.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    
    private String name;
    
    @Email
    private String email;
    
    @Length(min = 8, max = 20, message = "비밀번호는 8~20 자리로 설정해야 합니다.")
    private String password;
}
