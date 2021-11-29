package com.server.bitwit.module.security.basic;

import com.server.bitwit.domain.AccountType;
import com.server.bitwit.module.error.exception.NotFoundException;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final AccountRepository accountRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByEmailAndAccountType(username, AccountType.EMAIL)
                                .map(this::createUser)
                                .orElseThrow(( ) -> new NotFoundException("로그인 요청에 해당하는 유저를 찾을 수 없습니다"));
    }
    
    private UserDetailsPrincipal createUser(Account account) {
        var authorities = account.getAuthorities( ).stream( )
                                 .map(Authority::getAuthorityName)
                                 .map(SimpleGrantedAuthority::new)
                                 .collect(Collectors.toList( ));
        
        return new UserDetailsPrincipal(account, authorities);
    }
}
