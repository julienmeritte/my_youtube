package com.etna.myapi.services;

import com.etna.myapi.Exception.*;
import com.etna.myapi.entity.Eformat;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.entity.Evideo;
import com.etna.myapi.repositories.Rformat;
import com.etna.myapi.repositories.Rvideo;
import com.etna.myapi.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class Svideo {

    private final String videoPath = System.getProperty("user.dir") + "/src/main/resources/static/videos/";

    @Autowired
    private Rvideo videoRepository;

    @Autowired
    private Rformat formatRepository;

    @Autowired
    private Susers userService;


    @Transactional
    public Evideo addVideo(Eusers user, String name, MultipartFile source) throws IOException {
        var root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/videos/" + name);
        String namePath;
        File tempFile;
        String pathFile;
        for (var i = 0; true; i++) {
            if (i == 0) {
                pathFile = videoPath + "1080" + name;
                namePath = "1080" + name;
            } else {
                pathFile = videoPath + i + "_1080" + name;
                namePath = i + "_1080" + name;
            }
            root = Paths.get(pathFile);
            tempFile = new File(String.valueOf(root));
            String extension = FilenameUtils.getExtension(String.valueOf(tempFile));
            if (!extension.equals("mp4")) {
                throw new CustomInvalidRegexException(extension, "fichier d'extension mp4");
            }
            if (!tempFile.exists()) {
                Files.write(root, source.getBytes());
                break;
            }
        }
        var video = new Evideo();
        video.setName(name);
        video.setSource(root.toString());
        video.setNamePath(namePath);
        video.setEnabled(true);
        video.setUser(user);
        video.setDuration(Utils.getMp4Duration(root.toString()));
        var format = new Eformat();
        format.setVideo(video);
        format.setName("1080");
        format.setSource(root.toString());
        List<Eformat> formats = new ArrayList<>();
        formats.add(format);
        video.setFormats(formats);
        videoRepository.save(video);
        formatRepository.save(format);
        return video;
    }

    public JSONObject getAllVideos(String name, String id, int duration, int page, int perPage) throws JSONException {
        List<Evideo> allVideos = videoRepository.findAll();
        List<Evideo> filteredVideos = new ArrayList<>();
        List<Evideo> perPageVideos = new ArrayList<>();

        Eusers user;
        int pageNumber;
        var count = 0;
        var actualPage = 1;

        var idIsBoolean = Utils.isInteger(id);

        if (idIsBoolean) {
            user = userService.findByUserId((long) Integer.parseInt(id));
        } else {
            user = userService.findUserByUsername(id);
        }

        for (Evideo video : allVideos
        ) {
            if (video.getName().equals(name) && video.getDuration() == duration && video.getUser() == user) {
                filteredVideos.add(video);
            }
        }

        pageNumber = Utils.getTotalNumberPages(filteredVideos, perPage);

        if (page <= 0) {
            throw new CustomInvalidPagerException(page, perPage, "Require page number superior than 0");
        } else if (page > pageNumber) {
            throw new CustomInvalidPagerException(page, perPage, "You entered invalid pages digits, we do not have that many pages.");
        }

        for (Evideo video : filteredVideos
        ) {
            if (count++ == perPage) {
                actualPage++;
            }
            if (actualPage == page) {
                perPageVideos.add(video);
            }
        }

        var jsonResponse = new JSONObject();
        var jsonArray = new JSONArray();
        jsonResponse.put("message", "OK");
        for (Evideo video : perPageVideos
        ) {
            jsonArray.put(Utils.jsonifyVideo(video, user));
        }
        jsonResponse.put("data", jsonArray);
        jsonResponse.put("pager", Utils.jsonifyPager(page, pageNumber));
        return jsonResponse;
    }

    public JSONObject getAllVideosByUser(Eusers user, int page, int perPage) throws JSONException {
        List<Evideo> allVideos = videoRepository.findAllByUserId(user.getIdUser());
        List<Evideo> perPageVideos = new ArrayList<>();

        int pageNumber;
        var count = 0;
        var actualPage = 1;

        pageNumber = Utils.getTotalNumberPages(allVideos, perPage);

        if (page <= 0) {
            throw new CustomInvalidPagerException(page, perPage, "Require page number superior than 0");
        } else if (page > pageNumber) {
            throw new CustomInvalidPagerException(page, perPage, "You entered invalid pages digits, we do not have that many pages.");
        }

        for (Evideo video : allVideos
        ) {
            if (count++ == perPage) {
                actualPage++;
            }
            if (actualPage == page) {
                perPageVideos.add(video);
            }
        }

        var jsonResponse = new JSONObject();
        var jsonArray = new JSONArray();
        jsonResponse.put("message", "OK");
        for (Evideo video : perPageVideos
        ) {
            jsonArray.put(Utils.jsonifyVideo(video, user));
        }
        jsonResponse.put("data", jsonArray);
        jsonResponse.put("pager", Utils.jsonifyPager(page, pageNumber));
        return jsonResponse;
    }

    public JSONObject updateVideoByUserId(Eusers user, String name, Long idVideo) throws JSONException {
        Evideo video;
        var jsonResponse = new JSONObject();
        try {
            video = findByVideoId(idVideo);
            if (video != null) {
                video.setName(name);
                video.setUser(user);

                videoRepository.save(video);

                jsonResponse.put("message", "OK");
                jsonResponse.put("data", Utils.jsonifyVideo(video, user));
            } else {
                throw new CustomResourceException();
            }
        } catch (Exception e) {
            throw new CustomResourceException();
        }
        return jsonResponse;
    }

    public Evideo findByVideoId(Long idVideo) {
        Evideo video;
        try {
            video = videoRepository.getById(idVideo);
        } catch (Exception e) {
            throw new CustomResourceException();
        }
        return video;
    }

    public void deleteVideo(Long id) {
        Evideo video = findByVideoId(id);
        try {
            videoRepository.delete(video);
        } catch (Exception e) {
            throw new CustomResourceException();
        }
    }

    public JSONObject encodeVideoByName(Long idVideo, String format, String filename) throws JSONException {
        Evideo video = findByVideoId(idVideo);

        if (!video.getName().equals(filename)) {
            throw new CustomResourceException();
        }

        for (Eformat formatVideo : video.getFormats()
             ) {
            if (formatVideo.getName().equals(format)) {
                throw new CustomFormatException("Le format est déjà existant", format, filename);
            }
        }

        var eformat = new Eformat();
        eformat.setVideo(video);
        eformat.setSource(video.getSource().replace("1080", format)); //
        eformat.setName(format);
        formatRepository.save(eformat);

        video.getFormats().add(eformat);
        videoRepository.save(video);

        var root = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/videos/" + video.getNamePath());
        var source = new File(root.toString());
        var multimediaObject = new MultimediaObject(source);
        String newNamePath = video.getNamePath().replace("1080", format);
        var rootBis = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/videos/" + newNamePath);
        var target = new File(rootBis.toString());

        var videoConverted = new VideoAttributes();
        convertVideoTo(videoConverted, format, filename);

        var attrs = new EncodingAttributes();
        attrs.setVideoAttributes(videoConverted);

        try {
            var encoder = new Encoder();
            encoder.encode(multimediaObject, target, attrs);
        } catch (Exception e) {
            throw new CustomInvalidException();
        }

        var jsonObject = new JSONObject();
        jsonObject.put("message", "OK");
        jsonObject.put("data", Utils.jsonifyVideo(video, video.getUser()));

        return jsonObject;
    }

    private void convertVideoTo(VideoAttributes video, String format, String filename) {
        switch (format) {
            case "720":
                video.setBitRate(720 * 16 / 9 * 1000);
                video.setSize(new VideoSize(1280, 720));
                break;
            case "480":
                video.setBitRate(480 * 16 / 9 * 1000);
                video.setSize(new VideoSize(854, 480));
                break;
            case "360":
                video.setBitRate(360 * 16 / 9 * 1000);
                video.setSize(new VideoSize(640, 360));
                break;
            case "240":
                video.setBitRate(240 * 16 / 9 * 1000);
                video.setSize(new VideoSize(426, 240));
                break;
            case "144":
                video.setBitRate(144 * 16 / 9 * 1000);
                video.setSize(new VideoSize(256, 144));
                break;
            default:
                throw new CustomFormatException("Problème de format", format, filename);
        }
    }
}

