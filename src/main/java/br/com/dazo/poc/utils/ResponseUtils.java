package br.com.dazo.poc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    public static final String ERROR = "error";
    public static final String ERROR_MESSAGE = "error_message";

    public static void escreveErrorResponse(HttpServletResponse response, String erroMessagem, HttpStatus httpStatus) {

        try {
            response.setHeader(ERROR, erroMessagem);
            response.setStatus(httpStatus != null ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, String> mapError = new HashMap<>();
            mapError.put(ERROR_MESSAGE, erroMessagem);

            new ObjectMapper().writeValue(response.getOutputStream(), mapError);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write error response");
        }
    }

    public static void escreveResponse(HttpServletResponse response, Map<String, String> mapToken, HttpStatus httpStatus) {

        try {
            response.setStatus(httpStatus != null ? httpStatus.value() : HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), mapToken);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write response");
        }
    }
}
