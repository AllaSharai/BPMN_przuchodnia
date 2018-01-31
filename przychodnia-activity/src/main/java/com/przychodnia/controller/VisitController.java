package com.przychodnia.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.przychodnia.model.Doctor;
import com.przychodnia.model.User;
import com.przychodnia.model.Visit;
import com.przychodnia.respository.DoctorRepository;
import com.przychodnia.respository.VisitRepository;
import com.przychodnia.service.UserService;

@Controller
public class VisitController {

	@Autowired
	VisitRepository visitRepository;

	@Autowired
	UserService userService;

	@Autowired
	DoctorRepository doctorRepository;

	@RequestMapping(value = "/visit/start/process", method = RequestMethod.POST)
	public String profileForAdmin(Visit visit, Model model, User user) {
		org.springframework.security.core.userdetails.User contextUser = userService.getUserFromContext();
		String userName = contextUser.getUsername();
		User currentUser = userService.findByLogin(userName);
		// uruchom task sprawdz ubezpieczenie
		// bramka czy jest ubezpieczony
		if (!currentUser.isInsurance()) {
			/// komunikat do activity ze nie jest ubepieczony i zakonczenie
			/// procesu
			model.addAttribute("error", "uzytkownik nie jest ubezpieczony koniec procesu");
			return "index";
		}
		// przejscie tak jest uezpieczony
		Visit newVisit = new Visit();
		newVisit.setDate(visit.getDate());
		newVisit.setHour(visit.getHour());
		newVisit.setUser(currentUser);
		Random rand = new Random();
		int a = rand.nextInt(2) + 1;
		// uruchom proces który lekarz
		Doctor doctor = doctorRepository.findOne(Long.valueOf(a));
		newVisit.setDoctor(doctor);
		// bramka czy to lekarz specialista
		if (doctor.getId().equals(1L)) {
			// nie to lekarz zwykły zakończ proces powodzeniem
			visitRepository.save(newVisit);

			model.addAttribute("success",
					"udało ci się zarejestrować do leakrza (nie jest specialistą- nie trzeba skierowania) wizyta: "
							+ visit.getDate() + " o godzinie: " + visit.getHour());
			return "index";
		}
		// uruchom proces pobeirz skierowanie
		newVisit.setReferral(visit.isReferral());
		// start bramki " czy ma skierowanie"
		if (visit.isReferral()) {
			// zakoncz porces ma skierowanie udało sie
			visitRepository.save(newVisit);
			model.addAttribute("success", "udało ci się zarejestrować do leakrza wizyta: " + visit.getDate()
					+ " o godzinie: " + visit.getHour());
			return "index";
		}
		// zakoncz proces nie udało sie nei ma skierowania
		model.addAttribute("error", "uzytkownik nie ma skierowanai nie udało sie");
		return "index";
	}

}
