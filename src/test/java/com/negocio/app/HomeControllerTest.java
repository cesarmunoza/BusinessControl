package com.negocio.app;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.negocio.app.controllers.HomeController;

@WebMvcTest(HomeController.class) //Web test for HomeController
public class HomeControllerTest {
	
	@Autowired
	private MockMvc mockMvc; //Injects MockMvc
	
	@Test
	public void testHomePage() throws Exception{
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("home"))
		.andExpect((ResultMatcher) content().string(
				containsString("Bienvenido al sistema...")));
	}

}
