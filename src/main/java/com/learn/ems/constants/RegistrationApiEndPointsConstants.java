package com.learn.ems.constants;

public final class RegistrationApiEndPointsConstants {

    private static final String BASE_URL = "/api/v1/events/registration";

    public static final String REGISTER_EVENT =  BASE_URL + "/register/{userId}/{eventId}";
    public static final String CANCEL_REGISTER_EVENT =  BASE_URL + "/cancel/{userId}/{eventId}";
    public static final String CHECK_IN_USER =  BASE_URL + "/checkIn/{userId}/{eventId}";
    public static final String GET_REGISTERED_EVENTS=  BASE_URL + "/get/event/{eventId}";
    public static final String GET_REGISTERED_EVENT_BY_USER =  BASE_URL + "/get/user/{userId}";
    public static final String GET_REGISTERED_EVENT_DETAILS =  BASE_URL + "/get/details/{userId}/{eventId}";


}
