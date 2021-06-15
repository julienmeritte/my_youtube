package com.etna.myapi.controllers;

import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import com.etna.myapi.services.Scomment;
import com.etna.myapi.services.Susers;
import com.etna.myapi.services.Svideo;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class Ccomment {

    @Autowired
    private Scomment commentService;

    @Autowired
    private Susers userService;

    @Autowired
    private Svideo videoService;

    @PostMapping("video/{id}/comment")
    public ResponseEntity<Object> createComment(@PathVariable(name = "id") Long id, @RequestParam(name = "body") String body, HttpServletRequest request) throws JSONException {
        Eusers user = userService.getCurrentUser(request);
        Evideo video = videoService.findByVideoId(id);
        return ResponseEntity.status(201).body(commentService.createComment(user, video, body).toString());
    }

    @GetMapping("video/{id}/comments")
    public ResponseEntity<Object> getCommentsFromVideo(@PathVariable(name = "id") Long id, @RequestParam(name = "page") int page, @RequestParam(name = "perPage") int perPage) throws JSONException {
        Evideo video = videoService.findByVideoId(id);
        return ResponseEntity.status(201).body(commentService.getAllCommentsByVideoId(video, page, perPage).toString());
    }
}
