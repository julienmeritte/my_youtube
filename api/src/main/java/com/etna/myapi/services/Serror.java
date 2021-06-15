package com.etna.myapi.services;

import com.etna.myapi.entity.Eusers;
import com.etna.myapi.utils.Utils;
import jdk.jshell.execution.Util;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Serror {

    public ResponseEntity<Object> handleUserCreationError(Eusers user) throws JSONException {
        var jsonItem1 = new JSONObject();
        jsonItem1.put("message", "Bad Request");
        jsonItem1.put("code", "10001");
        var jsonItem2 = new JSONObject();
        jsonItem2.put("message", "User already exists");
        jsonItem2.put("user", Utils.jsonifyEuser(user));
        var jsonArray = new JSONArray();
        jsonArray.put(jsonItem2);
        jsonItem1.put("data", jsonArray);

        return ResponseEntity.status(400).body(jsonItem1.toString());
    }
}
