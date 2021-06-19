package com.etna.myapi.services;

import com.etna.myapi.Exception.CustomInvalidException;
import com.etna.myapi.Exception.CustomInvalidPagerException;
import com.etna.myapi.Exception.CustomResourceException;
import com.etna.myapi.entity.Ecomment;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import com.etna.myapi.repositories.Rcomment;
import com.etna.myapi.utils.Utils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Scomment {

    @Autowired
    private Rcomment commentRepository;

    public JSONObject createComment(Eusers user, Evideo video, String body) throws JSONException {
        var comment = new Ecomment();

        if (video == null) {
            throw new CustomResourceException();
        }

        try {
            comment.setUser(user);
            comment.setVideo(video);
            comment.setBody(body);
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CustomInvalidException();
        }

        var jsonResponse = new JSONObject();
        jsonResponse.put("message", "OK");
        jsonResponse.put("data", Utils.jsonifyComment(comment, user));

        return jsonResponse;
    }

    public JSONObject getAllCommentsByVideoId(Evideo video, int page, int perPage) throws JSONException {
        List<Ecomment> allComments = commentRepository.findAllByVideoId(video.getIdVideo());
        List<Ecomment> perPageComments = new ArrayList<>();

        int pageNumber;
        var count = 0;
        var actualPage = 1;

        pageNumber = Utils.getTotalNumberPages(allComments, perPage);

        if (page <= 0) {
            throw new CustomInvalidPagerException(page, perPage, "Require page number superior than 0");
        } else if (page > pageNumber) {
            throw new CustomInvalidPagerException(page, perPage, "You entered invalid pages digits, we do not have that many pages.");
        }

        for (Ecomment comment : allComments
        ) {
            if (count++ == perPage) {
                actualPage++;
            }
            if (actualPage == page) {
                perPageComments.add(comment);
            }
        }

        var jsonResponse = new JSONObject();
        var jsonArray = new JSONArray();
        jsonResponse.put("message", "OK");
        for (Ecomment comment : perPageComments
        ) {
            jsonArray.put(Utils.jsonifyComment(comment, comment.getUser()));
        }
        jsonResponse.put("data", jsonArray);
        jsonResponse.put("pager", Utils.jsonifyPager(page, pageNumber));

        return jsonResponse;
    }
}
