package resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.dto.updated.UpdatedClient;
import domain.users.Client;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import services.ClientService;

@RestController
@RequestMapping
@Api(value = "Client resource")
@CrossOrigin
public class ClientResource {

	@Autowired
	private ClientService service;
	
	

	@GetMapping("/clients")
	@ApiOperation(value = "Return all clients")
	public ResponseEntity<List<Client>> findAll() {

		return ResponseEntity.ok().body(service.findAll());
	}

	@ApiOperation(value = "Return your own profile as Client")
	@GetMapping("/client")
	public ResponseEntity<Client> find() {

		Client obj = service.returnClientWithoutParsingTheId();
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Create a client")
	@PostMapping("/create/client")
	public ResponseEntity<Client> insert(@RequestBody Client obj) {

		service.insert(obj);

		return ResponseEntity.ok().body(obj);
	}

	@PutMapping("/update/client")
	@ApiOperation(value = "Update a client ")
	public ResponseEntity<Client> update(@RequestBody UpdatedClient obj){

		Client cli =  service.update(obj);
		return ResponseEntity.ok().body(cli);
	}

	@DeleteMapping("/delete/client")
	@ApiOperation(value = "Delete a client")
	public ResponseEntity<Void> delete() {
		service.delete();

		return ResponseEntity.noContent().build();
	}
}
