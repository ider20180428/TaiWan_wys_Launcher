package com.zxy.wtlauncher;

import java.io.File;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PresenterComl implements IPresenter{
	
	
	private IView iview;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				int current = msg.arg1;
				int total = msg.arg2;
				iview.updateProgress(current, total);
				break;

			default:
				break;
			}
		}
	};
	
	
	public PresenterComl(IView iview) {
		this.iview = iview;
	}	
	
	@Override
	public void installAll() {
		new Thread(install).start();
	}
	
	Runnable install = new Runnable() {
		
		@Override
		public void run() {
			Log.i("zxy", "111");
			File folder = new File("/system/preinstall1");
			File[] files = folder.listFiles();
			if(files != null) {
				Log.i("zxy", "222");
				sendMessage(0, files.length);
				for(int i = 0; i < files.length; i++) {
					InstallUtil.installSlient(files[i].getAbsolutePath());
					sendMessage(i+1, files.length);
				}	
			}
			
		}
	};
	
	private void sendMessage(int current, int total) {
		Message msg = new Message();
		msg.what = 0;
		msg.arg1 = current;
		msg.arg2 = total;
		handler.sendMessage(msg);
	}
	
}
