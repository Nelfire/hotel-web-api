package dev.hotel.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hotel.entite.Client;
import dev.hotel.repository.ClientRepository;

/**
 * @author 20-100
 *
 */
@RestController
@RequestMapping("/clients")
public class WebApiCtrl {

	private ClientRepository clientRepository;

	// Injection par constructeur
	public WebApiCtrl(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	/**
	 * Créer la Web API suivante GET /clients?start=X&size=Y : • Elle retourne la
	 * liste des clients de la base de données au format JSON. • Les propriétés
	 * start et size permettent de paginer les résultats
	 */
	// http://localhost:8080/clients/lister?start=0&size=5
	@RequestMapping(method = RequestMethod.GET, path = "/lister")
	@GetMapping
	public List<Client> listeClients(@RequestParam Integer start, @RequestParam Integer size) {
		return clientRepository.findAll(PageRequest.of(start, size)).toList();
	}

	/**
	 * Créer la Web API suivante GET /clients/UUID : • Elle retourne une erreur 404
	 * si l’UUID ne correspond pas à un uuid de client en base de données • Elle
	 * retourne un code 200 avec le client trouvé au format JSON dans le corps de la
	 * réponse.
	 */
//	// http://localhost:8080/clients/UUID?uuid=dcf129f1a2f947dc82651d844244b192
//	@RequestMapping(method = RequestMethod.GET, path = "/UUID")
//	@GetMapping
//	public ResponseEntity<Client> clientByUUID(@RequestParam String uuid) {
//		StringBuffer stringBuffer = new StringBuffer(uuid);
//		stringBuffer.insert(20, '-');
//		stringBuffer.insert(16, '-');
//		stringBuffer.insert(12, '-');
//		stringBuffer.insert(8, '-');
//		Optional<Client> clt = clientRepository.findById(UUID.fromString(stringBuffer.toString()));
//		if (clt.isPresent()) {
//			return ResponseEntity.status(200).body(clt.get());
//		} else {
//			return new ResponseEntity<Client>(HttpStatus.valueOf(404));
//		}
//
//	}

	/**
	 * Créer la Web API suivante GET /clients/UUID : • Elle retourne une erreur 404
	 * si l’UUID ne correspond pas à un uuid de client en base de données • Elle
	 * retourne un code 200 avec le client trouvé au format JSON dans le corps de la
	 * réponse.
	 */
	// http://localhost:8080/clients/91defde0-9ad3-4e4f-886b-f5f06f601a0d
	@RequestMapping(method = RequestMethod.GET, path = "/{uuid}")
	public ResponseEntity<?> clientByUUID(@PathVariable UUID uuid) {
		Optional<Client> uuidClient = clientRepository.findById(uuid);
		if (uuidClient.isPresent()) {
			return ResponseEntity.status(200).body(uuidClient.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur , erreur, fuyez !");
		}

	}

	/**
	 * Créer la Web API suivante POST /clients : • Elle retourne une erreur si le
	 * nom ou le prénom ont moins de 2 caractères. • Elle retourne un code 200 avec
	 * le client créé au format JSON dans le corps de la réponse.
	 * 
	 */
//	@RequestMapping(method = RequestMethod.POST)
	@PostMapping
	public ResponseEntity<?> ajouterClient(@Valid @RequestBody Client client, BindingResult result) {
		
		if(client.getNom().length() <3 || client.getPrenoms().length() <3 ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Cas passant","Erreur , plus de 2 caracteres necessaire !").build();
		} else {
			return ResponseEntity.ok(creerClient(client.getNom(), client.getPrenoms()));
		}

	}
	
	@Transactional
	public Client creerClient(String nom, String prenoms) {

		Client c = new Client(nom, prenoms);

		clientRepository.save(c);

		return c;
	}
	
}
