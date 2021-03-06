package me.anichakra.poc.random.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.anichakra.poc.random.rest.domain.Vehicle;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoRestServiceApplicationTests {

	private static String JSON_INPUT_V1 = "{\"id\":10,\"manufacturer\":\"Nissan\",\"year\":2015,\"model\":\"Ultima\"}";
	private static String JSON_INPUT_V2 = "{\"id\":12,\"manufacturer\":\"Toyota\",\"year\":2016,\"model\":\"Camri\"}";

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext context;

	@Before
	public void setup() {
		// this.mockMvc = MockMvcBuilders.standaloneSetup(new
		// WeatherApiController()).build();
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void retrievetest_ok() throws Exception {
		saveVehicle_ok();
		mockMvc.perform(get("/vehicle/10")).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(10))
				.andExpect(MockMvcResultMatchers.jsonPath("$.manufacturer").value("Nissan"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2015))
				.andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Ultima"));

	}

	@Test
	public void saveVehicle_ok() throws Exception {
		mockMvc.perform(post("/vehicle")// .andDo(print())
				.content(JSON_INPUT_V1).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		mockMvc.perform(post("/vehicle")// .andDo(print())
				.content(JSON_INPUT_V2).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteVehicle_ok() throws Exception {
		saveVehicle_ok();
		mockMvc.perform(delete("/vehicle?id=10")// .andDo(print())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

	}

	@Test
	public void searchVehicle_ok() throws Exception {
		saveVehicle_ok();
		mockMvc.perform(get("/vehicle/search?manufacturer=Nissan")).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("[" + JSON_INPUT_V1 + "]"));
	}

}
