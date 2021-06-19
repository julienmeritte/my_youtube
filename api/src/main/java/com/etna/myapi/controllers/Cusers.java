package com.etna.myapi.controllers;

import com.etna.myapi.Exception.CustomAuthenticationException;
import com.etna.myapi.Exception.CustomInvalidException;
import com.etna.myapi.Exception.CustomInvalidRegexException;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.repositories.Rusers;
import com.etna.myapi.services.Serror;
import com.etna.myapi.utils.Constants;
import com.etna.myapi.utils.Utils;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    public ResponseEntity<Object> userUpdate(@PathVariable Long id, @RequestParam(name = "username", required = false) String username, @RequestParam(name = "pseudo", required = false) String pseudo, @RequestParam(name = "email", required = false) String email, @RequestParam(name = "password", required = false) String password) throws JSONException {

        if (email != null) {
            if (Utils.isValidRegex(email, Constants.regexMail)) {
                throw new CustomInvalidRegexException(email, "email type");
            }
        } else if (password != null) {
            if (password.length() <= 5) {
                throw new CustomInvalidRegexException(password, "password type - needs to be more than 5 characters");
            }
        } else if (username != null) {
            if (Utils.isValidRegex(username, Constants.username)) {
                throw new CustomInvalidRegexException(username, "username type - Invalid");
            }
        }
        Eusers user = userService.getUserById(id);
        var jsonResponse = new JSONObject();
        if (user != null) {
            if (password != null) {
                user.setPassword(bcryptEncoder.encode(password));
            }
            if (email != null) {
                user.setEmail(email);
            }
            if (username != null) {
                user.setUsername(username);
            }
            if (pseudo != null) {
                user.setPseudo(pseudo);
            }
            if (password != null || email != null || username != null || pseudo != null) {
                userRepository.save(user);
            }
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
    public ResponseEntity<Object> getAllUsers(@RequestParam(name = "pseudo", required = false) String pseudo, @RequestParam(name = "page", required = false) String page, @RequestParam(name = "perPage", required = false) String perPage) throws JSONException {
        List<Eusers> users;
        if (pseudo != null) {
            users = userService.findUserByPseudo(pseudo);
        } else {
            users = userService.findAllUsers();
        }

        if (page == null) {
            page = "1";
        }

        if (perPage == null) {
            perPage = "5";
        }

        int pageNumber;
        int perPageNumber;

        try {
            pageNumber = Integer.parseInt(page);
            perPageNumber = Integer.parseInt(perPage);
        } catch (Exception e) {
            throw new CustomInvalidException();
        }

        var jsonResponse = userService.getAllUsers(users, pageNumber, perPageNumber);
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
