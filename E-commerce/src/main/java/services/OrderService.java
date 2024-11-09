package services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exceptions.AuthorizationException;
import exceptions.ObjectNotFoundException;
import repositories.OrderRepository;
import security.ClientSS;

import domain.Order;
import domain.users.Client;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ClientService clientService;


	private Client findClientById(Integer id) {
		return clientService.findById(id);
	}


	public Order findById(Integer id, boolean isClient) {
		Optional<Order> obj = orderRepo.findById(id);
		return findByIdAsClient(id, obj);
	}

	public List<Order> findAll(boolean isClient) {
		return findAllAsClient();
		
	}


	private Order findByIdAsClient(Integer id, Optional<Order> obj) {
		ClientSS user = UserService.clientAuthenticated();
		Client cli = findClientById(user.getId());

		if (!cli.getOrders().contains(obj.get())) {
			throw new AuthorizationException();

		} else {

			try {
				return obj.get();

			} catch (NoSuchElementException e) {
				throw new ObjectNotFoundException();
			}
		}
	}
	
	private List<Order> findAllAsClient(){
		ClientSS user = UserService.clientAuthenticated();
		Client cli = findClientById(user.getId());

		return cli.getOrders();
	}
	
}
