package com.spring.controller.v1.booking;

import com.spring.dto.model.BookingDTO;
import com.spring.dto.response.Response;
import com.spring.service.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	//get all booking
	@GetMapping()
	public ResponseEntity<Response<List<BookingDTO>>> getAll() {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findAll());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//get one booking by id
	@GetMapping("{id}")
	public ResponseEntity<Response<BookingDTO>> getOne(@PathVariable("id") Long id) {
		Response<BookingDTO> response = new Response<>();
		response.setData(bookingService.findById(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Response<BookingDTO>> create(@RequestBody @Valid BookingDTO bookingDTO, BindingResult bindingResult) {
		Response<BookingDTO> response = new Response<>();
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().
					forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(this.bookingService.create(bookingDTO));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public ResponseEntity<Response<BookingDTO>> update(@PathVariable("id") Long id, @RequestBody @Valid BookingDTO bookingDTO, BindingResult bindingResult) {
		Response<BookingDTO> response = new Response<>();
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().
					forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(this.bookingService.update(bookingDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// c???p nh???t status 0-dang cho 1-dat lich thanh cong 2-dat lich that bai
//	@PutMapping("/{id}/status/{status}") // id - booking
//	public ResponseEntity<Response<BookingDTO>> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
//		Response<BookingDTO> response = new Response<>();
//		response.setData(bookingService.updateStatus(id, status));
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	@PutMapping("/{id}/status/{status}/{idCustomer}") // id - booking
	public BookingDTO updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status,
			@PathVariable("idCustomer") Long idCustomer) {
		System.out.println(id + "  " + status);
		return bookingService.updateStatus(id, status, idCustomer);
	}

	// - utils
	//lay booking theo scheduletime id (check tr??ng)
	@GetMapping("/scheduleTime/{id}")
	public ResponseEntity<Response<BookingDTO>> checkScheduleTime(@PathVariable("id") Long id) {
		Response<BookingDTO> response = new Response<>();
		response.setData(bookingService.findByScheduleTime(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//l???y booking theo acc customer (ng?????i d??ng xem ?????t l???ch c???a m??nh)
	@GetMapping("/customerId/{id}")
	public ResponseEntity<Response<List<BookingDTO>>> getAllByCustomerId(@PathVariable("id") Long id) {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findByCustomerId(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//l???y booking theo acc dentist (b??c s?? xem c??ng vi???c c???a m??nh)
	@GetMapping("/dentistId/{id}")
	public ResponseEntity<Response<List<BookingDTO>>> getAllByDentistId(@PathVariable("id") Long id) {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findByDentistId(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}