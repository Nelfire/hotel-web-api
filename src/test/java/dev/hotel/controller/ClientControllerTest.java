package dev.hotel.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.hotel.entite.Client;
import dev.hotel.repository.ClientRepository;

/**
 * @author 20-100
 *
 */
@WebMvcTest(WebApiCtrl.class)
public class ClientControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	ClientRepository clientRepository;

	@Test
	void testListerClients() throws Exception {
		List<Client> listMock = Arrays.asList(new Client("Paul", "BRUNET"));
		
		Mockito.when(clientRepository.findAll(PageRequest.of(0, 1))).thenReturn(new PageImpl<>(listMock));

		mockMvc.perform(MockMvcRequestBuilders.get("/clients/lister?start=0&size=1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].nom").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].nom").value("Paul"));
	}

	@Test
	void testClientByUuid() throws Exception {
		UUID uuid = UUID.fromString("91defde0-9ad3-4e4f-886b-f5f06f601a0d");
		
		Mockito.when(clientRepository.findById(uuid)).thenReturn(Optional.of(new Client("Etienne", "Joly")));

		mockMvc.perform(MockMvcRequestBuilders.get("/clients/91defde0-9ad3-4e4f-886b-f5f06f601a0d"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.nom").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("Etienne"));
	}

	//Post content type json
	

//	@Test
//	void testAjouterClient() throws Exception {
//
//		String jsonBodyTrue = "{\"name\": \"Vincent\", \"prenoms\": \"Girard\"}";
//		String jsonBodyFalse = "{\"name\": \"V\", \"prenoms\": \"Gr\"}";
//
//		mockMvc.perform(MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON).content(jsonBodyTrue))
//		.andExpect(MockMvcResultMatchers.status().isAccepted())
//		.andExpect(MockMvcResultMatchers.header().stringValues("Test passant", "C'est good"));
//		
//		mockMvc.perform(MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON).content(jsonBodyFalse))
//		.andExpect(MockMvcResultMatchers.status().isNotAcceptable())
//		.andExpect(MockMvcResultMatchers.content().string("Erreur , plus de 2 caracteres necessaire !"));
//	}
	
	
	@Test
    void testPostClient() throws Exception {
        String jsonBodyTrue = "{\"name\": \"Vincent\", \"prenoms\": \"Girard\"}";
        String jsonBodyFalse = "{\"name\": \"V\", \"prenoms\": \"Gr\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON).content(jsonBodyTrue))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.header().stringValues("verifNomPrenom", "Le Client est valide"));
        mockMvc.perform(
                MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON).content(jsonBodyFalse))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andExpect(MockMvcResultMatchers.content()
                        .string("Le Nom et Le Prenom doivent contenir plus de 2 Charactere"));
    }
}