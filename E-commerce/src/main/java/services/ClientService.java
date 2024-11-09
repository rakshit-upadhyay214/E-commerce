package services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import exceptions.AuthorizationException;
import exceptions.ClientOrSellerHasThisSameEntryException;
import exceptions.DuplicateEntryException;
import exceptions.ObjectNotFoundException;
import exceptions.UserHasProductsRelationshipsException;
import repositories.ClientRepository;
import repositories.SellerRepository;
import security.ClientSS;
import domain.dto.updated.UpdatedClient;
import domain.users.Client;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private SellerRepository sellerRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Client findById(Integer id) {

		ClientSS user = UserService.clientAuthenticated();

		if (user == null || !user.getId().equals(id)) {
			throw new AuthorizationException();
		}
		Optional<Client> obj = clientRepo.findById(id);

		try {
			return obj.get();
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}

	}

	public Client returnClientWithoutParsingTheId() {
		ClientSS user = UserService.clientAuthenticated();

		if (user == null) {
			throw new AuthorizationException();
		}

		try {
			return findById(user.getId());
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}

	}

	public List<Client> findAll() {
		return clientRepo.findAll();
	}

	@Transactional
	public Client insert(Client obj) {
		obj.setId(null);
		obj.setPassword(passwordEncoder.encode(obj.getPassword()));

		if (sellerRepo.findByEmail(obj.getEmail()) == null) {
			try {
				return clientRepo.save(obj);
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}

		throw new ClientOrSellerHasThisSameEntryException("Seller");

	}

	@Transactional
	public Client update(UpdatedClient obj) {
		ClientSS user = UserService.clientAuthenticated();

		Client cli = findById(user.getId());

		if (user == null || !user.getId().equals(cli.getId())) {
			throw new AuthorizationException();
		}

		cli.setEmail(obj.getEmail());
		cli.setName(obj.getName());
		cli.setPassword(passwordEncoder.encode(obj.getPassword()));

		if (sellerRepo.findByEmail(cli.getEmail()) == null) {
			try {
				return clientRepo.save(cli);
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}

		throw new ClientOrSellerHasThisSameEntryException("seller");

	}

	public void delete() {
		ClientSS user = UserService.clientAuthenticated();

		Client cli = findById(user.getId());

		// verify if the client hasn't bought any products
		// doing this by numberOfBuys because the performance
		if (cli.getNumberOfBuys() == 0) {
			clientRepo.deleteById(user.getId());
		}

		else {
			throw new UserHasProductsRelationshipsException();

		}

	}

}
