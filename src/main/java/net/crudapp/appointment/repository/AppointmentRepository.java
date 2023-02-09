package net.crudapp.appointment.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.crudapp.appointment.model.AppointmentModel;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentModel, Long> {
	@Query(value= "select * from appointment WHERE user_id=:uid", nativeQuery = true)
	List<AppointmentModel> getUserById(@Param("uid") Long user_id);
}
