package net.crudapp.appointment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.crudapp.appointment.exception.ResourceNotFoundException;
import net.crudapp.appointment.model.AppointmentModel;
import net.crudapp.appointment.repository.AppointmentRepository;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:3000/")
public class AppointmentController {
	@Autowired
	private AppointmentRepository appointmentRepository;

	// getAll
	@GetMapping("appointments")
	public List<AppointmentModel> getAllData() {
		return this.appointmentRepository.findAll();
	}

	// getOne
	@GetMapping("appointments/{id}")
	public ResponseEntity<AppointmentModel> getCrudById(@PathVariable(value = "id") Long AppointmentId)
			throws ResourceNotFoundException {
		AppointmentModel appointment = appointmentRepository.findById(AppointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + AppointmentId));
		return ResponseEntity.ok().body(appointment);
	}

	// CreateAppointment
	@PostMapping("createAppointment")
	public AppointmentModel createAppointment(@RequestBody AppointmentModel appointment) {
		return this.appointmentRepository.save(appointment);
	}

	// UpdateAppointment
	@PutMapping("/updateAppointment/{id}")
	public ResponseEntity<AppointmentModel> updateCrud(@PathVariable(value = "id") Long appointmentId,
			@Validated @RequestBody AppointmentModel appointmentDetails) throws ResourceNotFoundException {
		AppointmentModel appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + appointmentId));
		appointment.setTitle(appointmentDetails.getTitle());
		appointment.setDescription(appointmentDetails.getDescription());
		appointment.setDatetime(appointmentDetails.getDatetime());

		return ResponseEntity.ok(this.appointmentRepository.save(appointment));
	}

	// DeleteApointment
	@DeleteMapping("/deleteAppointment/{id}")
	public Map<String, Boolean> deleteCrud(@PathVariable(value = "id") Long appointmentId)
			throws ResourceNotFoundException {
		AppointmentModel crud = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + appointmentId));
		this.appointmentRepository.delete(crud);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
