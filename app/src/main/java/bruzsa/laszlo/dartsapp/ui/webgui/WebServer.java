package bruzsa.laszlo.dartsapp.ui.webgui;

import fi.iki.elonen.NanoHTTPD;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WebServer extends NanoHTTPD {

    public static final int WEBSERVER_DEFAULT_PORT = 9000;

    private String response;

    public WebServer() {
        super(WEBSERVER_DEFAULT_PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(response);
    }

}
