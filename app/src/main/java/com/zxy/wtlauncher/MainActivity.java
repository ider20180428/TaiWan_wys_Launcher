package com.zxy.wtlauncher;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;
import com.zxy.wtlauncher.adapter.TvGridAdapter;
import com.zxy.wtlauncher.applist.AppsActivity;
import com.zxy.wtlauncher.util.NetUtil;
import com.zxy.wtlauncher.util.PreferenceManager;
import com.zxy.wtlauncher.view.LauncherItem;
import com.zxy.wtlauncher.view.MyDialog;
import com.zxy.wtlauncher.view.MyText;
import com.zxy.wtlauncher.view.TvHorizontalGridView;
import com.zxy.wtlauncher.view.TvHorizontalGridView.OnItemClickListener;
import com.zxy.wtlauncher.view.TvMarqueeText;
import com.zxy.wtlauncher.view.TvRelativeLayoutAsGroup;
import com.zxy.wtlauncher.view.TvRelativeLayoutAsGroup.OnChildClickListener;
import com.zxy.wtlauncher.view.TvRelativeLayoutAsGroup.OnChildLongClickListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	private TextClock tc_date,tc_time;
	private LauncherItem[] items,itemsb;
	private PreferenceManager pf;
	private TvRelativeLayoutAsGroup tvrel,tvrelb;
	private LauncherItem currentItem =null;
	private TvMarqueeText mt_tip,address_tip;
	private ImageView net_icon,tf_icon,usb_icon,test;
	private Method systemProperties_get = null;
	private final String ADULT = "adult_mode";
	private final String MOVIE = "movie_mode";
	private String mode ;
	private MyText movie,adult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pf = PreferenceManager.getInstance(this);
		mode = pf.getString("select_mode",MOVIE);
        initview();
        updateData();
        registeservers();
       Log.i("zxy", "isinstall ="+pf.getBoolean("isInstall"));
        if(!pf.getBoolean("isInstall")){
        	startInstall();
        }
        checkExternalStorage();
		if (null==savedInstanceState) {
			Intent intent = new Intent("com.ider.launchermovie.MY_BROADCAST");
			sendBroadcast(intent);
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle out){
		super.onSaveInstanceState(out);
		String tem = "save something";
		out.putString("key",tem);
	}
	
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
	private void initview() {
		movie = (MyText)findViewById(R.id.movie_text);
		adult = (MyText)findViewById(R.id.adult_text);
		tc_date = (TextClock) findViewById(R.id.tc_date);
		tc_date.setFormat12Hour("yyyy-MM-dd");
		tc_date.setFormat24Hour("yyyy-MM-dd");
		tc_time =(TextClock) findViewById(R.id.time_tc);
		tc_time.setFormat12Hour("h:mm");
		tc_time.setFormat24Hour("h:mm");
		items = new LauncherItem[10];
		itemsb = new LauncherItem[2];
		tvrel =(TvRelativeLayoutAsGroup) findViewById(R.id.launcher_items);
		tvrelb =(TvRelativeLayoutAsGroup) findViewById(R.id.launcher_itemsb);
		itemsb[0] = (LauncherItem) findViewById(R.id.launcher_item_b1);
		itemsb[1] = (LauncherItem) findViewById(R.id.launcher_item_b2);
		itemsb[0].setIcon(R.drawable.btn02);
		itemsb[1].setIcon(R.drawable.btn01);
		items[0] = (LauncherItem) findViewById(R.id.launcher_item1);
		items[1] = (LauncherItem) findViewById(R.id.launcher_item2);
		items[2] = (LauncherItem) findViewById(R.id.launcher_item3);
		items[3] = (LauncherItem) findViewById(R.id.launcher_item4);
		items[4] = (LauncherItem) findViewById(R.id.launcher_item5);
		items[5] = (LauncherItem) findViewById(R.id.launcher_item6);
		items[6] = (LauncherItem) findViewById(R.id.launcher_item7);
		items[7] = (LauncherItem) findViewById(R.id.launcher_item8);
		items[8] = (LauncherItem) findViewById(R.id.launcher_item9);
		items[9] = (LauncherItem) findViewById(R.id.launcher_item10);
		//mt_tip = (TvMarqueeText) findViewById(R.id.mt_tip);
		//mt_tip.startMarquee();
		address_tip = (TvMarqueeText)findViewById(R.id.address_tip);
		address_tip.startMarquee();
		net_icon = (ImageView) findViewById(R.id.net_icon);
		tf_icon = (ImageView) findViewById(R.id.tf_icon);
		usb_icon = (ImageView) findViewById(R.id.usb_icon);
		tvrel.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public void onChildClick(View child) {
				LauncherItem item = (LauncherItem) child;
				Application app = item.getApp();
				
				if(app!=null){
					if(!app.getPackageName().equals("app.add")){
						if (NetUtil.isNetworkAvailable(MainActivity.this)) {
							Application.startApp(getApplicationContext(), app);
						}else {
							Toast.makeText(MainActivity.this, getString(R.string.network_inposiable) , Toast.LENGTH_SHORT).show();
						}
					}else{
						//add
						currentItem = item;
						showAddDialog(getlocalApps(),MainActivity.this);
					}
				}
			}
		});
		tvrel.setOnLongClickListener(new OnChildLongClickListener() {

			@Override
			public void onChildLongClick(View child) {
				LauncherItem item = (LauncherItem) child;
				item.showApplication(Application.doAddApplication(getApplicationContext()));
				pf.delete(item.getTag().toString());
			}
		});
		tvrelb.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public void onChildClick(View child) {
				LauncherItem item = (LauncherItem) child;
				Application app = item.getApp();

				if(app!=null){
					if(!app.getPackageName().equals("app.add")){
						if (NetUtil.isNetworkAvailable(MainActivity.this)) {
							Application.startApp(getApplicationContext(), app);
						}else {
							Toast.makeText(MainActivity.this, getString(R.string.network_inposiable) , Toast.LENGTH_SHORT).show();
						}
					}else{
						//add
						currentItem = item;
						showAddDialog(getlocalApps(),MainActivity.this);
					}
				}
			}
		});
		movie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				movie.setSelected(true);
				adult.setSelected(false);
				mode = MOVIE;
				pf.putString("select_mode",MOVIE);
				tvrel.setVisibility(View.VISIBLE);
				tvrelb.setVisibility(View.GONE);
			}
		});
		adult.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				adult.setSelected(true);
				movie.setSelected(false);
				mode = ADULT;
				pf.putString("select_mode",ADULT);
				tvrel.setVisibility(View.GONE);
				tvrelb.setVisibility(View.VISIBLE);
			}
		});
		if (mode.equals(MOVIE)){
			movie.setSelected(true);
			adult.setSelected(false);
			tvrel.setVisibility(View.VISIBLE);
			tvrelb.setVisibility(View.GONE);
		}else {
			movie.setSelected(false);
			adult.setSelected(true);
			tvrel.setVisibility(View.GONE);
			tvrelb.setVisibility(View.VISIBLE);
		}

	}


	public void startInstall(){
		Intent intent = new Intent(this, InstallActivity.class);
    	startActivity(intent);
	}
	
	
	protected void showAddDialog(List<String> getlocalApps,Context context) {
		final List<Application> apps = Application.loadAllApplication(context, getlocalApps);
		final MyDialog dialog = new MyDialog(context, R.layout.add_dialogt,
				R.style.MyDialog);
		View view = dialog.getWindow().getDecorView();
		final TvMarqueeText mt = (TvMarqueeText) view.findViewById(R.id.mt_title);
		mt.startMarquee();
		TvHorizontalGridView tgv_list = (TvHorizontalGridView) view.findViewById(R.id.tgv_list);
		tgv_list.setAdapter(new TvGridAdapter(context, apps));
		dialog.show();
		tgv_list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(View item, int position) {
				Application app = apps.get(position);
				pf.putString(currentItem.getTag().toString(), app.getPackageName());
				currentItem.showApplication(app);
				mt.stopMarquee();
				dialog.dismiss();
				dialog.cancel();
			}
		});
		
	}
	private void updateData(){
		for (int i = 0; i < items.length; i++) {
			String tag = items[i].getTag().toString();
			Log.i("zxy", "tag==="+tag);
			String packageName = pf.getPackage(tag);
			if(packageName!=null){
				Application app =Application.doApplication(packageName, getApplicationContext());
				if(app==null){
					app = Application.doAddApplication(getApplicationContext());
				}
				items[i].showApplication(app);
			}else{
				pf.delete(tag);
				items[i].showApplication(Application.doAddApplication(getApplicationContext()));
			}
			if (i>4){
				items[i].setNextFocusDownId(R.id.movie_text);
			}
		}
		for (int i = 0; i < itemsb.length; i++) {
			String tag = itemsb[i].getTag().toString();
			Log.i("zxy", "tag==="+tag);
			String packageName = pf.getPackage(tag);
			if(packageName!=null){
				Application app =Application.doApplication(packageName, getApplicationContext());
				if(app==null){
					app = Application.doAddApplication(getApplicationContext());
				}
				itemsb[i].showApplication(app);
			}else{
				pf.delete(tag);
				itemsb[i].showApplication(Application.doAddApplication(getApplicationContext()));
			}
			itemsb[i].setNextFocusDownId(R.id.adult_text);
		}
	}
    
	
	public void OniconClick(View view){
		Log.i("zxy", "onclick  id ="+view.getId());
		switch (view.getId()) {
		case R.id.bt_apps:
			 	Intent intent = new Intent(MainActivity.this,AppsActivity.class);
			 	startActivity(intent);
			break;
		case R.id.bt_setting:
				Intent intentset = getPackageManager().getLaunchIntentForPackage("com.android.tv.settings");
				if(intentset!=null){
					startActivity(intentset);
				}
			break;	
		case R.id.bt_clean:
			Intent intentclean = new Intent(MainActivity.this,StartActivity.class);
		 	startActivity(intentclean);
			break;
		}
	}
	
	public List<String> getlocalApps(){
		List<String> localapps = new ArrayList<String>();
		for (int i = 0; i < items.length; i++) {
			String tag = items[i].getTag().toString();
			String packString = pf.getPackage(tag);
			if(packString!=null){
				localapps.add(packString);
			}
		}
		return localapps;
	}
	
	 
	private void registeservers() {
		IntentFilter filter;

		filter = new IntentFilter();
		filter.addAction(NetUtil.CONNECTIVITY_CHANGE);
		filter.addAction(NetUtil.RSSI_CHANGE);
		filter.addAction(NetUtil.WIFI_STATE_CHANGE);
		registerReceiver(netReceiver, filter);
		
		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addDataScheme("file");
		registerReceiver(mediaReciever, filter);
		
		filter = new IntentFilter();
		filter.addAction("ider.install.over");
		registerReceiver(installReceiver, filter);
	}
	

			BroadcastReceiver installReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
