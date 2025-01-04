package bruzsa.laszlo.dartsapp.ui.webgui;

import java.io.IOException;
import java.util.concurrent.Executors;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {

    public static final int WEBSERVER_PORT = 9000;
    private static WebServer webServer;
    private String response = "Nothing to show now...";


    private WebServer() {
        super(WEBSERVER_PORT);
    }

    public static WebServer getWebServer() {
        if (webServer == null || !webServer.wasStarted()) {
            throw new IllegalStateException("WebServer was null or not started!");
        }
        return webServer;
    }

    public static void startWebserver() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                if (webServer == null) {
                    webServer = new WebServer();
                }
                if (!webServer.wasStarted())
                    webServer.start();
            } catch (IOException e) {
                throw new IllegalStateException("WebServer can not start!", e);
            }
        });
    }

    public void setWebServerContent(String html) {
        response = html;
    }


    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(response);
    }

}
