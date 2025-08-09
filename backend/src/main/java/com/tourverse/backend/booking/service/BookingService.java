package com.tourverse.backend.booking.service;

import com.tourverse.backend.booking.dto.BookingRequest;
import com.tourverse.backend.booking.dto.BookingResponse;
import com.tourverse.backend.booking.entity.Booking;
import com.tourverse.backend.booking.repository.BookingRepository;
import com.tourverse.backend.guide.document.AvailabilitySlot;
import com.tourverse.backend.guide.entity.Guide;
import com.tourverse.backend.guide.repository.AvailabilityRepository;
import com.tourverse.backend.guide.repository.GuideRepository;
import com.tourverse.backend.user.entity.Traveler;
import com.tourverse.backend.user.repository.TravelerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TravelerRepository travelerRepository;
    private final GuideRepository guideRepository;
    private final AvailabilityRepository availabilityRepository;

    /**
     * Creates a booking request from a traveler.
     * This method validates the guide's availability and calculates the total cost.
     *
     * @param travelerId The ID of the logged-in traveler.
     * @param request    The booking request DTO.
     * @return A DTO representing the newly created booking.
     */
    @Transactional
    public BookingResponse createBooking(Long travelerId, BookingRequest request) {
        // Step 1: Fetch the core entities
        Traveler traveler = travelerRepository.findById(travelerId)
                .orElseThrow(() -> new RuntimeException("Traveler not found"));

        Guide guide = guideRepository.findById(request.getGuideId())
                .orElseThrow(() -> new RuntimeException("Guide not found"));

        // Step 2: Validate the guide's availability
        validateGuideAvailability(guide.getId(), request.getTourDate(), request.getTourTime());

        // Step 3: Calculate the total amount
        BigDecimal totalAmount = guide.getRatePerHour().multiply(new BigDecimal(request.getHours()));

        // Step 4: Create and save the booking entity
        Booking booking = Booking.builder()
                .traveler(traveler)
                .guide(guide)
                .tourDate(request.getTourDate())
                .tourTime(request.getTourTime())
                .hours(request.getHours())
                .totalAmount(totalAmount)
                .status(Booking.BookingStatus.PENDING) // Status is PENDING until payment is complete
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        // Here, we would typically proceed to the payment flow.
        // For now, we return the created booking details.

        return convertToDto(savedBooking);
    }

    /**
     * Retrieves all bookings for a specific traveler.
     */
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsForTraveler(Long travelerId) {
        return bookingRepository.findByTravelerId(travelerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all bookings for a specific guide.
     */
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsForGuide(Long guideId) {
        return bookingRepository.findByGuideId(guideId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Allows a guide to accept a pending booking request.
     *
     * @param guideId   The ID of the currently authenticated guide.
     * @param bookingId The ID of the booking to be accepted.
     * @return A DTO of the updated, now confirmed booking.
     */
    @Transactional
    public BookingResponse acceptBooking(Long guideId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Security check: Ensure the booking belongs to the guide who is accepting it.
        if (!booking.getGuide().getId().equals(guideId)) {
            throw new IllegalStateException("You are not authorized to accept this booking.");
        }

        // Ensure the booking can only be accepted if it is currently pending.
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new IllegalStateException("This booking cannot be accepted as it is not pending.");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        Booking updatedBooking = bookingRepository.save(booking);

        // Optional: You could send an email notification to the traveler here,
        // letting them know their booking is confirmed and ready for payment.

        return convertToDto(updatedBooking);
    }

    // --- Private Helper Methods ---

    private void validateGuideAvailability(Long guideId, java.time.LocalDate date, LocalTime time) {
        AvailabilitySlot slot = availabilityRepository.findByGuideIdAndDate(guideId, date)
                .orElseThrow(() -> new RuntimeException("Guide is not available on " + date));

        if (!slot.isAvailable() || slot.getSlots() == null || !slot.getSlots().contains(time)) {
            throw new IllegalStateException("The selected time slot is not available.");
        }
    }

    private BookingResponse convertToDto(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .guideId(booking.getGuide().getId())
                .guideName(booking.getGuide().getName())
                .travelerId(booking.getTraveler().getId())
                .travelerName(booking.getTraveler().getName())
                .tourDate(booking.getTourDate())
                .tourTime(booking.getTourTime())
                .hours(booking.getHours())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .build();
    }
}