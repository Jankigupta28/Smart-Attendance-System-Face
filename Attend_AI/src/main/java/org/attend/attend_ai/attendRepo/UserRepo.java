package org.attend.attend_ai.attendRepo;

import org.attend.attend_ai.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<EndUser,Integer> {


    EndUser findByEmail(String email);
}
