package com.exadel.sandbox.service;

import com.exadel.sandbox.dto.pagelist.PageList;
import com.exadel.sandbox.dto.request.FilterRequest;
import com.exadel.sandbox.dto.response.event.CustomEventResponse;
import com.exadel.sandbox.dto.response.event.EventDetailsResponse;

public interface EventService {

    PageList<CustomEventResponse> getAllEventsByUserId(Long userId, Integer pageNumber, Integer pageSize);

    EventDetailsResponse getEventById(Long eventId);

    PageList<CustomEventResponse> getAllEventsByCityId(Long cityId, Integer pageNumber, Integer pageSize);

    PageList<CustomEventResponse> getAllEventsByDescription(Long cityId, String search, Integer pageNumber, Integer pageSize);

    PageList<CustomEventResponse> getEventsByFilter(Long userId, FilterRequest filterRequest, Integer pageNumber, Integer pageSize);

    PageList<CustomEventResponse> getAll(Integer pageNumber, Integer pageSize);
}
