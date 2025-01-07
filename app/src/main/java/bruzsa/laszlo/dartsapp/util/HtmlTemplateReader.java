package bruzsa.laszlo.dartsapp.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import lombok.Getter;

public class HtmlTemplateReader {

    public static final String WEBGUI_X_01_HTML = "webgui/webgui_x01.html";
    public static final String WEBGUI_CRICKET_HTML = "webgui/webgui_cricket.html";

    @Getter
    private final String X01Template;
    @Getter
    private final String CricketTemplate;
    private final Context context;

    public HtmlTemplateReader(Context context) {
        this.context = context;
        X01Template = getHtmlTemplate(WEBGUI_X_01_HTML);
        CricketTemplate = getHtmlTemplate(WEBGUI_CRICKET_HTML);
    }

    private String getHtmlTemplate(String assetFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(assetFile), StandardCharsets.UTF_8))) {
            return reader.lines()
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } catch (IOException ioException) {
            throw new IllegalArgumentException("File can not read", ioException);
        }
    }

}
