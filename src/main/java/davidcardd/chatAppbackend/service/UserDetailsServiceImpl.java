//package davidcardd.chatAppbackend.service;
//
//import davidcardd.chatAppbackend.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String sessionID) throws UsernameNotFoundException {
//        User user = userService.findUserBySessionID(sessionID);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("Not found: " + sessionID);
//        }
//        return new UserDetailsImpl(user);
//    }
//}
