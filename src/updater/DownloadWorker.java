package updater;

import org.apache.commons.io.IOUtils;
import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by btor on 11/04/2017.
 */
public class DownloadWorker implements Runnable {
    private String       remoteURL;
    private String       localURL;
    private JProgressBar progressBar;
    private JLabel       textBar;
    private LanguageService trans  = LanguageService.getInstance();
    private ConfigService   config = ConfigService.getInstance();
    private Logger          logger = Logger.getLogger("streamanager");

    public DownloadWorker(JProgressBar progressBar, JLabel textBar) {
        this.remoteURL = config.getNewVersionURL();
        this.localURL = config.getVersionsPath() + File.separator + config.getNewVersion() + ".zip";
        this.progressBar = progressBar;
        this.textBar = textBar;
    }

    @Override
    public void run() {
        BufferedInputStream bis  = null;
        FileOutputStream    fout = null;
        try {
            URL           fileURL        = new URL(remoteURL);
            URLConnection fileConnection = fileURL.openConnection();
            bis = new BufferedInputStream(fileURL.openStream());
            fout = new FileOutputStream(localURL);

            long       fileSize  = fileConnection.getContentLength();
            final byte data[]    = new byte[1024];
            int        count;
            long       totalDone = 0;
            while ((count = bis.read(data, 0, 512)) != - 1) {
                totalDone += count;
                int percent = (int) Math.abs(((double) totalDone / fileSize) * 100);
                progressBar.setValue(percent);
                fout.write(data, 0, count);
            }
            String streamanagerFile = unzipfile(localURL, config.getVersionsPath());

            // Launch new Version
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", streamanagerFile);
            pb.start();
            config.shutdown();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fout != null) {
                try {
                    progressBar.setValue(100);
                    textBar.setText(trans.getProp("update.done"));
                    fout.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * Unzip downloaded file
     *
     * @param path
     * @param outputDirectory
     */
    private String unzipfile(String path, String outputDirectory) throws Exception {

        try {
            java.util.zip.ZipFile           zipFile = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String streamanagerFile = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry            = entries.nextElement();
                File     entryDestination = new File(outputDirectory, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    try {
                        InputStream  in  = zipFile.getInputStream(entry);
                        OutputStream out = new FileOutputStream(entryDestination);
                        streamanagerFile = entryDestination.getAbsolutePath();
                        IOUtils.copy(in, out);
                        IOUtils.closeQuietly(in);
                        out.close();
                    } catch (Exception e) {
                        throw new Exception(e);
                    }
                }
            }
            zipFile.close();

            // Remove zip file
            File zipFileToDelete = new File(path);
            zipFileToDelete.delete();

            return streamanagerFile;
        } catch (Exception e) {
            logger.severe("Can't read file for unzipping, error:" + e.getMessage());
            throw new Exception(e);
        }
    }
}
