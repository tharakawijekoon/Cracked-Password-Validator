package org.soadecoder.extensions.internal;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent;
import java.nio.file.StandardWatchEventKinds;
import java.util.List;

public class DataHolder {

    private static final Log log = LogFactory.getLog(DataHolder.class);

    private static List<String> lines = null;
    private static final String PATH_FILE = "repository/deployment/server/crackedPassword/crackedPassword.txt";
    private static final String PATH= "repository/deployment/server/crackedPassword/";


    public static void watchFile() throws IOException{
        File passwords = new File(PATH_FILE);
        try
        {
            lines = FileUtils.readLines(passwords, "UTF-8");
        }
        catch (IOException e) {
            log.error("CrackedPassword File not found", e);
        }
        final Path path = FileSystems.getDefault().getPath(PATH);
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                final WatchKey wk = watchService.take();
                for (WatchEvent<?> event : wk.pollEvents()) {
                    final Path changed = (Path) event.context();
                    log.debug(changed);
                    if (changed.endsWith("crackedPassword.txt")) {
                        try
                        {
                            lines = FileUtils.readLines(passwords, "UTF-8");
                        }
                        catch (IOException e) {
                            log.error("CrackedPassword File not found",e);
                        }
                    }
                }
                // reset the key
                boolean valid = wk.reset();
                if (!valid) {
                    if (log.isDebugEnabled()) {
                        log.debug("Key has been unregistered");
                    }
                }
            }
        }
        catch(InterruptedException e){
            log.error("Watch interrupted", e);
        }
    }

    public static List<String> getLines() {
        return lines;
    }
}
