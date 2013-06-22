package web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import utils.FileHelper;
import utils.GameLauncher;
import utils.OperatingSystemHelper;

/**
 * WebDownloaderAsync downloads files asyncronously using java's SwingWorker. This allows a progressbar or 
 * other UI components to be updated at the same time as the download. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class WebDownloaderAsync extends SwingWorker<Void, Void>
{
	private String[] paths;
	private JProgressBar progressBar;
	
	/**
	 * Creates a new instance of WebDownloaderAsync.
	 * @param progressBar the progressbar to update
	 * @param paths an array of all the filepaths to download
	 */
	public WebDownloaderAsync(JProgressBar progressBar, String[] paths) {
		this.paths = paths;
		this.progressBar = progressBar;
	}
	
	/**
	 * implements javax.swing.SwingWorker's doInBackground() method. First, files are downloaded; then
	 * the version of the game is updated; and then finally the game is launched.
	 */
	protected Void doInBackground() throws Exception {		
		try {
			for(int i = 0; i < paths.length; i++) {
				downloadFile(paths[i]);
				progressBar.setValue((int)((double)(i) / (paths.length - 1) * 100));
			}	
			FileHelper.copyFile(OperatingSystemHelper.getBasePath() + "/config/version_dl.txt", OperatingSystemHelper.getBasePath() + "/config/version.txt");
			GameLauncher.play();
		} catch(Exception e) {
			//Issues a failed download error if something breaks.
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					"For some reason the download failed. Perhaps something happened" + '\n' +
					"to your internet connection?", 
			 		"Failed Download",
			 		JOptionPane.OK_OPTION);
		}
		return null;		
	}
	
	/**
	 * Adds the OperatingSystem basepaths to a given filepath before downloading the file from the
	 * web. If these are already supplied, this step is not needed.
	 * @param path the filepath to add basepaths to, before downloading
	 */
	private void downloadFile(String path) {
		String onlinepath = (path.startsWith("/Resources")) ? OperatingSystemHelper.getOSIndependantOnlinePath() + path : OperatingSystemHelper.getOnlinePath() + path;
		String filepath = (path.startsWith("/Resources")) ? OperatingSystemHelper.getBasePath() + path : OperatingSystemHelper.getBasePath() + "/bin" + path;
		downloadFileFromWeb(onlinepath, filepath);	           
    }

	/**
	 * Downloads a file from the internet to disk.
	 * @param url the internet location of the file
	 * @param location the location of the file on disk
	 */
    private void downloadFileFromWeb(String url, String location) {
    	try {
 			ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());  
 			FileOutputStream fos = new FileOutputStream(location);     
 			fos.getChannel().transferFrom(rbc, 0, (1 << 24));
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 	}
}
