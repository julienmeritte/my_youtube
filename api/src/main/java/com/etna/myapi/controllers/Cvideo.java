package com.etna.myapi.controllers;

import com.etna.myapi.Exception.CustomInvalidException;
import com.etna.myapi.Exception.CustomResourceException;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import com.etna.myapi.services.Susers;
import com.etna.myapi.services.Svideo;
import com.etna.myapi.utils.Utils;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/")
public class Cvideo {

    @Autowired
    private Susers userService;

    @Autowired
    private Svideo videoService;

    @PostMapping(value = "user/{iduser}/video", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Object> createVideo(@PathVariable(name = "iduser") Long idUser, @RequestParam(name = "name") String name, @RequestPart("source") @Valid @NotNull @NotEmpty MultipartFile source) throws IOException, JSONException {

        final var uri = "http://encoder:8081/video";

        Eusers user = userService.findByUserId(idUser);
        Evideo video = videoService.addVideo(user, name, source);

        // Encoder

        String videoPath = "./app" + File.separator + "static" + File.separator + video.getSource();
        var root = Paths.get(videoPath);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        var value = new FileSystemResource(new File(String.valueOf(root)));
        map.add("file", value);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        var builder = UriComponentsBuilder.fromHttpUrl(uri);
        builder.queryParam("idvideo", video.getIdVideo());
        var restTemplate = new RestTemplate();
        restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity, String.class);

        // Mail

        var urlBis = "http://mailer:8082/video";

        Map<String, String> params = new HashMap<>();
        params.put("mail", user.getEmail());
        params.put("code", "END");

        var restTemplateBis = new RestTemplate();
        restTemplateBis.postForEntity( urlBis, params, String.class );

        // elasticsearch

        var urlSearch = "http://elasticsearch:9200/youtube/video/" + video.getIdVideo();

        Map<String, Object> mapSearch = new HashMap<>();
        mapSearch.put("name", video.getName());
        mapSearch.put("user", user.getUsername());
        mapSearch.put("date", video.getCreated_at().toString());
        mapSearch.put("id", video.getIdVideo());
        mapSearch.put("source", video.getSource());

        var restTemplateSearch = new RestTemplate();

        var headersSearch = new HttpHeaders();
        headersSearch.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mapSearch, headersSearch);

        restTemplateSearch.put( urlSearch, entity, String.class);

        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "OK");
        jsonResponse.put("data", Utils.jsonifyVideo(video, user));

        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @GetMapping(value = "videos")
    public ResponseEntity<Object> getAllVideos(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "user", required = false) String username, @RequestParam(name = "duration", required = false) String duration, @RequestParam(name = "page", required = false) String page, @RequestParam(name = "perPage", required = false) String perPage) throws JSONException {
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
        JSONObject jsonResponse = videoService.getAllVideos(name, username, duration, pageNumber, perPageNumber);
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @GetMapping(value = "user/{iduser}/videos")
    public ResponseEntity<Object> getAllVideosByUser(@PathVariable(name = "iduser") Long idUser, @RequestParam(name = "page", required = false) String page, @RequestParam(name = "perPage", required = false) String perPage) throws JSONException {
        Eusers user = userService.findByUserId(idUser);
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
        var jsonResponse = videoService.getAllVideosByUser(user, pageNumber, perPageNumber);
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @PostMapping(value = "format/{idvideo}", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Object> registerVideoFormat(@PathVariable(name = "idvideo") Long idVideo, @RequestParam(name = "format") String format, @RequestPart("source") @Valid @NotNull @NotEmpty MultipartFile source) throws JSONException, IOException {

        Evideo video = videoService.findByVideoId(idVideo);
        video = videoService.addFormat(video, format, source);
        Eusers user = video.getUser();

        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "OK");
        jsonResponse.put("data", Utils.jsonifyVideo(video, user));

        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @PutMapping(value = "video/{idvideo}")
    public ResponseEntity<Object> updateVideo(@PathVariable(name = "idvideo") Long idVideo, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "user", required = false) String idUser) throws JSONException {
        long idUserConverted;
        Eusers user = null;
        if (idUser != null) {
            try {
                idUserConverted = Long.parseLong(idUser);
            } catch (Exception e) {
                throw new CustomInvalidException();
            }
            user = userService.findByUserId(idUserConverted);
            if (user == null) {
                throw new CustomResourceException();
            }
        }

        var jsonResponse = videoService.updateVideoByUserId(user, name, idVideo);
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @DeleteMapping("video/{idvideo}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "idvideo") Long id) {
        videoService.deleteVideo(id);

        var urlSearch = "http://elasticsearch:9200/youtube/video/" + id;

        var restTemplateSearch = new RestTemplate();
        restTemplateSearch.delete(urlSearch);

        return ResponseEntity.status(201).body("");
    }
}
