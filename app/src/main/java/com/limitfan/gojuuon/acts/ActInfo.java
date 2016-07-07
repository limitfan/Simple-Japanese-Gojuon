package com.limitfan.gojuuon.acts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limitfan.gojuuon.R;
import com.umeng.analytics.MobclickAgent;

public class ActInfo extends Activity {
	  private static final String tag = "actInfo: ";
	  TextView grammar;
		 public void onCreate(Bundle paramBundle)
		  {
		    super.onCreate(paramBundle);
		   // TextView tv=new TextView(this);
		   // tv.setText("hellO");
		    setContentView(R.layout.info);
		    TextView title=(TextView)findViewById(R.id.title);	    
		    
		    
		    //System.out.println("ActDictDetail taskId:"+this.getTaskId());
		    
		    //System.out.println(tag+Common.activityGroup().viewContents.getChildCount());
		    
		    TextView back=(TextView)findViewById(R.id.back);
		    back.setOnClickListener(new BackListener());
		    Intent intent=this.getIntent();
		    Bundle bundle=intent.getExtras();
		    int id=bundle.getInt("id");
		    grammar=(TextView)findViewById(R.id.grammer);
		    if(id==0){
		    	grammar.setText(this.getString(R.string.gojuuon));
		    	title.setText(R.string.intro);
		       }
		    else if(id==1){
		    	grammar.setText(this.getString(R.string.osusume));
		    	title.setText(R.string.advanced);
		    }
		    
		  }
		 
		 
		 
		 class BackListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub\
				
			//	ActMoreAbout.this.back();
				ActInfo.this.finish();
				
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