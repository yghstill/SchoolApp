package ui.loading;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

public class BaseApplication extends Application {
	private ArrayList<Activity> mList = new ArrayList<Activity>();
	private static BaseApplication instance;
	private String login_username = "";
	private boolean islogin = false;
	private String realname = "";
	private String idcard = "";
	private int id = 0;
	private static Context context;


	@Override
	public void onCreate() {
		super.onCreate();
		context=getApplicationContext();
	}




	public static Context getContext(){
		return context;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginUserName() {
		return login_username;
	}

	public void setLoginUserName(String login_username) {
		this.login_username = login_username;
	}

	public String getrealname() {
		return realname;
	}

	public void setrealname(String realname) {
		this.realname = realname;
	}

	public String getidcard() {
		return idcard;
	}

	public void setidcard(String idcard) {
		this.idcard = idcard;
	}



	public boolean getislogin() {
		return islogin;
	}

	public void setislogin(boolean islogin) {
		this.islogin = islogin;
	}



	public synchronized static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
