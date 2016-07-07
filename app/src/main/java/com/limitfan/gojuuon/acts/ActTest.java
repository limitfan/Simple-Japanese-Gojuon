package com.limitfan.gojuuon.acts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.limitfan.gojuuon.R;
import com.limitfan.gojuuon.acts.ActInfo.BackListener;

public class ActTest extends Activity {
	private static final String tag = "actTest: ";
	Button next;
	TextView description, a, b, c, d,back,title;
	int current = 1;

	public void loadProblem() {
		// mysql=SQLiteDatabase.openOrCreateDatabase(file, null);
		//
		// Cursor cur = mysql.rawQuery(getQueryString(current), null);
		//
		// if(cur!=null){
		// cur.moveToNext();
		description.setText("hello");
		a.setText("a");
		b.setText("b");
		c.setText("c");

		// System.out.println("Cur:"+cur.getString(1));
		// }

	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// TextView tv=new TextView(this);
		// tv.setText("hellO");
		setContentView(R.layout.wendachallenge);
		description = (TextView) this.findViewById(R.id.description);
		a = (TextView) this.findViewById(R.id.id_kana1);
		b = (TextView) this.findViewById(R.id.id_kana2);
		c = (TextView) this.findViewById(R.id.id_kana3);

		loadProblem();
		next = (Button) this.findViewById(R.id.id_pass);

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				current++;
				loadProblem();
			}

		});
		
		back=(TextView)findViewById(R.id.back);
	    back.setOnClickListener(new BackListener());
	    title=(TextView)findViewById(R.id.title);
	    title.setText(R.string.drill);

	}
	 class BackListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub\
				
			//	ActMoreAbout.this.back();
				ActTest.this.finish();
				
			}
			 
			 
		 }
}
