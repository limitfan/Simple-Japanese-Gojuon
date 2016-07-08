package com.limitfan.gojuuon.acts;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limitfan.gojuuon.R;
import com.limitfan.gojuuon.utils.Common;
import com.umeng.analytics.MobclickAgent;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;

import java.util.HashMap;
import java.util.Vector;

public class ActYouon extends Activity {
	LinearLayout table;
	TextView button;
	
	
	

	QuickAction quickAction;
	//action id
	private static final int ID_HIRAPRON     = 1;
	private static final int ID_KATAPRON  = 2;
	private static final int ID_HELP=3;



	TextView modeName;
	int curMode=1;
	
	boolean showAd=true;
	public void hira_kata_exchange(){
		
		if(isHira){
			if(curMode>1){
			hira2kata();
		    isHira=false;
			}
		   }
		   else{
			if(curMode<2){
			kata2hira();   
			 isHira=true; 
			 }
		   }
	}
	public void changeMode(int actionId){
		int newMode=actionId;
		if(newMode!=curMode){
			String modeStr="";
			switch (actionId){
			case ID_HIRAPRON: modeStr=this.getResources().getString(R.string.mode_hiragana_pronounce);
			modeName.setText(modeStr);
			curMode=newMode;
			hira_kata_exchange();
			break;
			case ID_KATAPRON: modeStr=this.getResources().getString(R.string.mode_katakana_pronounce);
			modeName.setText(modeStr);
			curMode=newMode;
			hira_kata_exchange();
			break;
			case ID_HELP:
				LayoutInflater li = LayoutInflater.from(ActYouon.this);
	            View view = li.inflate(R.layout.help_manual, null);
				Builder p = new AlertDialog.Builder(ActYouon.this).setView(view);
	            final AlertDialog alrt = p.create();
	            alrt.setIcon(R.drawable.big_help);
	            alrt.setTitle(getString(R.string.help_title));
	            alrt.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.confirm),
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog,
	                                int whichButton) {
	                        }
	                    });
	            alrt.show();
				break;
			default: break;
			}
		}

	}
	public void initializeActionBar(){


		String hp=this.getResources().getString(R.string.menu_hira_pron);
		String hs=this.getResources().getString(R.string.menu_hira_draw);
		String hd=this.getResources().getString(R.string.menu_hira_demo);
		String kp=this.getResources().getString(R.string.menu_kata_pron);
		String ks=this.getResources().getString(R.string.menu_kata_draw);
		String kd=this.getResources().getString(R.string.menu_kata_demo);
		String help=this.getResources().getString(R.string.help);
		ActionItem hpItem 	= new ActionItem(ID_HIRAPRON, hp, getResources().getDrawable(R.drawable.mymenu));
		ActionItem kpItem 	= new ActionItem(ID_KATAPRON, kp, getResources().getDrawable(R.drawable.mymenu));
		ActionItem helpItem= new ActionItem(ID_HELP, help, getResources().getDrawable(R.drawable.mymenu));

		//orientation


		//add action items into QuickAction
		quickAction.addActionItem(hpItem);
		quickAction.addActionItem(kpItem);
		quickAction.addActionItem(helpItem);

		//Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				changeMode(actionId);

			}
		});



	}
	Vector<Boolean> vb=new Vector<Boolean>();
	public void initColorState(){
		SharedPreferences sp=this.getPreferences(Context.MODE_PRIVATE);
		for(int i=0;i<33;i++){
			vb.add(sp.getBoolean(String.valueOf(i), false));
		}
	}
	
	
	HashMap<Integer,Integer> hm=new HashMap<Integer,Integer>();
	SoundPool soundPool;
    public void initSoundPool(){
    	soundPool=new SoundPool(3, AudioManager.STREAM_MUSIC, 100); 
    	try{
    	for(int i=1;i<=Common.youonRomaji.length;++i){
    		AssetFileDescriptor afd=getAssets().openFd("voices/"+Common.youonRomaji[i-1]+".wav");
    		hm.put(i, soundPool.load(afd, 1));
    	}
    	}
    	catch(Exception e){
    		
    	}
    	
    }
	 public void onCreate(Bundle paramBundle)
	  {
		// System.out.println("ActYouon:Here!!!!");
	    super.onCreate(paramBundle);
	    
	    
	   // TextView tv=new TextView(this);
	   // tv.setText("hellO");
	    setContentView(R.layout.youon);
	    new SoundPoolThread().start();
	    
	    if(this.isUnlocked()==1)
	    	showAd=false;
	    if(showAd){
	    }
	    modeName=(TextView)findViewById(R.id.modeName);
	    
	    quickAction = new QuickAction(this, QuickAction.VERTICAL);
		initializeActionBar();
	    TextView title=(TextView)findViewById(R.id.title);	    
	    title.setText(R.string.youon);
	  /*  ViewGroup listView = (ViewGroup)findViewById(R.id.list); //More Lists
	    ViewGroup listItem = (ViewGroup)getLayoutInflater().inflate(R.layout.common_list_item, null);
	    ((TextView)listItem.findViewById(R.id.text)).setText(R.string.more_about);
	    ((ImageView)listItem.findViewById(R.id.icon)).setImageResource(R.drawable.more_howto);
	    listView.addView(listItem);
	    System.out.println("ActMore taskId:"+this.getTaskId());
	    
	    
	    listItem.setOnClickListener(new ForwardListener());
	    
	    */
	    LinearLayout.LayoutParams localParams=Common.paramsFillParent;
	    localParams.weight=1.0f;
	    table=(LinearLayout)findViewById(R.id.kana);
	    int rows=Common.getYouonRowcnts();
		initColorState();
	    for(int i=0;i<rows;++i){
	    LinearLayout kanaRow=new LinearLayout(this);
	    kanaRow.setOrientation(LinearLayout.HORIZONTAL);
	    for(int j=0;j<3;++j){
	    LinearLayout kana=(LinearLayout)this.getLayoutInflater().inflate(R.layout.kana,null);
	    TextView main=(TextView)kana.getChildAt(0);
	    main.setText(Common.getYouon(i, j));
	    TextView sub=(TextView)kana.getChildAt(1);
	    sub.setText(Common.getYouonRomaji(i, j));
	    kanaRow.addView(kana,localParams);
		int location=i*3+j;
		if(vb.get(location))
			kana.setBackgroundResource(R.drawable.common_color_background_selector);
			kana.setOnClickListener(new KanaClickListener());
			kana.setOnLongClickListener(new ColorListener(location));
	    }
	    table.addView(kanaRow);
	    }
	    button=(TextView)findViewById(R.id.button);
	    button.setOnClickListener(new KataClickListener());
	    
	    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	  }
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	        new AlertDialog.Builder(this)
	        .setTitle(R.string.exit)
	        .setMessage(R.string.confirm_exit)
	        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
                    ActYouon.this.finish();
	            }

	        })
	        .setNegativeButton(R.string.cancel, null)
	        .show();

	        return true;
	    }
	    else {
	        return super.onKeyDown(keyCode, event);
	    }

	}
		public void invColorState(int j){
			SharedPreferences sp=this.getPreferences(Context.MODE_PRIVATE);
			if(vb.get(j))
				vb.set(j, false);
			else
				vb.set(j, true);
			Editor ed=sp.edit();
			for(int i=0;i<33;++i){
				ed.putBoolean(String.valueOf(i), vb.get(i));
			}
			ed.commit();
		}
		class ColorListener implements OnLongClickListener{
			
			int index;
			public ColorListener(int index){
				this.index=index;
			}

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				invColorState(index);
				if(vb.get(index))
					v.setBackgroundResource(R.drawable.common_color_background_selector);
				else
					v.setBackgroundResource(R.drawable.common_list_background_selector);
				return true;
				
			}
			
		}
		MediaPlayer mp=null;
		private void initMP(){
			if(mp==null)
				return;
			
		}
	 class KanaClickListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout kana=(LinearLayout)v;
				TextView main=(TextView)kana.getChildAt(0);
				TextView sub=(TextView)kana.getChildAt(1);
				//System.out.println("Kana Clicked"+main.getText());
				String romaji=sub.getText().toString().trim();
				if(!romaji.equals("")){
					 try {
						 soundPool.play(hm.get(Common.getYouonSeq(sub.getText().toString())+1), 1, 1, 0, 0, 1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally{
				
						}
					
				}
			}
			 
			 
		 }
	 boolean isHira=true;
	 class KataClickListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				quickAction.show(v);
				
			}
		 
	 }
	 public void hira2kata(){
		int rowcnt=Common.getYouonRowcnts();
		for(int i=0;i<rowcnt;++i)
		{
			LinearLayout row=(LinearLayout)table.getChildAt(i);
			for(int j=0;j<3;++j)
			{
			  LinearLayout kana=(LinearLayout)row.getChildAt(j);
			  TextView main=(TextView)kana.getChildAt(0);
			  main.setText(Common.getYouonKatakana(i, j));
				
			}
			
		}
		 
		 
	 }
	 public void kata2hira(){
			int rowcnt=Common.getYouonRowcnts();
			for(int i=0;i<rowcnt;++i)
			{
				LinearLayout row=(LinearLayout)table.getChildAt(i);
				for(int j=0;j<3;++j)
				{
				  LinearLayout kana=(LinearLayout)row.getChildAt(j);
				  TextView main=(TextView)kana.getChildAt(0);
				  main.setText(Common.getYouon(i, j));
					
				}
				
			}
			 
			 
		 }
	 public void onResume(){
		 super.onResume();
	    MobclickAgent.onResume(this);


		 
	 }
	 public void onPause(){
		 super.onPause();
	     MobclickAgent.onPause(this); 
		 
	 }
	 public void onDestroy(){
		 super.onDestroy();
	 }
		public int isUnlocked(){
			SharedPreferences sp=this.getSharedPreferences("key", Context.MODE_PRIVATE);
			return sp.getInt("key", 0);
		}
	    public void unlock(){
	    	SharedPreferences sp=this.getSharedPreferences("key", Context.MODE_PRIVATE);
	    	Editor ed=sp.edit();
	    	ed.putInt("key", 1);
	    	ed.commit();	
	    	//unlocked=1;
	    	
	    }
	    class SoundPoolThread extends Thread{
			public void run(){
				initSoundPool();
			}
		}
}