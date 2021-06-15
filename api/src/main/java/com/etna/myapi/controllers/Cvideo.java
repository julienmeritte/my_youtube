package com.etna.myapi.controllers;

import com.etna.myapi.Exception.CustomFormatException;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import com.etna.myapi.services.Susers;
import com.etna.myapi.services.Svideo;
import com.etna.myapi.utils.Utils;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Paths;

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
    public ResponseEntity<Object> setContent(@PathVariable(name = "iduser") Long idUser, @RequestParam(name = "name") String name, @RequestPart("source") @Valid @NotNull @NotEmpty MultipartFile source) throws IOException, JSONException {

        Eusers user = userService.findByUserId(idUser);
        Evideo video = videoService.addVideo(user, name, source);

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

    // TODO
    @PatchMapping(value = "video/{idvideo}")
    public ResponseEntity<Object> encodeVideo(@PathVariable(name = "idvideo") Long idVideo, @RequestParam(name = "format") String format, @RequestParam(name = "file") String filename) throws JSONException {
        var jsonResponse = new JSONObject();
        if (format.equals("1080") || format.equals("720") || format.equals("480") || format.equals("360") || format.equals("240") || format.equals("144")) {
             jsonResponse = videoService.encodeVideoByName(idVideo, format, filename);
        } else {
            throw new CustomFormatException("Format not recognnized", format, filename);
        }

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
