package com.limitfan.gojuuon;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.limitfan.gojuuon.acts.ActDakuon;
import com.limitfan.gojuuon.acts.ActMore;
import com.limitfan.gojuuon.acts.ActSeion;
import com.limitfan.gojuuon.acts.ActYouon;
import com.limitfan.gojuuon.templates.ViewFooter;
import com.limitfan.gojuuon.utils.Common;

public class GojuuonActivity extends TabActivity {
	/** Called when the activity is first created. */

	private TabHost tabHost;
	private ViewFooter vf;
	public static final String TAB_Dict = "tabSeion";
	public static final String TAB_Trans = "tabDakuon";
	public static final String TAB_Bookmark = "tabYouon";
	public static final String TAB_More = "tabMore";


	public void onWindowFocusChanged(boolean hasFocus){


		DisplayMetrics dm=new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		int height=dm.heightPixels;
		Rect rectgle= new Rect();
		Window window= getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int StatusBarHeight= rectgle.top;



		Common.W=width;
		Common.H=height-StatusBarHeight;
		//		 
		//		 System.out.println("hello:"+Common.W+" "+Common.H);
		//		 System.out.println("Window focus change");

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tabHost = getTabHost();	
		iniTabs();
		LinearLayout tmp = (LinearLayout) this.findViewById(R.id.footer);
		vf = (ViewFooter) tmp.getChildAt(0);
		addFooterListener();

		// tabHost.setCurrentTabByTag(TAB_Trans);

		choose(0);
	}

	public void iniTabs() {
		// System.out.println("initializing!!!");
		tabHost.addTab(tabHost.newTabSpec(TAB_Dict).setIndicator(TAB_Dict)
				.setContent(new Intent(this, ActSeion.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_Trans).setIndicator(TAB_Trans)
				.setContent(new Intent(this, ActDakuon.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_Bookmark)
				.setIndicator(TAB_Bookmark)
				.setContent(new Intent(this, ActYouon.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_More).setIndicator(TAB_More)
				.setContent(new Intent(this, ActMore.class)));
	}

	public void addFooterListener() {
		int i = 0;
		int j = vf.getChildCount();
		while (true) {
			if (i >= j) {
				return;
			}
			int k = i;
			View localView = vf.getChildAt(i);
			ViewFooterClickListener listener = new ViewFooterClickListener(k);
			localView.setOnClickListener(listener);
			i += 1;
		}

	}

	class ViewFooterClickListener implements OnClickListener {
		int ind;

		public ViewFooterClickListener(int i) {
			ind = i;
		}

		public void onClick(View paramView) {
			// ViewFooter localViewFooter=ViewFooter.this;
			// localViewFooter.switchTab(paramView, ind);
			// System.out.println("Cli i");
			choose(ind);

			// Toast.makeText(Common.context, "Hello,Toast!",
			// Toast.LENGTH_SHORT).show();
		}
	}

	public void choose(int ind) {
		vf.select(ind);
		tabHost.setCurrentTab(ind);
	}

}