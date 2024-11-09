package services.details;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import repositories.ClientRepository;

import security.ClientSS;

import domain.users.Client;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClientRepository clientRepo;


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Client cli = clientRepo.findByEmail(email);


		ClientSS cliSS = new ClientSS();

		cliSS.setId(cli.getId());
		cliSS.setEmail(cli.getEmail());
		cliSS.setPassword(cli.getPassword());
		cliSS.setAuthorities(Arrays.asList(cli.getType()));

		return cliSS;
	}

}
