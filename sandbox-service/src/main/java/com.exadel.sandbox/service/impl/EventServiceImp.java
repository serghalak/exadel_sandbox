package com.exadel.sandbox.service.impl;

import com.exadel.sandbox.dto.pagelist.PageList;
import com.exadel.sandbox.dto.request.FilterRequest;
import com.exadel.sandbox.dto.response.event.EventDetailsResponse;
import com.exadel.sandbox.dto.response.event.EventResponse;
import com.exadel.sandbox.mappers.event.EventMapper;
import com.exadel.sandbox.model.vendorinfo.Event;
import com.exadel.sandbox.repository.event.EventRepository;
import com.exadel.sandbox.repository.location_repository.CityRepository;
import com.exadel.sandbox.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EventServiceImp implements EventService {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_FIELD_SORT = "name";

    private final EventRepository eventRepository;
    private final CityRepository cityRepository;
    private final EventMapper eventMapper;

    @Override
    public PageList<EventResponse> getAllEventsByUserId(Long userId, Integer pageNumber, Integer pageSize) {
        var city = cityRepository.findCityByUserId(userId);
        return getEventResponses(city.getId(), getPageNumber(pageNumber), getPageSize(pageSize));
    }

    @Override
    public PageList<EventResponse> getAllEventsByCityId(Long cityId, Integer pageNumber, Integer pageSize) {
        return getEventResponses(cityId, getPageNumber(pageNumber), getPageSize(pageSize));
    }

    @Override
    public PageList<EventResponse> getAllEventsByDescription(Long cityId, String search,
                                                             Integer pageNumber, Integer pageSize) {

        Page<Event> eventsPage = eventRepository.findEventByDescription(("%" + search + "%"),
                cityId, PageRequest.of(pageNumber, pageSize));
        return new PageList<>(eventMapper.eventListToEventResponseList(eventsPage.getContent()), eventsPage);
    }

    private PageList<EventResponse> getEventResponses(Long cityId, Integer pageNumber, Integer pageSize) {
        Page<Event> eventsPage = eventRepository.findEventByCityId(cityId, PageRequest.of(pageNumber, pageSize));

        return new PageList<>(eventMapper.eventListToEventResponseList(eventsPage.getContent()), eventsPage);

    }

    @Override
    public EventDetailsResponse getEventById(Long eventId) {

        var formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

        return Optional.ofNullable(eventRepository.findEventById(eventId))
                .map(eventMapper::eventToEventDetailResponse)
                .orElseThrow(() -> new EntityNotFoundException("entetity with id " + eventId + " not found"));
    }

    @Override
    public PageList<EventResponse> getEventsByFilter(Long userId, FilterRequest filterRequest, Integer pageNumber, Integer pageSize) {
        pageNumber = getPageNumber(pageNumber);
        pageSize = getPageSize(pageSize);
        if (filterRequest.getLocationId() == 0 &&
                filterRequest.getCategoriesIdSet().isEmpty() &&
                filterRequest.getTagsIsSet().isEmpty() &&
                filterRequest.getVendorsIdSet().isEmpty() &&
                filterRequest.getTimeFilter().isBlank()) {
            var city = cityRepository.findCityByUserId(userId);

            return getEventResponses(city.getId(), pageNumber, pageSize);
        }
        if (filterRequest.getLocationId() == 0) {
            var city = cityRepository.findCityByUserId(userId);
            filterRequest.setLocationId(city.getId());
            filterRequest.setCity(true);
        }
        if (!filterRequest.getTagsIsSet().isEmpty() &&
                filterRequest.getVendorsIdSet().isEmpty() &&
                filterRequest.getTimeFilter().isEmpty()) {

            Page<Event> eventsPage = filterRequest.isCity() ?
                    eventRepository.findByTagsByCity(filterRequest.getLocationId(),
                            filterRequest.getTagsIsSet(),
                            PageRequest.of(pageNumber, pageSize)) :
                    eventRepository.findByTagsByCountry(filterRequest.getLocationId(),
                            filterRequest.getTagsIsSet(),
                            PageRequest.of(pageNumber, pageSize));

            return new PageList<>(eventMapper.eventListToEventResponseList(eventsPage.getContent()), eventsPage);
        }

//        if (filterRequest.getTagsIsSet() != null &&
//                filterRequest.getVendorsIdSet() != null &&
//                filterRequest.getTimeFilter() == null) {
//
//            Page<Event> eventsPage = filterRequest.isCity() ?
//                    eventRepository.findByTagsByVendorsByCity(filterRequest.getLocationId(),
//                            filterRequest.getVendorsIdSet(),
//                            filterRequest.getTagsIsSet(),
//                            PageRequest.of(pageNumber, pageSize)) :
//                    eventRepository.findByTagsByVendorsByCountry(filterRequest.getLocationId(),
//                            filterRequest.getVendorsIdSet(),
//                            filterRequest.getTagsIsSet(),
//                            PageRequest.of(pageNumber, pageSize));
//
//            return new PageList<>(eventMapper.eventListToEventResponseList(eventsPage.getContent()), eventsPage);
//        }
//
//        if (filterRequest.getCategoriesIdSet() != null &&
//                filterRequest.getTagsIsSet() == null &&
//                filterRequest.getVendorsIdSet() != null &&
//                filterRequest.getTimeFilter() == null) {
//
//            Page<Event> eventsPage = filterRequest.isCity() ?
//                    eventRepository.findByTagsByVendorsByCity(filterRequest.getLocationId(),
//                            filterRequest.getVendorsIdSet(),
//                            filterRequest.getTagsIsSet(),
//                            PageRequest.of(pageNumber, pageSize)) :
//                    eventRepository.findByTagsByVendorsByCountry(filterRequest.getLocationId(),
//                            filterRequest.getVendorsIdSet(),
//                            filterRequest.getTagsIsSet(),
//                            PageRequest.of(pageNumber, pageSize));
//
//            return new PageList<>(eventMapper.eventListToEventResponseList(eventsPage.getContent()), eventsPage);
//        }


        return null;
    }

    private int getPageNumber(Integer pageNumber) {
        return pageNumber == null || pageNumber < 0 ? DEFAULT_PAGE_NUMBER : pageNumber;
    }

    private int getPageSize(Integer pageSize) {
        return pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

    }
}
