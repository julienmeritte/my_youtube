package com.etna.myapi.Exception;

import com.etna.myapi.entity.Eusers;
import com.etna.myapi.utils.Utils;
import org.codehaus.jettison.json.JSONArray;
import org.jetbrains.annotations.NotNull;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomResourceException.class)
    public ResponseEntity<Object> handleCustomResourceException() throws JSONException {
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "Not Found");
        return ResponseEntity.status(404).body(jsonResponse.toString());
    }

    @ExceptionHandler(CustomInvalidException.class)
    public ResponseEntity<Object> handleCustomInvalidException() throws JSONException {
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "Bad Request");
        return ResponseEntity.status(400).body(jsonResponse.toString());
    }

    @ExceptionHandler(CustomInvalidRegexException.class)
    public ResponseEntity<Object> handleCustomInvalidRegexException(CustomInvalidRegexException exception) throws JSONException {
        var jsonResponse = new JSONObject();
        var jsonResponseBis = new JSONObject();
        var jsonArray = new JSONArray();
        jsonResponse.put("message", "Bad Request");
        jsonResponse.put("code", "10001");
        jsonResponseBis.put("message", "Type does not match regex.");
        jsonResponseBis.put("variable", exception.getVariable());
        jsonResponseBis.put("required type", exception.getType());
        jsonArray.put(jsonResponseBis);
        jsonResponse.put("data", jsonArray);
        return ResponseEntity.status(400).body(jsonResponse.toString());
    }

    @ExceptionHandler(CustomInvalidPagerException.class)
    public ResponseEntity<Object> handleCustomInvalidPagerException(CustomInvalidPagerException exception) throws JSONException {
        var jsonResponse = new JSONObject();
        var jsonResponseBis = new JSONObject();
        var jsonArray = new JSONArray();
        jsonResponse.put("message", "Bad Request");
        jsonResponse.put("code", "10001");
        jsonResponseBis.put("message", exception.getMessage());
        jsonResponseBis.put("page", exception.getPage());
        jsonResponseBis.put("perPage", exception.getPerPage());
        jsonArray.put(jsonResponseBis);
        jsonResponse.put("data", jsonArray);
        return ResponseEntity.status(400).body(jsonResponse.toString());
    }

    @ExceptionHandler(CustomFormatException.class)
    public ResponseEntity<Object> handleCustomFormatException(CustomFormatException exception) throws JSONException {
        var jsonResponse = new JSONObject();
        var jsonResponseBis = new JSONObject();
        var jsonArray = new JSONArray();
        jsonResponse.put("message", "Bad Request");
        jsonResponse.put("code", "10001");
        jsonResponseBis.put("message", exception.getMessage());
        jsonResponseBis.put("file", exception.getFile());
        jsonResponseBis.put("format", exception.getFormat());
        jsonArray.put(jsonResponseBis);
        jsonResponse.put("data", jsonArray);
        return ResponseEntity.status(400).body(jsonResponse.toString());
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<Object> handleCustomAuthenticationException() throws JSONException {
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "Unauthorized");
        return ResponseEntity.status(401).body(jsonResponse.toString());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(HttpServletResponse res) throws JSONException {
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "Unauthorized");
        return ResponseEntity.status(401).body(jsonResponse.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(HttpServletResponse res) throws JSONException {
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "Not Found");
        return ResponseEntity.status(404).body(jsonResponse.toString());
    }

    @NotNull
    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(@NotNull MissingServletRequestParameterException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        return ResponseEntity.status(400).body("ici 1");
    }
}
