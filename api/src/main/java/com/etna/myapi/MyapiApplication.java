package com.etna.myapi;

import com.etna.myapi.entity.Erole;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.services.Susers;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class MyapiApplication implements CommandLineRunner {

    @Autowired
    Susers userService;

    public static void main(String[] args) {
        SpringApplication.run(MyapiApplication.class, args);
    }

    @Override
    public void run(String... params) throws Exception {
        var admin = new Eusers();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("meritt_j@etna-alternance.net");
        admin.setRoles(new ArrayList<>(Collections.singletonList(Erole.ADMIN)));

        userService.createUser(admin);

        var client = new Eusers();
        client.setUsername("user");
        client.setPassword("user");
        client.setEmail("user@email.com");
        client.setRoles(new ArrayList<>(Collections.singletonList(Erole.USER)));

        userService.createUser(client);

        var urlSearch = "http://elasticsearch:9200/_all";

        var restTemplateSearch = new RestTemplate();
        restTemplateSearch.delete(urlSearch);

        try {
            var root = Paths.get("./app" + File.separator + "static" + File.separator + "videos" + File.separator);
            FileUtils.cleanDirectory(new File(root.toString()));
        } catch (Exception e) {

        }

    }
}
