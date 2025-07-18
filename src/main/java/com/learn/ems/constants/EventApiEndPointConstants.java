package com.learn.ems.constants;

public class EventApiEndPointConstants {

    private EventApiEndPointConstants() {}

    private static final String BASE_URL = "/api/v1/events";

    public static final String CREATE_EVENT = BASE_URL + "/create";
    public static final String UPLOAD_BANNER = BASE_URL + "/{eventId}/upload-banner";
    public static final String GET_EVENT_BY_ID = BASE_URL + "/get/{eventId}";
    public static final String GET_ALL_EVENTS = BASE_URL;
    public static final String GET_EVENTS_BY_ORGANIZER = BASE_URL + "/organizer/{organizerId}";
    public static final String UPDATE_EVENT = BASE_URL + "/update/{eventId}";
    public static final String DELETE_EVENT = BASE_URL + "/delete/{eventId}";
}
