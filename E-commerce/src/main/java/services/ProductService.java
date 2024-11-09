package services;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exceptions.AuthorizationException;
import exceptions.ObjectNotFoundException;
import exceptions.ProductHasAlreadyBeenSold;
import repositories.OrderRepository;
import repositories.ProductRepository;
import security.ClientSS;
import security.SellerSS;

import domain.Order;
import domain.Product;
import domain.dto.updated.UpdatedProduct;
import domain.users.Client;
import domain.users.Seller;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private OrderRepository orderRepo;


	@Autowired
	private ClientService clientService;



	public Product findById(Integer id) {
		Optional<Product> obj = productRepo.findById(id);

		try {
			return obj.get();
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}

	}

	@Transactional
	public Product insert(Product obj) {
		obj.setId(null);
		obj.setHasBeenSold("Unsold");
		return productRepo.save(obj);

	}

	// verify if its product owner
	@Transactional
	public Product update(UpdatedProduct obj, Integer productId) {
		SellerSS user = UserService.sellerAuthenticated();	
		Product product = findById(productId);
		
		if (Product.isSold(findById(product.getId()))) {
			throw new ProductHasAlreadyBeenSold();
		}
		product.setName(obj.getName());
		product.setDescription(obj.getDescription());
		product.setPrice(obj.getPrice());

		return productRepo.save(product);

	}

	public void delete(Integer id) {
		SellerSS user = UserService.sellerAuthenticated();
		Product obj = findById(id);

		if (Product.isSold(findById(id))) {
			throw new ProductHasAlreadyBeenSold();
		}
		productRepo.deleteById(id);

	}

	public List<Product> findAll() {

		return productRepo.findByHasBeenSold("Unsold");
	}
	


	@Transactional
	public Product buyProduct(Integer productId) {

		if (Product.isSold(findById(productId))) {
			throw new ProductHasAlreadyBeenSold();
		}

		ClientSS user = UserService.clientAuthenticated();
		Client buyer = clientService.findById(user.getId());
		Product boughtProduct = findById(productId);

		buyer.setBoughtProducts(Arrays.asList(boughtProduct));
		boughtProduct.setBuyerOfTheProduct(buyer);
		boughtProduct.setHasBeenSold("Sold");

		// method to add: number of buys and sells | money spent and sold
		addNumberOfBuysAndSellsAndMoneySoldAndSpent(boughtProduct, buyer);

		

		// Save order entity
		threadSaveOrder(boughtProduct);

		return productRepo.save(boughtProduct);

	}

	

	private void addNumberOfBuysAndSellsAndMoneySoldAndSpent(Product boughtProduct, Client buyer) {
		Seller sel = boughtProduct.getProductOwner();
		Double price = boughtProduct.getPrice();

		buyer.addNumberOfBuys();
		sel.addNumberOfSells();

		buyer.addSpentMoneyWhenClientBuyAProduct(price);
		sel.addSoldMoneyWhenSellerSellAProduct(price);

	}

	

	private void threadSaveOrder(Product product) {

		Thread threadSaveOrder = new Thread() {
			public void run() {

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				Date date = new Date(System.currentTimeMillis());
				String instant = sdf.format(date);

				Order order = new Order(null, instant, product);

				orderRepo.save(order);
			}
		};
		threadSaveOrder.start();

	}

}
