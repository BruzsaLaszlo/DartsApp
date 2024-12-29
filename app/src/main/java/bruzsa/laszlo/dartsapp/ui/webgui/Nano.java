package bruzsa.laszlo.dartsapp.ui.webgui;

import fi.iki.elonen.NanoHTTPD;
import lombok.Setter;

@Setter
public class Nano extends NanoHTTPD {

    public Nano(int port) {
        super(port);
    }

    private String response = "default response";

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(response);
    }

}
