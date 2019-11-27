package ru.itis.jwtexample.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.jwtexample.models.User;
import ru.itis.jwtexample.repositories.UsersRepository;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		if (!usersRepository.findByLogin(login).isPresent()) {
			throw new UsernameNotFoundException("User not found with login: " + login);
		}

		User user = usersRepository.findByLogin(login).get();
		return new UserDetailsImpl(user);
	}
}