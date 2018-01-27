package org.tetawex.ecf.desktop;

import org.tetawex.ecf.core.ActionResolver;
import org.tetawex.ecf.util.BasicListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DesktopActionResolver implements ActionResolver {

    @Override
    public void setBackPressedListener(BasicListener listener) {

    }

    @Override
    public boolean externalStorageAccessible() {
        return true;
    }

    @Override
    public void openUrl(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}