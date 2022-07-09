import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataDownloader {

    private String resourceURL;
    private String outputDir;
    private String fileName;
    private File outputFile;

    public DataDownloader(String resourceURL, String outputDir) {
        this.resourceURL = resourceURL;
        this.outputDir = outputDir;
        this.fileName = resourceURL.substring(resourceURL.lastIndexOf("/") + 1, resourceURL.length());

        boolean endsWithSlash = outputDir.endsWith("\\");
        if (endsWithSlash) {
            this.outputFile = new File(outputDir + fileName);
        }
        else {
            this.outputFile = new File(outputDir + "\\" + fileName);
        }
    }

    public void download() {
        try {
            // create an HTTP connection
            URL url = new URL(resourceURL);
            HttpURLConnection connector = (HttpURLConnection) url.openConnection();
            // get the response code for the connection
            int responseCode = connector.getResponseCode();

            // check if the response code allows for data retrieval
            if (responseCode == HttpURLConnection.HTTP_OK) {
                double fileSize = (double) connector.getContentLengthLong();
                BufferedInputStream inpStream = new BufferedInputStream(connector.getInputStream());
                FileOutputStream fileOutStream = new FileOutputStream(this.outputFile);
                BufferedOutputStream bufferOutStream = new BufferedOutputStream(fileOutStream, 4096);
                // create the buffer array
                byte[] buffer = new byte[4096];

                // iterative process of downloading resource's contents
                int bytesRead = -1;
                // counter for progress
                double downloaded = 0.0;
                // iterate until no more bytes need to be downloaded
                while ((bytesRead = inpStream.read(buffer, 0, 4096)) != -1) {
                    bufferOutStream.write(buffer, 0, bytesRead);
                    // STDOUT progress message
                    downloaded += bytesRead;
                    double progress = downloaded / 1_000_000;
                    double total = fileSize / 1_000_000;
                    System.out.printf("Progress: %.3f MB / %.3f MB\n", progress, total);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
                // close the buffers
                bufferOutStream.close();
                inpStream.close();
            }
            connector.disconnect();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }


}