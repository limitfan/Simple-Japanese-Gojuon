package com.limitfan.gojuuon.acts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.limitfan.gojuuon.R;
import com.umeng.analytics.MobclickAgent;

public class ActRecommend extends Activity {
	  private static final String tag = "actRecommend: ";
	   
	  Button first,second;
		 public void onCreate(Bundle paramBundle)
		  {
		    super.onCreate(paramBundle);
		   // TextView tv=new TextView(this);
		   // tv.setText("hellO");
		    setContentView(R.layout.recommend);
		    TextView title=(TextView)findViewById(R.id.title);	    
		    
		    first=(Button)this.findViewById(R.id.downfirst);
		    second=(Button)this.findViewById(R.id.downTwo);
		    first.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_VIEW); 
					intent.setData(Uri.parse("market://details?id=com.limitfan.animejapanese")); 
					startActivity(intent);
				}
		    	
		    	
		    	
		    });
		    second.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_VIEW); 
						intent.setData(Uri.parse("market://details?id=com.limitfan.omninihongolearning")); 
						startActivity(intent);
					}
			    	
			    	
			    	
			    });
		    
		    
		    //System.out.println("ActDictDetail taskId:"+this.getTaskId());
		    
		    //System.out.println(tag+Common.activityGroup().viewContents.getChildCount());
		    
		    TextView back=(TextView)findViewById(R.id.back);
		    back.setOnClickListener(new BackListener());
		   
		    	
		    	title.setText(R.string.advanced);
		  

		    
		  }
		 
		 
		 
		 class BackListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub\
				
			//	ActMoreAbout.this.back();
				ActRecommend.this.finish();
				
			}
			 
			 
		 }
		   public void onResume() { 
		        super.onResume(); 
		        MobclickAgent.onResume(this);
		    }
		    public void onPause() { 
		        super.onPause(); 
		        MobclickAgent.onPause(this); 
		    }
			 public void onDestroy(){
				 super.onDestroy();
			 }
}