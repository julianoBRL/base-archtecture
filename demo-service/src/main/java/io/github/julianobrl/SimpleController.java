package io.github.julianobrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping
public class SimpleController {
	
	
	@Value("${server.port}")
	private int serverPort;
	
	@Value("${spring.application.name}")
	private String serverName;
	
	ObjectMapper mapper = new ObjectMapper();

	@GetMapping
	public String get(@RequestParam(required = false) Object params) throws JsonProcessingException {
		System.out.println("GET - RequestParam = "+mapper.writeValueAsString(params));
		return "GET on server<"+serverName+"> port<"+serverPort+">";
	}
	
	@PostMapping
	public String post(@RequestBody(required = false) Object body) throws JsonProcessingException {
		System.out.println("Post - RequestBody = "+mapper.writeValueAsString(body));
		return "POST on server<"+serverName+"> port<"+serverPort+">";
	}
	
	@DeleteMapping
	public String delete(@RequestParam(required = false) Object params) throws JsonProcessingException {
		System.out.println("Delete - RequestParam = "+mapper.writeValueAsString(params));
		return "DELETE on server<"+serverName+"> port<"+serverPort+">";
	}
	
	@PutMapping
	public String put(@RequestParam(required = false) Object params,@RequestBody(required = false) Object body) throws JsonProcessingException {
		System.out.println("Put - RequestParam = "+mapper.writeValueAsString(params));
		System.out.println("Put - RequestBody = "+mapper.writeValueAsString(body));
		return "PUT on server<"+serverName+"> port<"+serverPort+">";
	}
	
}
