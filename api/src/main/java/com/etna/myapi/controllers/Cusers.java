package com.etna.myapi.controllers;

import com.etna.myapi.Exception.CustomAuthenticationException;
import com.etna.myapi.Exception.CustomInvalidException;
import com.etna.myapi.Exception.CustomInvalidRegexException;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.repositories.Rusers;
import com.etna.myapi.services.Serror;
import com.etna.myapi.utils.Constants;
import com.etna.myapi.utils.Utils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.etna.myapi.services.Susers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class Cusers {

    @Autowired
    private Susers userService;

    @Autowired
    private Rusers userRepository;

    @Autowired
    private Serror errorService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @PostMapping("user")
    public ResponseEntity<Object> createUser(@RequestParam(name = "username") String username, @RequestParam(name = "pseudo", required = false) String pseudo, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password) throws JSONException {

        if (Utils.isValidRegex(email, Constants.regexMail)) {
            throw new CustomInvalidRegexException(email, "email type");
        } else if (password.length() <= 5) {
            throw new CustomInvalidRegexException(password, "password type - needs to be more than 5 characters");
        }

        var jsonResponse = new JSONObject();
        var user = new Eusers();

        user.setUsername(username);
        user.setEmail(email);
        user.setPseudo(pseudo);
        user.setPassword(password);

        try {
            jsonResponse = userService.createUser(user);
        } catch (Exception e) {
            return errorService.handleUserCreationError(user);
        }

        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @PostMapping("auth")
    public ResponseEntity<Object> authenticateUser(@RequestParam(name = "login") String username, @RequestParam(name = "password") String password) throws JSONException {
        String token = userService.authenticateUser(username, password);
        Eusers user = userService.findUserByUsername(username);
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "OK");
        jsonResponse.put("data", Utils.jsonifyToken(token, user));
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(201).body("");

    }

    @GetMapping("user/{id}")
    public ResponseEntity<Object> userById(@PathVariable Long id) throws JSONException {
        Eusers user = userService.getUserById(id);
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "OK");
        jsonResponse.put("data", Utils.jsonifyEuser(user));
        return ResponseEntity.status(200).body(jsonResponse.toString());
    }

    @PutMapping("user/{id}")
    public ResponseEntity<Object> userUpdate(@PathVariable Long id, @RequestParam(name = "username") String username, @RequestParam(name = "pseudo", required = false) String pseudo, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password) throws JSONException {
        if (Utils.isValidRegex(email, Constants.regexMail)) {
            throw new CustomInvalidRegexException(email, "email type");
        } else if (password.length() <= 5) {
            throw new CustomInvalidRegexException(password, "password type - needs to be more than 5 characters");
        } else if (Utils.isValidRegex(username, Constants.username)) {
            throw new CustomInvalidRegexException(username, "username type - Invalid");
        }
        Eusers user = userService.getUserById(id);
        var jsonResponse = new JSONObject();
        if (user != null) {
                user.setPassword(bcryptEncoder.encode(password));
                user.setEmail(email);
                user.setUsername(username);
                user.setPseudo(pseudo);
                userRepository.save(user);
                jsonResponse.put("message", "OK");
                jsonResponse.put("data", Utils.jsonifyEuser(user));
            return ResponseEntity.status(200).body(jsonResponse.toString());
        } else {
            jsonResponse.put("message", "OK");
            jsonResponse.put("data", "Error id doesn't exist");
            return ResponseEntity.status(400).body(jsonResponse.toString());
        }
    }

    @GetMapping("users")
    public ResponseEntity<Object> getAllUsers(@RequestParam(name = "pseudo") String pseudo, @RequestParam(name = "page", required = false) int page, @RequestParam(name = "perPage") int perPage) throws JSONException {
        var users = userService.findUserByPseudo(pseudo);
        var jsonResponse = userService.getAllUsers(users, page, perPage);
        return ResponseEntity.status(400).body(jsonResponse.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException exception) throws JSONException {
        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "Bad Request");
        jsonResponse.put("code", "10001");
        var jsonArray = new JSONArray();
        jsonArray.put(Utils.jsonifyParamMissing(exception));
        jsonResponse.put("data", jsonArray);
        return ResponseEntity.status(400).body(jsonResponse.toString());
    }
}
