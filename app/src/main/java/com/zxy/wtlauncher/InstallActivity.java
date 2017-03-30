package com.zxy.wtlauncher;


import com.zxy.wtlauncher.util.PreferenceManager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

public class InstallActivity extends Activity implements IView {
	
	private ProgressBar progressbar;
	private TextView textview;
	private IPresenter presenter;
	private boolean installOver = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_install);
		
		progressbar = (ProgressBar) findViewById(R.id.install_progress);
		textview = (TextView) findViewById(R.id.install_text);
		presenter = new PresenterComl(this);
		presenter.installAll();
	}
	
	
	@Override
	public void updateProgress(int current, int total) {
		progressbar.setMax(total);
		progressbar.setProgress(current);
		
		String textRes = getResources().getString(R.string.install_title);
		String progresstext = String.format(textRes, current, total);
		textview.setText(progresstext);
		
		if(current == total) {
			saveInstallState();
			installOver = true;
			sendBroadcast(new Intent(
					"ider.install.over"));
			textview.setText(R.string.install_success);
		}
	}
	
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		
		if(installOver) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			
		} else {
			return true;
		}
		
		return super.onKeyDown(arg0, arg1);
	}

	private void saveInstallState() {
		PreferenceManager	preferences =	PreferenceManager.getInstance(this);
		preferences.putBoolean("isInstall", true);
	}
	
	
}
