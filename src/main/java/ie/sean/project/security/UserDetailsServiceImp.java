package ie.sean.project.security;

import ie.sean.project.dao.UserDao;
import ie.sean.project.entities.TheUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{

        Optional<TheUser> optionalUser = userDao.findById(userName);

        if (optionalUser.isPresent()){

            TheUser theUser = optionalUser.get();

            return User.builder()
                    .username(theUser.getUserEmail())
                    .password(theUser.getUserPassword())
                    .disabled(theUser.isDisabled())
                    .accountExpired(false)
                    .accountLocked(theUser.isLocked())
                    .roles(theUser.getUserRole())
                    .build();
        }else{

            throw new UsernameNotFoundException("User name " + userName + " not found");

        }

    }

}
