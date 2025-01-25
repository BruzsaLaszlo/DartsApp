package bruzsa.laszlo.dartsapp.ui.webgui;

import static java.util.Locale.US;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.util.IpAddress;

public class WebGuiServer {

    private final WebServer webServer;
    private String host;

    @Inject
    public WebGuiServer(WebServer webServer, IpAddress ipAddress) {
        this.webServer = webServer;
        ipAddress.getIPv4Address().ifPresentOrElse(
                hostIp -> host = String.format(US, "http://%s:%d", hostIp, webServer.getListeningPort()),
                () -> host = "Can not find ip address");

    }

    public void setContent(String html) {
        webServer.setResponse(html);
    }

    public String getHostIpAddress() {
        return host;
    }
}
