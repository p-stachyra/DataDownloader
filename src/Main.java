public class Main {
    public static void main(String[] args) {
        String url = args[0];
        String outputDirectory = args[1];
        DataDownloader downloader = new DataDownloader(url, outputDirectory);
        downloader.download();
    }
}
