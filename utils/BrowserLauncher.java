package utils;

import java.awt.Desktop;
import java.net.URI;

public class BrowserLauncher {
    public static void openURL(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                // Fallback for systems where Desktop is not supported
                String os = System.getProperty("os.name").toLowerCase();
                Runtime runtime = Runtime.getRuntime();
                
                if (os.contains("win")) {
                    runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.contains("mac")) {
                    runtime.exec("open " + url);
                } else if (os.contains("nix") || os.contains("nux")) {
                    runtime.exec("xdg-open " + url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
