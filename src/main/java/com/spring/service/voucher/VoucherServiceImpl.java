package com.spring.service.voucher;

import com.spring.dto.model.VoucherDTO;
import com.spring.exception.NotFoundException;
import com.spring.model.Voucher;
import com.spring.repository.VoucherRepository;
import com.spring.service.email.MailServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;

@Service
public class VoucherServiceImpl implements VoucherService {

	private final VoucherRepository voucherRepository;

	@Autowired
	public VoucherServiceImpl(VoucherRepository voucherRepository) {
		this.voucherRepository = voucherRepository;
	}

	@Autowired
	MailServices mailServices;

	@Override
	public VoucherDTO save(VoucherDTO dto) {

		return this.voucherRepository.save(dto.convertDTOToEntity()).convertEntityToDTO();
	}
	
	public String createVoucher(int id) {
		try {
			return id+RandomStringUtils.randomAlphanumeric(5) + new Date().getTime()
					+ RandomStringUtils.randomAlphanumeric(5);
		} catch (Exception e) {
			return null;
		}
	}
	
	public VoucherDTO sentVoucher(int id, String EmailNhan) {
		VoucherDTO v = null;
		try {
			System.out.println(voucherRepository.getCountBooking(id));
			if (voucherRepository.getCountBooking(id) == 20) {
				String maVoucher = createVoucher(id);
				System.out.println("test 1");
				v = new VoucherDTO(maVoucher, "CHI ÂN KHÁCH HÀNG JAVA", "ẢNH", 50.0, LocalDateTime.now(),
						LocalDateTime.now(), new Date(), false);
				if (save(v) != null) {
					mailServices.push(EmailNhan, v.getContent(), "Mã Voucher: " + v.getId());
				}
			}
		} catch (Exception e) {
			v = null;
		}

		return v;
	}

	@Override
	public VoucherDTO update(VoucherDTO dto) {
		return this.voucherRepository.save(dto.convertDTOToEntity()).convertEntityToDTO();
	}

	@Override
	public List<VoucherDTO> findByTitle(String title) {
		List<VoucherDTO> itemDTO = new ArrayList<>();
		this.voucherRepository.findByContent(title).forEach(t -> itemDTO.add(t.convertEntityToDTO()));
		return itemDTO;

	}

	@Override
	public Optional<VoucherDTO> findById(String id) {

		Optional<Voucher> voucher = this.voucherRepository.findById(id);
		if (voucher.isPresent()) {
			return voucher.map(Voucher::convertEntityToDTO);
		}
		return Optional.empty();
	}

	@Override
	public List<VoucherDTO> findAll() {
		List<VoucherDTO> itemDTO = new ArrayList<>();
		this.voucherRepository.findAll().forEach(t -> itemDTO.add(t.convertEntityToDTO()));
		return itemDTO;
	}

	@Override
	public List<Voucher> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
		return this.voucherRepository.findAllByStartGreaterThanEqualAndStartLessThanEqual(startDate, endDate);
	}

	@Override
	public void hardDelete(String id) throws NotFoundException {
		Voucher entity = this.voucherRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Voucher not found with :" + id));
		this.voucherRepository.delete(entity);
	}
}
