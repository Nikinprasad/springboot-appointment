package net.crudapp.appointment.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.crudapp.appointment.exception.ResourceNotFoundException;
import net.crudapp.appointment.model.AppointmentModel;
import net.crudapp.appointment.payload.FilePayload;
import net.crudapp.appointment.repository.AppointmentRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/appointments/")
public class AppointmentController {
	@Autowired
	private AppointmentRepository appointmentRepository;

	// getAll
	@GetMapping("/all")
	public List<AppointmentModel> getAllData() {
		return this.appointmentRepository.findAll();
	}
	//getAllByUserId
	@GetMapping("/all/{user_id}")
	public List<AppointmentModel> getAllData(@PathVariable(value = "user_id") Long UserId) {
		return this.appointmentRepository.getUserById(UserId);
	}
	// getOne
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentModel> getCrudById(@PathVariable(value = "id") Long AppointmentId)
			throws ResourceNotFoundException {
		AppointmentModel appointment = appointmentRepository.findById(AppointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + AppointmentId));
		return ResponseEntity.ok().body(appointment);
	}

	// CreateAppointment
//	@PostMapping("create")
//	public AppointmentModel createAppointment(@RequestBody AppointmentModel appointment) {
//		return this.appointmentRepository.save(appointment);
//	}
	
	@PostMapping("create")
	public ResponseEntity<?> createAppointment(@ModelAttribute FilePayload file) throws IOException {
		AppointmentModel appointment = new AppointmentModel(file.getTitle(),file.getDescription(),file.getDatetime(),file.getUser_id(),file.getFile().getBytes(),file.getFile().getOriginalFilename());
		return ResponseEntity.ok(this.appointmentRepository.save(appointment));
	}

	// UpdateAppointment
	@PutMapping("/update/{id}")
	public ResponseEntity<AppointmentModel> updateAppointment(@PathVariable(value = "id") Long appointmentId,
			@Validated @RequestBody AppointmentModel appointmentDetails) throws ResourceNotFoundException {
		AppointmentModel appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + appointmentId));
		appointment.setTitle(appointmentDetails.getTitle());
		appointment.setDescription(appointmentDetails.getDescription());
		appointment.setDatetime(appointmentDetails.getDatetime());

		return ResponseEntity.ok(this.appointmentRepository.save(appointment));
	}

	// DeleteApointment
	@DeleteMapping("/delete/{id}")
	public Map<String, Boolean> deleteAppointment(@PathVariable(value = "id") Long appointmentId)
			throws ResourceNotFoundException {
		AppointmentModel crud = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + appointmentId));
		this.appointmentRepository.delete(crud);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