//					pf.putString("12", "com.yolib.ibiza");
//					pf.putString("11", "com.ibizatv.ch2");
//						pf.putString("1", "com.qianxun.tvbox");
//						pf.putString("2", "tvfan.tv");
//						pf.putString("3", "com.moretv.android");
//						pf.putString("4", "com.android.vending");
//						pf.putString("5", "hdpfans.com");
//						pf.putString("6", "com.iflytek.aichang.tv");
//						pf.putString("7", "org.amotv.videolive");
//						pf.putString("8", "com.google.android.youtube");
//						pf.putString("9", "com.shafa.market");
						
					

						updateData();
				};
			};
	BroadcastReceiver netReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (NetUtil.CONNECTIVITY_CHANGE.equals(action)) {
				updateNetState(getApplicationContext());
			}
		}
	};
	
	private void  updateNetState(Context context){
		if (NetUtil.isNetworkAvailable(context)) {
			if(NetUtil.isEthernetConnect(context)) {
				net_icon.setImageResource(R.drawable.eth_icon);
			} else {
				net_icon.setImageResource(R.drawable.icon_net);
			}
		} else {
			net_icon.setImageResource(R.drawable.icon_neterror);
		}
	}
	
	BroadcastReceiver mediaReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
					Log.i("tag", "4444");
					checkExternalStorage();
				}
				if(Intent.ACTION_MEDIA_UNMOUNTED.equals(action)){
					checkExternalStorage();
				}
			}
		};
	
	 private void checkExternalStorage() {
        String usb1_path = getExternalPaths()[0];
        String usb2_path = getExternalPaths()[1];
        String tf_path = getExternalPaths()[2];
        usb_icon.setImageResource(R.drawable.icon_usb_false);
        tf_icon.setImageResource(R.drawable.icon_sdcard_false);
        if (usb1_path != null) {
            File usb1 = new File(usb1_path);
            if (usb1.exists() && usb1.getTotalSpace() > 0) {
                usb_icon.setImageResource(R.drawable.icon_usb);
            }
        }

        if (usb2_path != null) {
            File usb2 = new File(usb2_path);
            if (usb2.exists() && usb2.getTotalSpace() > 0) {
            	usb_icon.setImageResource(R.drawable.icon_usb);
            }
        }

        if (tf_path != null) {
            File tf = new File(tf_path);
            if (tf.exists() && tf.getTotalSpace() > 0) {
                tf_icon.setImageResource(R.drawable.icon_sdcard);
            }
        }

    }
	
	private String[] getExternalPaths() {
        Log.i("tag", "SDK version = " + Build.VERSION.SDK_INT+getCpuName());
        if (Build.VERSION.SDK_INT < 23) {
            String[] path = new String[3];

            if (Build.DEVICE.contains("rk")) {
                path[0] = "/mnt/usb_storage/USB_DISK0";
                path[1] = "/mnt/usb_storage/USB_DISK1";
                path[2] = null;
            } else if (getCpuName().contains("Amlogic")) {
                String usbP = "/storage/external_storage";
                Log.i("tag", "111");
                File file = new File(usbP);
                if(file!=null&&file.listFiles().length>0){
                   switch (file.listFiles().length){
                       case 1:
                           if(!file.listFiles()[0].getName().contains("sdcard")){
                               path[0] = usbP + "/" +file.listFiles()[0].getName();

                           }else{
                               path[0] = null;
                           }
                           path[1] = null;
                           break;
                       case 2:
                           if(!file.listFiles()[0].getName().contains("sdcard")) {
                               path[0] = usbP + "/" + file.listFiles()[0].getName();
                               if(!file.listFiles()[1].getName().contains("sdcard")){
                                   path[1] = usbP + "/" +file.listFiles()[1].getName();
                               }else{
                                   path[1] = null;
                               }
                           }else {
                               path[0] = usbP + "/" + file.listFiles()[1].getName();
                               path[1] = null;
                           }


                           break;
                       case 3:
                           path[0] = usbP + "/" +file.listFiles()[0].getName();
                           path[1] = usbP + "/" +file.listFiles()[1].getName();
                           break;
                   }

                }
                path[2] = "/storage/external_storage/sdcard1";
            } else if(getCpuName().equals("sun8i")) {
                path[0] = "/mnt/usbhost/Storage01";
                path[1] = "/mnt/usbhost/Storage02";
                path[2] = null;
            }
            return path;
        }else{
        	return getNUSBPath();
        }
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String[] getNUSBPath() {
        String[] paths = new String[3];
        StorageManager storageManager = (StorageManager) getSystemService(Service.STORAGE_SERVICE);
        try {
            Class SM = Class.forName("android.os.storage.StorageManager");
            Class VI = Class.forName("android.os.storage.VolumeInfo");
            Class DI = Class.forName("android.os.storage.DiskInfo");
            Method getVolumes = SM.getDeclaredMethod("getVolumes");
            Method getPath = VI.getDeclaredMethod("getPath");
            Method getDiskInfo = VI.getDeclaredMethod("getDisk");
            Method isMountedReadable = VI.getDeclaredMethod("isMountedReadable");
            Method getType = VI.getDeclaredMethod("getType");

            Method isUsb = DI.getDeclaredMethod("isUsb");
            Method isSd = DI.getDeclaredMethod("isSd");
            List volumeInfos = (List) getVolumes.invoke(storageManager);
            for(int i = 0; i < volumeInfos.size(); i++) {
            	
                Object volume = volumeInfos.get(i);
                if(volume != null &&isMountedReadable.invoke(volume).toString().equals("true")&&(Integer) getType.invoke(volume) == 0) {
                    File path = (File) getPath.invoke(volumeInfos.get(i));
                    Log.i("tag", "path = " + path.getAbsolutePath());
                    Object diskInfo = getDiskInfo.invoke(volumeInfos.get(i));
                    String isUSB = isUsb.invoke(diskInfo).toString();
                    String isSD = isSd.invoke(diskInfo).toString();
                    if(isUSB.equals("true")) {
                        if(paths[0] == null) {
                            paths[0] = path.getAbsolutePath();
                        } else {
                            paths[1] = path.getAbsolutePath();
                        }
                    }
                    if(isSD.equals("true")) {
                        paths[2] = path.getAbsolutePath();
                    }
                }

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return paths;
    }
	
	public String getCpuName() {
        File file = new File("/proc/cpuinfo");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Hardware")) {
                    String cpu = line.split(":")[1];
                    return cpu.trim();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
	
	@Override
	protected void onDestroy() {
		try{
			unregisterReceiver(netReceiver);
			unregisterReceiver(mediaReciever);
			unregisterReceiver(installReceiver);
			//mt_tip.stopMarquee();
			address_tip.stopMarquee();
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
