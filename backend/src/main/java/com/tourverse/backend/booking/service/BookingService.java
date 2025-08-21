package com.tourverse.backend.booking.service;

import com.tourverse.backend.booking.dto.BookingRequest;
import com.tourverse.backend.booking.dto.BookingResponse;
import com.tourverse.backend.booking.entity.Booking;
import com.tourverse.backend.booking.repository.BookingRepository;
import com.tourverse.backend.common.exceptions.BookingException;
import com.tourverse.backend.common.exceptions.UnauthorizedActionException;
import com.tourverse.backend.common.exceptions.UserNotFoundException;
import com.tourverse.backend.guide.entity.Guide;
import com.tourverse.backend.guide.repository.GuideRepository;
import com.tourverse.backend.user.entity.Traveler;
import com.tourverse.backend.user.repository.TravelerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TravelerRepository travelerRepository;
    private final GuideRepository guideRepository;

    @Transactional
    public BookingResponse createBooking(Long travelerId, BookingRequest request) {
        Traveler traveler = travelerRepository.findById(travelerId)
                .orElseThrow(() -> new UserNotFoundException("Traveler not found with ID: " + travelerId));

        // Dynamically calculate an estimated amount based on the average rate of guides in the location.
        List<Guide> guidesInLocation = guideRepository.findByLocationAndVerificationStatus(request.getLocation(), Guide.VerificationStatus.APPROVED);
        BigDecimal estimatedAmount;

        if (!guidesInLocation.isEmpty()) {
            BigDecimal totalRate = guidesInLocation.stream()
                    .map(Guide::getRatePerHour)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal averageRate = totalRate.divide(new BigDecimal(guidesInLocation.size()), 2, RoundingMode.HALF_UP);
            estimatedAmount = averageRate.multiply(new BigDecimal(request.getHours()));
        } else {
            // Fallback to a default base rate if no guides are in that location yet.
            estimatedAmount = new BigDecimal("500.00").multiply(new BigDecimal(request.getHours()));
        }


        Booking booking = Booking.builder()
                .traveler(traveler)
                .tourDate(request.getTourDate())
                .tourTime(request.getTourTime())
                .hours(request.getHours())
                .location(request.getLocation())
                .totalAmount(estimatedAmount)
                .status(Booking.BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return convertToDto(savedBooking);
    }

	@Transactional
	public BookingResponse acceptBooking(Long guideId, Long bookingId) {
		Guide guide = guideRepository.findById(guideId)
				.orElseThrow(() -> new UserNotFoundException("Guide not found with ID: " + guideId));

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new BookingException("Booking not found with ID: " + bookingId));

		if (booking.getStatus() != Booking.BookingStatus.PENDING) {
			throw new BookingException("This booking has already been accepted or is no longer pending.");
		}

		if (!booking.getLocation().equalsIgnoreCase(guide.getLocation())) {
			throw new UnauthorizedActionException(
					"This booking is not in your location (" + guide.getLocation() + ").");
		}

		booking.setGuide(guide);
		// Update the total amount based on the accepting guide's rate
		booking.setTotalAmount(guide.getRatePerHour().multiply(new BigDecimal(booking.getHours())));
		booking.setStatus(Booking.BookingStatus.CONFIRMED);
		Booking updatedBooking = bookingRepository.save(booking);

		return convertToDto(updatedBooking);
	}

	@Transactional(readOnly = true)
	public List<BookingResponse> getBookingsForTraveler(Long travelerId) {
		return bookingRepository.findByTravelerId(travelerId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<BookingResponse> getAvailableBookingsForGuide(Long guideId) {
		Guide guide = guideRepository.findById(guideId)
				.orElseThrow(() -> new UserNotFoundException("Guide not found with ID: " + guideId));
		return bookingRepository.findByLocationAndStatus(guide.getLocation(), Booking.BookingStatus.PENDING).stream()
				.map(this::convertToDto).collect(Collectors.toList());
	}

	@Transactional
	public BookingResponse completeBooking(Long travelerId, Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new BookingException("Booking not found with ID: " + bookingId));

		if (!booking.getTraveler().getId().equals(travelerId)) {
			throw new UnauthorizedActionException("You are not authorized to complete this booking.");
		}

		if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
			throw new BookingException("Only confirmed bookings can be completed.");
		}

		booking.setStatus(Booking.BookingStatus.COMPLETED);
		return convertToDto(bookingRepository.save(booking));
	}

    private BookingResponse convertToDto(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .guideId(booking.getGuide() != null ? booking.getGuide().getId() : null)
                .guideName(booking.getGuide() != null ? booking.getGuide().getName() : null)
                .travelerId(booking.getTraveler().getId())
                .travelerName(booking.getTraveler().getName())
                .tourDate(booking.getTourDate())
                .tourTime(booking.getTourTime())
                .hours(booking.getHours())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .location(booking.getLocation())
                .build();
    }
}