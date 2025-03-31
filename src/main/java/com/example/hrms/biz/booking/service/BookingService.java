package com.example.hrms.biz.booking.service;

import com.example.hrms.biz.booking.model.Booking;
import com.example.hrms.biz.booking.model.criteria.BookingCriteria;
import com.example.hrms.biz.booking.model.dto.BookingDTO;
import com.example.hrms.biz.booking.repository.BookingMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.hrms.enumation.BookingType;

@Service
public class BookingService {
    private final BookingMapper bookingMapper;

    public BookingService(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    public Booking getBookingById(Long bookingId) {
        return bookingMapper.selectById(bookingId);
    }

    public void insert(BookingDTO.Req req) {
        Booking booking = req.toBooking();
        handleBookingType(booking);
        bookingMapper.insert(booking);
    }

    public void updateBooking(Booking booking) {
        handleBookingType(booking);
        bookingMapper.updateBooking(booking);
    }

    public void deleteBooking(Long bookingId) {
        bookingMapper.deleteBooking(bookingId);
    }

    public boolean isConflict(Booking booking) {
        List<Booking> conflictingBookings = bookingMapper.findConflictingBookings(
                booking.getRoomId(), booking.getStartTime(), booking.getEndTime()
        );
        return !conflictingBookings.isEmpty();
    }

    public int count(BookingCriteria criteria) {
        return bookingMapper.count(criteria);
    }

    public List<BookingDTO.Resp> list(BookingCriteria criteria) {
        List<Booking> bookings = bookingMapper.select(criteria);
        return bookings.stream().map(BookingDTO.Resp::toResponse).toList();
    }

    public List<BookingDTO.Resp> getAllBookings() {
        List<Booking> bookings = bookingMapper.selectAll();
        return bookings.stream().map(BookingDTO.Resp::toResponse).toList();
    }

    private void handleBookingType(Booking booking) {
        if (booking.getBookingType() == null) {
            booking.setBookingType(BookingType.ONLY);
        }

        switch (booking.getBookingType()) {
            case ONLY:
                booking.setStartTime(booking.getStartTime().toLocalDate().atStartOfDay());
                booking.setEndTime(null);
                booking.setWeekdays(null);
                break;
            case DAILY:
                booking.setStartTime(booking.getStartTime().toLocalDate().atStartOfDay());
                booking.setEndTime(booking.getEndTime().toLocalDate().atStartOfDay());
                booking.setWeekdays(null);
                break;
            case WEEKLY:
                booking.setStartTime(booking.getStartTime().toLocalDate().atStartOfDay());
                booking.setEndTime(booking.getEndTime().toLocalDate().atStartOfDay());
                booking.setWeekdays("Mo,Tu,We,Th,Fr");
                break;
            default:
                throw new IllegalArgumentException("Invalid booking type");
        }
    }

}
