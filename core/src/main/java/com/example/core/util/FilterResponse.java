package com.example.core.util;


import com.example.core.errors.exception.Exception401;
import com.example.core.errors.exception.Exception403;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterResponse {

    private static final ObjectMapper OBJECT_MAPPER;
    private static final String CONTENT_TYPE;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        CONTENT_TYPE = "application/json; charset=utf-8";
    }

    public static void unAuthorized(HttpServletResponse response, Exception401 exception) throws IOException {
        response.setStatus(exception.status().value());
        response.setContentType(CONTENT_TYPE);

        String responseBody = OBJECT_MAPPER.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse response, Exception403 exception) throws IOException {
        response.setStatus(exception.status().value());
        response.setContentType(CONTENT_TYPE);

        String responseBody = OBJECT_MAPPER.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }
}