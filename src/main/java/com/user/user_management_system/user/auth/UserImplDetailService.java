package com.user.user_management_system.user.auth;

import com.user.user_management_system.user.model.IUserRepository;
import com.user.user_management_system.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImplDetailService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(email)){
            throw new UsernameNotFoundException(email);
        }

        Optional<User> user = userRepository.findUserByEmailIgnoreCase(email);

        if (!user.isPresent()){
            throw new UsernameNotFoundException(email);
        }

        return new UserImpl(user.get());
    }
}
