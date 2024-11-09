package resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.dto.EmailDTO;
import domain.dto.TypeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import services.AuthService;

@RestController
@Api(value = "Auth service")
@CrossOrigin
@RequestMapping
public class AuthResource {

	@Autowired
	private AuthService service;

	@PostMapping("/forgot")
	@ApiOperation(value = "send a new password to email")
	public ResponseEntity<Void> sendNewPassowrd(@RequestBody EmailDTO email) {

		service.sendNewPassword(email.getEmail());
		
		return ResponseEntity.ok().build();
	}

}
