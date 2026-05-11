package org.attend.attend_ai.attendService;

import org.attend.attend_ai.UserPrinciple;
import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.model.EndUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        EndUser user = repo.findByEmail(email);
        if(user == null){
           throw new UsernameNotFoundException("Error 404");
        }

        return new UserPrinciple(user);
    }

}
