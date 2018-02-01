package com.przychodnia.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.przychodnia.model.Doctor;
import com.przychodnia.model.User;
import com.przychodnia.model.Visit;
import com.przychodnia.respository.DoctorRepository;
import com.przychodnia.respository.VisitRepository;
import com.przychodnia.service.UserService;

@Controller
public class HomeController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	VisitRepository visitRepository;

	@Autowired
	UserService userService;

	@Autowired
	DoctorRepository doctorRepository;

	private static final String URL_TASK = "http://80.211.246.129:8080/activiti-rest/service/runtime/tasks/";
	private static final String URL = "http://80.211.246.129:8080/activiti-rest/service/runtime/process-instances";
	private static final String INSTANCE_ID = "http://80.211.246.129:8080/activiti-rest/service/runtime/tasks?processInstanceId=";

	String instanceID = null;
	String teskId = null;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, User user) {
		return "index";
	}

	@RequestMapping(value = "/visit/start/process", method = RequestMethod.GET)
	public String visit(Model model) throws JSONException {
		startProcess(URL, "process:13:16918");
		model.addAttribute("visit", new Visit());
		return "visit";
	}

	@RequestMapping(value = "/visit", method = RequestMethod.GET)
	public String visitIndex(Model model) throws JSONException {
		model.addAttribute("visit", new Visit());
		return "visit";
	}

	private void startProcess(String url, String processId) throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic a2VybWl0Omtlcm1pdA==");
		String requestJson = "{\"processDefinitionId\":\"" + processId + "\"}";
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		JSONObject jsonObj = new JSONObject(response.getBody());
		instanceID = jsonObj.getString("id");
		System.out.println(response);
	}

	private void completeTask() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic a2VybWl0Omtlcm1pdA==");
		String completeActionJson = "{\"action\" : \"complete\",\"variables\" : [{\"name\" : \"input\",\"value\" : " + "2" + "}]}";
		//String jsonInString = "{\"action\":\"complete\",\"variables\":[\"name\":\"input\",\"value\":\"1\"]}";
		HttpEntity<String> entity = new HttpEntity<String>(completeActionJson, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(URL_TASK + teskId, entity, String.class);
		System.out.println(response);
	
	}

	private void completeTask(String id) {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic a2VybWl0Omtlcm1pdA==");
		String jsonInString = "{\"action\":\"complete\",\"variables\":[\"name\":\"input\",\"value\":\"1\"]}";
		HttpEntity<String> entity = new HttpEntity<String>(jsonInString, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(URL_TASK + id, entity, String.class);
		System.out.println(response);
	}

	private void getTaskID() throws JSONException {
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.add("Authorization",
		 * "Basic a2VybWl0Omtlcm1pdA==");
		 * headers.setContentType(MediaType.APPLICATION_JSON);
		 * HttpEntity<String> entity = new HttpEntity<String>(headers);
		 * ResponseEntity<String> response =
		 * restTemplate.getForEntity(INSTANCE_ID+instanceID, , String.class);
		 * JSONObject jsonObj = new JSONObject(response.getBody()); teskId =
		 * jsonObj.getString("id");
		 */
		String jsonResponse = getJsonFromGet(INSTANCE_ID + instanceID);
		JsonParser parser = new JsonParser();
		JsonObject rootObject = parser.parse(jsonResponse).getAsJsonObject();
		JsonArray dataArray = rootObject.getAsJsonArray("data");
		JsonObject dataObject = dataArray.get(0).getAsJsonObject();

		teskId = dataObject.get("id").getAsString();
	}

	@RequestMapping(value = "/visit/start/process", method = RequestMethod.POST)
	public String profileForAdmin(Visit visit, Model model, User user) throws JSONException {
		org.springframework.security.core.userdetails.User contextUser = userService.getUserFromContext();
		String userName = contextUser.getUsername();
		User currentUser = userService.findByLogin(userName);
		//task sprawdzanie ubezpieczenia
		getTaskID();
		completeTask();
		Visit newVisit = new Visit();
		newVisit.setDate(visit.getDate());
		newVisit.setHour(visit.getHour());
		newVisit.setUser(currentUser);
		Random rand = new Random();
		int a = rand.nextInt(2) + 1;
		// task ktory lekarz
		getTaskID();
		completeTask();
		Doctor doctor = doctorRepository.findOne(Long.valueOf(a));
		newVisit.setDoctor(doctor);
		newVisit.setReferral(visit.isReferral());
		// task czy pobieranie skierowania
		getTaskID();
		completeTask();
		visitRepository.save(newVisit);
		model.addAttribute("success",
				"udało ci się zarejestrować do leakrza  wizyta: "
						+ visit.getDate() + " o godzinie: " + visit.getHour());
		return "index";
	}

	public String getInstanceID() {
		return instanceID;
	}

	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}

	public String getTeskId() {
		return teskId;
	}

	public void setTeskId(String teskId) {
		this.teskId = teskId;
	}

	private String getJsonFromGet(String url) {
		try {
			HttpClient client = getAdminClient();
			HttpResponse response = client.execute(new HttpGet(url));
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String stringResponse = br.readLine();
			return stringResponse;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public HttpClient getAdminClient() {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("kermit", "kermit");
		provider.setCredentials(AuthScope.ANY, credentials);
		return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
	}

}
