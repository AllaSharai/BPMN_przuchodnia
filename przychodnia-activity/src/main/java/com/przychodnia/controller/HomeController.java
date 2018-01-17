package com.przychodnia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.przychodnia.model.User;

@Controller
public class HomeController {

	@Autowired
	RestTemplate restTemplate;

	private static final String URL = "http://80.211.246.129:8080/activiti-rest/service/runtime/process-instances";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, User user) {
		return "index";
	}

	@RequestMapping(value = "/visit/start/process", method = RequestMethod.GET)
	public String visit() {
		startProcess(URL, "process:6:16836");
		return "visit";
	}

	private void startProcess(String url, String processId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic a2VybWl0Omtlcm1pdA==");
		String requestJson = "{\"processDefinitionId\":\"" + processId + "\"}";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("processDefinitionId", processId);
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		System.out.println(response);
	}

}
