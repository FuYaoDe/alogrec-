package org.jtb.alogrec;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

public class LogcatRecorder implements Runnable {

	private boolean running = false;
	private Writer writer;
	private File logFile;

	public LogcatRecorder(Context context, File logFile) {
		try {
			this.logFile = logFile;
			writer = new BufferedWriter(new FileWriter(logFile, true), 8192);
		} catch (IOException e) {
			Log.e("alogcat", "could not open file for writing: " + logFile);
			return;
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		running = true;

		BufferedReader br = null;
		Process p = null;

		try {
			String command = "logcat -v time";
			p = Runtime.getRuntime().exec(command);

			br = new BufferedReader(new InputStreamReader(p.getInputStream()),
					8192);

			String line;
			while (running && (line = br.readLine()) != null) {
				writer.write(line);
				writer.write("\n");
//				Log.d("file size:",""+logFile.length());

				long fileSize = 1024*1024*200;   //100MB一個檔案
				long maxSize = 1024*1024*1024*2;   //上限 2G

				if(logFile.length() >= fileSize) {

					Log.d("dir size",folderSize(LogFile.DIR)+"");
					Log.d("file size","Save:"+logFile.length());
					Log.d("file time","time:"+logFile.lastModified());

					if(folderSize(LogFile.DIR)>maxSize){
						overSize(LogFile.DIR);
					}

					LogFile a  = new LogFile();
					a.create();
					logFile =a.getFile();
					writer = new BufferedWriter(new FileWriter(logFile, true), 8192);

				}
			}
//			Log.d("save","finish");
		} catch (IOException e) {
			Log.e("alogrec", "error recording log", e);
		} finally {
			running = false;

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Log.e("alogcat", "error closing stream", e);
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					Log.e("alogcat", "error closing stream", e);
				}
			}
			if (p != null) {
				p.destroy();
			}
		}
	}

	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}

	public static void overSize(File directory) {
		File[] flies = directory.listFiles();
		long miniTime = flies[0].lastModified();
		File delFile = flies[0];
		for (File file : directory.listFiles()) {
			if (file.lastModified()<miniTime){
				delFile = file;
				miniTime = file.lastModified();
			}
		}
		delFile.delete();
	}

}
