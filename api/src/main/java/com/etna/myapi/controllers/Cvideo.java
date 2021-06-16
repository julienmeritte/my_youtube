package com.etna.myapi.controllers;

import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import com.etna.myapi.services.Susers;
import com.etna.myapi.services.Svideo;
import com.etna.myapi.utils.Utils;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

@CrossOrigin(origins = "*", maxAge = 3600)
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


        final var uri = "http://localhost:8081/video";

        Eusers user = userService.findByUserId(idUser);
        Evideo video = videoService.addVideo(user, name, source);

        String videoPath = System.getProperty("user.dir") + "/src/main/resources/static/" + video.getSource();
        var root = Paths.get(videoPath);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        FileSystemResource value = new FileSystemResource(new File(String.valueOf(root)));
        map.add("file", value);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);


        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "OK");
        jsonResponse.put("data", Utils.jsonifyVideo(video, user));

        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @GetMapping(value = "videos")
    public ResponseEntity<Object> getAllVideos(@RequestParam(name = "name") String name, @RequestParam(name = "user") String username, @RequestParam(name = "duration") int duration, @RequestParam(name = "page") int page, @RequestParam(name = "perPage") int perPage) throws JSONException {
        JSONObject jsonResponse = videoService.getAllVideos(name, username, duration, page, perPage);
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @GetMapping(value = "user/{iduser}/videos")
    public ResponseEntity<Object> getAllVideosByUser(@PathVariable(name = "iduser") Long idUser, @RequestParam(name = "page") int page, @RequestParam(name = "perPage") int perPage) throws JSONException {
        Eusers user = userService.findByUserId(idUser);
        var jsonResponse = videoService.getAllVideosByUser(user, page, perPage);
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @PatchMapping(value = "video/{idvideo}")
    public ResponseEntity<Object> encodeVideo(@PathVariable(name = "idvideo") Long idVideo, @RequestParam(name = "format") String format, @RequestParam(name = "file") String filename) throws JSONException {

        String videoPath = System.getProperty("user.dir") + "/src/main/resources/static/videos/1080testvideo";
        var root = Paths.get(videoPath);
        var tempFile = new File(String.valueOf(root));


        final var uri = "http://localhost:8081/video";
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        var jsonResponse = new JSONObject();
        jsonResponse.put("data", response.getBody());

        /*if (format.equals("1080") || format.equals("720") || format.equals("480") || format.equals("360") || format.equals("240") || format.equals("144")) {
             jsonResponse = videoService.encodeVideoByName(idVideo, format, filename);
        } else {
            throw new CustomFormatException("Format not recognnized", format, filename);
        }*/

        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @PutMapping(value = "video/{idvideo}")
    public ResponseEntity<Object> updateVideo(@PathVariable(name = "idvideo") Long idVideo, @RequestParam(name = "name") String name, @RequestParam(name = "user") Long idUser) throws JSONException {
        Eusers user = userService.findByUserId(idUser);
        var jsonResponse = videoService.updateVideoByUserId(user, name, idVideo);
        return ResponseEntity.status(201).body(jsonResponse.toString());
    }

    @DeleteMapping("video/{idvideo}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "idvideo") Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.status(201).body("");
    }
}
