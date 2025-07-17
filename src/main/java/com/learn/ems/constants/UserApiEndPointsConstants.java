package com.learn.ems.constants;

public final class UserApiEndPointsConstants {

    private UserApiEndPointsConstants() {}

    private static final String BASE_URL = "/api/v1/users";

    public static final String FORM_LOGIN = BASE_URL + "/login/form";
    public static final String LOGIN = BASE_URL + "/login";
    public static final String LOGOUT = BASE_URL + "/logout";

    public static final String REGISTER_ATTENDEE = BASE_URL + "/attendee/register";
    public static final String REGISTER_ADMIN = BASE_URL + "/admin/register";
    public static final String REGISTER_ORGANIZER = BASE_URL + "/organizer/register";

    public static final String GET_USER_BY_ID = BASE_URL + "/{id}";
    public static final String GET_USER_BY_EMAIL = BASE_URL + "/email/{email}";
    public static final String GET_ALL_USERS = BASE_URL + "/all";
    public static final String CHECK_USER_EXISTS_BY_EMAIL = BASE_URL + "/exists-by-email/{email}";
    public static final String UPDATE_NAME = BASE_URL + "/{id}/update-name";
    public static final String DELETE_USER_BY_ID = BASE_URL + "/{id}";
    public static final String DELETE_USER_BY_EMAIL = BASE_URL + "/email/{email}";


    public static final String UPDATE_PASSWORD = BASE_URL + "/{id}/update-password";
    public static final String FORGOT_PASSWORD = BASE_URL + "/forgot-password/{email}";
    public static final String VERIFY_EMAIL_AND_CHANGE_PASSWORD = BASE_URL + "/verify-and-change-pwd";
}
