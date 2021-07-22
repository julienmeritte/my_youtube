package com.etna.myapi.utils;

import com.coremedia.iso.IsoFile;
import com.etna.myapi.Exception.CustomInvalidException;
import com.etna.myapi.entity.Ecomment;
import com.etna.myapi.entity.Eformat;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import lombok.Getter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.bind.MissingServletRequestParameterException;
import ws.schild.jave.VideoSize;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Getter
public final class Utils {

    public static String TrimTooLongLocalDateTime(LocalDateTime date) {
        return date.toString().substring(0, Math.min(date.toString().length(), 19));
    }

    public static int getMp4Duration(String videoPath) throws IOException {
        var isoFile = new IsoFile(videoPath);
        return (int) (isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                isoFile.getMovieBox().getMovieHeaderBox().getTimescale());
    }

    public static boolean isInteger(String stringToTest) {
        try {
            Integer.parseInt(stringToTest);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static JSONObject jsonifyEuser(Eusers user) throws JSONException {
        var jsonReponse = new JSONObject();
        jsonReponse.put("id", user.getIdUser());
        jsonReponse.put("username", user.getUsername());
        jsonReponse.put("pseudo", user.getPseudo());
        jsonReponse.put("email", user.getEmail());
        jsonReponse.put("created_at", TrimTooLongLocalDateTime(user.getCreated_at()));
        return jsonReponse;
    }

    public static JSONObject jsonifyToken(String token, Eusers user) throws JSONException {
        var jsonReponse = new JSONObject();
        jsonReponse.put("token", token);
        jsonReponse.put("user", jsonifyEuser(user));
        return jsonReponse;
    }

    public static JSONObject jsonifyVideo(Evideo video, Eusers user) throws JSONException {
        var jsonReponse = new JSONObject();
        jsonReponse.put("name", video.getName());
        jsonReponse.put("id", video.getIdVideo());
        jsonReponse.put("source", Paths.get(video.getSource()).toString().replace('\\', '/'));
        jsonReponse.put("image", Paths.get(video.getImage()).toString().replace('\\', '/'));
        jsonReponse.put("created_at", TrimTooLongLocalDateTime(video.getCreated_at()));
        jsonReponse.put("views", video.getView());
        jsonReponse.put("enabled", video.getEnabled());
        jsonReponse.put("user", jsonifyEuser(user));
        jsonReponse.put("duration", video.getDuration());
        jsonReponse.put("format", jsonifyFormat(video));
        return jsonReponse;
    }

    public static JSONObject jsonifyFormat(Evideo video) throws JSONException {
        var jsonReponse = new JSONObject();
        List<Eformat> formats = video.getFormats();
        for (Eformat format : formats
             ) {
            if (format.getName().startsWith("1080")) {
                jsonReponse.put("1080", Paths.get(format.getSource()).toString().replace('\\', '/'));
            } else if (format.getName().startsWith("720")) {
                jsonReponse.put("720", Paths.get(format.getSource()).toString().replace('\\', '/'));
            } else if (format.getName().startsWith("480")) {
                jsonReponse.put("480", Paths.get(format.getSource()).toString().replace('\\', '/'));
            } else if (format.getName().startsWith("360")) {
                jsonReponse.put("360", Paths.get(format.getSource()).toString().replace('\\', '/'));
            } else if (format.getName().startsWith("240")) {
                jsonReponse.put("240", Paths.get(format.getSource()).toString().replace('\\', '/'));
            } else if (format.getName().startsWith("144")) {
                jsonReponse.put("144", Paths.get(format.getSource()).toString().replace('\\', '/'));
            }
        }
        return jsonReponse;
    }

    public static JSONObject jsonifyPager(int current, int total) throws JSONException {
        var jsonReponse = new JSONObject();
        jsonReponse.put("current", current);
        jsonReponse.put("total", total);
        return jsonReponse;
    }

    public static JSONObject jsonifyComment(Ecomment comment, Eusers user) throws JSONException {
        var jsonReponse = new JSONObject();
        jsonReponse.put("id", comment.getId());
        jsonReponse.put("body", comment.getBody());
        jsonReponse.put("user", jsonifyEuser(user));
        return jsonReponse;
    }

    public static JSONObject jsonifyParamMissing(MissingServletRequestParameterException exception) throws JSONException {
        var jsonReponse = new JSONObject();
        jsonReponse.put("message", exception.getMessage());
        jsonReponse.put("required parameter type", exception.getParameterType());
        jsonReponse.put("required parameter name", exception.getParameterName());
        return jsonReponse;
    }


    public static int getTotalNumberPages(List<?> items, int perPage) {
        var count = 0;
        var pageNumber = 1;
        for (Object ignored : items
        ) {
            if (count++ == perPage) {
                pageNumber++;
            }
        }
        return pageNumber;
    }

    public static boolean isValidRegex(String text, String pattern) {
        try {
            return !Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text).find();
        } catch (Exception e) {
            throw new CustomInvalidException();
        }
    }
}
