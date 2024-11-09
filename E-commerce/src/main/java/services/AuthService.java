package services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import exceptions.ObjectNotFoundException;
import repositories.ClientRepository;
import repositories.SellerRepository;

import domain.users.Client;
import domain.users.Seller;

@Service
public class AuthService {

	private Random rand = new Random();

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private BCryptPasswordEncoder pe;


	public void sendNewPassword(String email) {

		try {
			Client cli = clientRepository.findByEmail(email);
			String newPassword = newPassword();
			cli.setPassword(pe.encode(newPassword));
			clientRepository.save(cli);
			
		} catch (NullPointerException e) {
			Seller sel = sellerRepository.findByEmail(email);

			if (sel == null) {
				throw new ObjectNotFoundException();
			}

			String newPassword = newPassword();
			sel.setPassword(pe.encode(newPassword));
			sellerRepository.save(sel);
		}

	}


	private String newPassword() {
		char[] vet = new char[6];

		for (int i = 0; i < 6; i++) {
			vet[i] = randomChar();
		}

		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);

		switch (opt) {
		case 0:

			return (char) (rand.nextInt(10) + 48);
		case 1:
			return (char) (rand.nextInt(26) + 65);
		default:
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
