package com.learn.ems.constants;

public final class UserApiEndPointsConstants {

    private UserApiEndPointsConstants() {}

    private static final String BASE_URL = "/api/v1/users";

    // ==== Auth ====
    public static final String FORM_LOGIN = BASE_URL + "/login/form";
    public static final String LOGIN = BASE_URL + "/login";
    public static final String LOGOUT = BASE_URL + "/logout";

    // ==== Registration ====
    public static final String REGISTER_ATTENDEE = BASE_URL + "/attendee/register";
    public static final String REGISTER_ADMIN = BASE_URL + "/admin/register";
    public static final String REGISTER_ORGANIZER = BASE_URL + "/organizer/register";

    // ==== User Operations ====
    public static final String GET_USER_BY_ID = BASE_URL + "/{id}";
    public static final String GET_USER_BY_EMAIL = BASE_URL + "/email/{email}";
    public static final String GET_ALL_USERS = BASE_URL + "/all";
    public static final String UPDATE_NAME = BASE_URL + "/{id}/update-name";
    public static final String DELETE_USER_BY_ID = BASE_URL + "/{id}";
    public static final String DELETE_USER_BY_EMAIL = BASE_URL + "/email/{email}";

    // ==== Password / Recovery ====
    public static final String UPDATE_PASSWORD = BASE_URL + "/{id}/update-password";
    public static final String FORGOT_PASSWORD = BASE_URL + "/forgot-password";
    public static final String VERIFY_EMAIL_AND_RESET_PASSWORD = BASE_URL + "/verify-reset";

}
