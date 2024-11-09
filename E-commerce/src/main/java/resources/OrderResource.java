package resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import services.OrderService;

@RestController
@Api(value = "Order resource")
@CrossOrigin
@RequestMapping
public class OrderResource {

	@Autowired
	private OrderService service;

	@ApiOperation(value = "Return a client order by id")
	@GetMapping("orders/customer/{id}")
	public ResponseEntity<Order> findByIdAsClient(@PathVariable Integer id) {

		// true means that the user is a client
		Order obj = service.findById(id, true);
		
		
		return ResponseEntity.ok().body(obj);
	}
	@ApiOperation(value = "Return all client orders")
	@GetMapping("getallorders")
	public ResponseEntity<List<Order>> findAllAsClient() {
		
		// true means that the user is a client
		return ResponseEntity.ok().body(service.findAll(true));
	}
	
	
}
