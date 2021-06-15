package com.etna.myapi.services;

import com.etna.myapi.Exception.CustomInvalidException;
import com.etna.myapi.Exception.CustomInvalidPagerException;
import com.etna.myapi.Exception.CustomResourceException;
import com.etna.myapi.configurations.JwtTokenProvider;
import com.etna.myapi.entity.Erole;
import com.etna.myapi.entity.Evideo;
import com.etna.myapi.repositories.Rusers;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.utils.Utils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Susers {

    @Autowired
    private Rusers userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JSONObject createUser(Eusers user) throws JSONException {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setRoles(new ArrayList<>(Collections.singletonList(Erole.USER)));
            user.setPassword(bcryptEncoder.encode(user.getPassword()));
            Eusers createdUser = userRepository.save(user);
            var jsonReponse = new JSONObject();

            jsonReponse.put("message", "OK");
            jsonReponse.put("data", Utils.jsonifyEuser(createdUser));

            return jsonReponse;
        } else {
            throw new CustomInvalidException();
        }
    }

    public String authenticateUser(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
        } catch (AuthenticationException e) {
            throw new CustomResourceException();
        }
    }

    public Eusers findUserByUsername(String username) {
        Eusers user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomResourceException();
        }
        return user;
    }

    public List<Eusers> findUserByPseudo(String pseudo) {
        List<Eusers> user = userRepository.findAllByPseudo(pseudo);
        if (user == null) {
            throw new CustomResourceException();
        }
        return user;
    }

    public Eusers getCurrentUser(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public Eusers findByUserId(Long idUser) {
        var user = new Eusers();
        try {
            user = userRepository.findByIdUser(idUser);
        } catch (Exception e) {
            throw new CustomResourceException();
        }
        return user;
    }

    public void deleteUser(Long id) {
        Eusers user = findByUserId(id);
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new CustomResourceException();
        }
    }

    public Eusers getUserById(Long id) {
        Eusers user;
        try {
            user = userRepository.getById(id);
        } catch (Exception e) {
            throw new CustomResourceException();
        }
        return user;
    }

    public JSONObject getAllUsers(List<Eusers> users, int page, int perPage) throws JSONException {
        List<Eusers> perPageUsers = new ArrayList<>();
        int pageNumber;
        var count = 0;
        var actualPage = 1;

        pageNumber = Utils.getTotalNumberPages(users, perPage);

        if (page <= 0) {
            throw new CustomInvalidPagerException(page, perPage, "Require page number superior than 0");
        } else if (page > pageNumber) {
            throw new CustomInvalidPagerException(page, perPage, "You entered invalid pages digits, we do not have that many pages.");
        }

        for (Eusers user : users
        ) {
            if (count++ == perPage) {
                actualPage++;
            }
            if (actualPage == page) {
                perPageUsers.add(user);
            }
        }

        var jsonResponse = new JSONObject();
        var jsonArray = new JSONArray();
        jsonResponse.put("message", "OK");
        for (Eusers user : perPageUsers
        ) {
            jsonArray.put(Utils.jsonifyEuser(user));
        }
        jsonResponse.put("data", jsonArray);
        jsonResponse.put("pager", Utils.jsonifyPager(page, pageNumber));
        return jsonResponse;
    }
}