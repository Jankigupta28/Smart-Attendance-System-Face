package org.attend.attend_ai.attendService;

import org.attend.attend_ai.attendRepo.UserRepo;
import org.attend.attend_ai.model.EndUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
    public class UserService {
    @Autowired
    private UserRepo repo;
    @Autowired
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

   public EndUser saveUser(EndUser user) {
       user.setPassword(encoder.encode(user.getPassword()));
       return repo.save(user);
   }
}
