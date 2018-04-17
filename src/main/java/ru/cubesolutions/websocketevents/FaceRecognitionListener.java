package ru.cubesolutions.websocketevents;

/**
 * Created by Garya on 24.12.2017.
 */
public class FaceRecognitionListener {

    public static void main(String[] args) {
        WebSocketListener wsl = new WebSocketListener(getFullWsUrl());
    }

    private static String getFullWsUrl() {
        return String.format(Config.BASE_WS_URL + "?event_type=match&similarity_gt=0.8&auth_token=%s", Config.AUTH_TOKEN);
    }


}
