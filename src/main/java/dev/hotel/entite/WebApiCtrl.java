package dev.hotel.entite;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 20-100
 *
 */
@RestController
@RequestMapping("/clients")
public class WebApiCtrl {

	private ClientRepository clientRepository;

	public WebApiCtrl(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	// http://localhost:8080/clients/lister?start=0&size=5
	@RequestMapping(method = RequestMethod.GET, path = "/lister")
	@GetMapping
	public List<Client> listeClients(@RequestParam Integer start, @RequestParam Integer size) {
		return clientRepository.findAll(PageRequest.of(start, size)).toList();
	}

}
