package bruzsa.laszlo.dartsapp;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import javax.inject.Singleton;

import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiServer;
import bruzsa.laszlo.dartsapp.ui.webgui.WebServer;
import bruzsa.laszlo.dartsapp.util.HtmlTemplateReader;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class HiltModuleWebGui {

    @Provides
    @Singleton
    public HtmlTemplateReader getHtmlTemplateReader(@ApplicationContext Context context) {
        return new HtmlTemplateReader(context);
    }

    @Provides
    @Singleton
    public WebGuiServer getWebGuiServer() {
        WebServer webServer = new WebServer();
        try {
            long t = System.currentTimeMillis();
            webServer.start();
            Log.i("WebServer", "startWebserver: starting time: " + (System.currentTimeMillis() - t) + "ms");
        } catch (IOException e) {
            Log.e("WebServer", "startWebserver: WebServer can not start!", e);
        }
        return new WebGuiServer(webServer);
    }


}
