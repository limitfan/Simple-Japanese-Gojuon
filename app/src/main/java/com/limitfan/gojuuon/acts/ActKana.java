package com.limitfan.gojuuon.acts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Picture;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limitfan.gojuuon.R;
import com.limitfan.gojuuon.utils.Common;
import com.limitfan.gojuuon.utils.StrokeUtil;
import com.umeng.analytics.MobclickAgent;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

public class ActKana extends Activity {
	
	
	ImageView stroke;
	LinearLayout write;
	TextView speak,title,previous,next,sample;
	Button egspeak,sketch,show,action,clear;
	String API="http://translate.google.com/translate_tts?ie=UTF-8&&tl=ja&&q=%s";
	int current=0;
	boolean isHira=true;
	String kana="";
	String romaji="";
	Bitmap dst; 
	MyView myKana;

	
	int width=0,height=0;
	int dx=0,dy=0;
	float sx=1.0f,sy=1.0f;
	Bitmap mBitmap;
	Picture picture;
	StrokeUtil su;
	MyView mv;
	Path seg=new Path();

	private Paint mPaint=new Paint();
	private Rect r=new Rect(0,0,100,100);
	private Matrix matrix=new Matrix();
	
	private Bitmap bitmap;
	private Paint mBitmapPaint;
	private Canvas mCanvas;
	private Path    mPath=new Path();
	private Vector<Path> vp;
	public Handler handler=new Handler(){

		public  void handleMessage(Message msg){
			myKana.invalidate();
           
		}

	};

	
	QuickAction quickAction;
	
	boolean showAd=true;
	//action id
	private static final int ID_UP     = 1;
	private static final int ID_DOWN   = 2;
	private static final int ID_SEARCH = 3;
	private static final int ID_INFO   = 4;
	private static final int ID_ERASE  = 5;	
	private static final int ID_OK     = 6;
	public void initializeActionBar(){
		ActionItem nextItem 	= new ActionItem(ID_DOWN, "Hiragana", getResources().getDrawable(R.drawable.menu_down_arrow));
		ActionItem prevItem 	= new ActionItem(ID_UP, "Katakana", getResources().getDrawable(R.drawable.menu_up_arrow));
		ActionItem searchItem 	= new ActionItem(ID_SEARCH, "片假名", getResources().getDrawable(R.drawable.menu_search));
		ActionItem infoItem 	= new ActionItem(ID_INFO, "发音固定", getResources().getDrawable(R.drawable.menu_info));
		ActionItem eraseItem 	= new ActionItem(ID_ERASE, "Clear", getResources().getDrawable(R.drawable.menu_eraser));
		ActionItem okItem 		= new ActionItem(ID_OK, "OK", getResources().getDrawable(R.drawable.menu_ok));

		//orientation


		//add action items into QuickAction
		quickAction.addActionItem(nextItem);
		quickAction.addActionItem(prevItem);
		quickAction.addActionItem(searchItem);
		quickAction.addActionItem(infoItem);
		quickAction.addActionItem(eraseItem);
		quickAction.addActionItem(okItem);

		//Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);

				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_SEARCH) {
					Toast.makeText(getApplicationContext(), "Let's do some search action", Toast.LENGTH_SHORT).show();
				} else if (actionId == ID_INFO) {
					Toast.makeText(getApplicationContext(), "I have no info this time", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
			}
		});

		//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
		//by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
			@Override
			public void onDismiss() {
				Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
			}
		});


	}


	private MaskFilter  mEmboss;
	private MaskFilter  mBlur;





	public void alertWritePad(){
		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		initializeActionBar();
		AlertDialog alertDialog = new AlertDialog.Builder(ActKana.this)
		.setTitle("Kana writing for["+kana+"]").
		setNeutralButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){



				//new WallThread().start();
			}
		})
		.create();  //创建对话框


         

		LayoutInflater li = LayoutInflater.from(ActKana.this);
		View view = li.inflate(R.layout.sketchpad, null);

		myKana=new MyView(this);
		LinearLayout ll=(LinearLayout)view.findViewById(R.id.sketchView);
		ll.addView(myKana);
		alertDialog.setView(view);
		clear=(Button)view.findViewById(R.id.third);
		show=(Button)view.findViewById(R.id.first);
		action=(Button)view.findViewById(R.id.second);

		action.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				quickAction.show(v);
			}

		});
		clear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myKana.clear();
				myKana.invalidate();
			}

		});
		show.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				 
				new StrokeThread().start();
			}

		});
		alertDialog.show(); // 显示对话框	



	}
    public void mkdir(){
    	File file=new File(
				Environment.getExternalStorageDirectory()
						+ "/SimpleGojuon/");
    	if(!file.exists())
    		file.mkdir();
    }
	public void onCreate(Bundle paramBundle)
	{
		// System.out.println("ActMore:Here!!!!");
		super.onCreate(paramBundle);


		// TextView tv=new TextView(this);
		// tv.setText("hellO");

		Bundle bundle=this.getIntent().getExtras();
		isHira=bundle.getBoolean("isHira");
		kana=bundle.getString("kana");
		romaji=bundle.getString("romaji");
		current=Common.getSeq(romaji);
		setContentView(R.layout.detail);
		mkdir();
		if(this.isUnlocked()==1)
			showAd=false;

		ViewGroup listView = (ViewGroup)findViewById(R.id.list); //More Lists
		ViewGroup demo_speak = (ViewGroup)getLayoutInflater().inflate(R.layout.demo_list_item, null);
		((TextView)demo_speak.findViewById(R.id.text)).setText(R.string.demo);
		((ImageView)demo_speak.findViewById(R.id.icon)).setImageResource(R.drawable.speak_off);

		listView.addView(demo_speak);


		demo_speak.setOnClickListener(new SpeakListener());

		title=(TextView)findViewById(R.id.title);	
		previous=(TextView)findViewById(R.id.previous);
		next=(TextView)findViewById(R.id.next);
		if(isHira)
			title.setText(this.getString(R.string.hira)+"•"+kana);
		else
			title.setText(this.getString(R.string.kata)+"•"+kana);	

		TextView back=(TextView)findViewById(R.id.back);
		back.setOnClickListener(new BackListener());

		stroke=(ImageView)findViewById(R.id.stroke);
		//  write=(LinearLayout)findViewById(R.id.write);
		speak=(TextView)findViewById(R.id.button);
		speak.setText(R.string.pronounce);
	
		try{
			String img="";
			if(isHira)
				img="kanagraph/hiragana_"+romaji+".jpg";
			else
				img="kanagraph/katakana_"+romaji+".jpg";
			InputStream is=getAssets().open(img, AssetManager.ACCESS_STREAMING);

			Bitmap bm=BitmapFactory.decodeStream(is);
			//  dst=this.getWritePad(bm);
			//  BitmapDrawable bd=new BitmapDrawable(dst);
			//   write.setBackgroundDrawable(bd);
			//  Toast.makeText(this, "[width:"+bm.getWidth()+"][height:"+bm.getHeight()+"]",Toast.LENGTH_SHORT).show();
			stroke.setImageBitmap(bm);
		}
		catch(Exception e){

		}

		previous.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				previous();
			}



		});
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				next();
			}



		});



		speak.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent=new Intent(About.this,Main.class);
				//startActivity(intent);
				//   System.out.println("back "+About.this.back.isFocused());
				// System.out.println("back "+About.this.back.isPressed());
				//back.setBackgroundResource(R.drawable.bg_back_on);
				try {
					final MediaPlayer mp=new MediaPlayer();
					mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//完毕事件  
						@Override 
						public void onCompletion(MediaPlayer arg0) {  
							mp.stop();
							mp.release();  
						}  
					});  
					AssetFileDescriptor descriptor=getAssets().openFd("voices/"+romaji+".wav");
					mp.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
					descriptor.close();
					mp.prepare();
					mp.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{

				}

			}
		});

		sample=(TextView)(this.findViewById(R.id.sample));

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		setSample();
		//  System.out.println("sample:"+getSample());
	}
	public String getSample(){
		String txt="";
		String ret="";
		int resID=0;
		try{
			if(isHira)
				txt="h"+romaji;
			else
				txt="k"+romaji;
			//    InputStream is=getAssets().open(txt, AssetManager.ACCESS_STREAMING);
			//   byte[]tmp=new byte[is.available()];
			//   is.read(tmp);
			//    ret=new String(tmp,0,tmp.length,"UTF-8");
			resID=this.getResources().getIdentifier(txt, "string", "com.limitfan.gojuuon");
			ret=this.getString(resID);
		}
		catch(Exception e){

		}
		return ret;

	}

	public void setSample(){


		String txt="";
		String ret="";
		int resID=0;

		try{
			if(isHira)
				txt="h"+romaji;
			else
				txt="k"+romaji;
			//  InputStream is=getAssets().open(txt, AssetManager.ACCESS_STREAMING);
			//    byte[]tmp=new byte[is.available()];
			//    is.read(tmp);
			//ret=new String(tmp,0,tmp.length,"UTF-8");
			resID=this.getResources().getIdentifier(txt, "string", "com.limitfan.gojuuon");
			ret=this.getString(resID);
		}
		catch(Exception e){

		}

		sample.setText(ret);

	}
	public void next(){
		if(current==45){
			Toast.makeText(this,R.string.nextwarning,Toast.LENGTH_SHORT).show();
			return;
		}
		else{
			current++;
		}
		if(isHira){
			kana=Common.hira[current];
			title.setText(this.getString(R.string.hira)+"•"+kana);

		}
		else{
			kana=Common.kata[current];	
			title.setText(this.getString(R.string.kata)+"•"+kana);	
		}
		romaji=Common.roma[current];

		try{
			String img="";
			if(isHira)
				img="kanagraph/hiragana_"+romaji+".jpg";
			else
				img="kanagraph/katakana_"+romaji+".jpg";
			InputStream is=getAssets().open(img, AssetManager.ACCESS_STREAMING);

			Bitmap bm=BitmapFactory.decodeStream(is);
			stroke.setImageBitmap(bm);
		}
		catch(Exception e){

		} 
		setSample();
	}

	public void previous(){
		if(current==0){
			Toast.makeText(this,R.string.prevwarning,Toast.LENGTH_SHORT).show();
			return;
		}
		else{
			current--;
		}
		if(isHira){
			kana=Common.hira[current];
			title.setText(this.getString(R.string.hira)+"•"+kana);

		}
		else{
			kana=Common.kata[current];	
			title.setText(this.getString(R.string.kata)+"•"+kana);	
		}
		romaji=Common.roma[current];

		try{
			String img="";
			if(isHira)
				img="kanagraph/hiragana_"+romaji+".jpg";
			else
				img="kanagraph/katakana_"+romaji+".jpg";
			InputStream is=getAssets().open(img, AssetManager.ACCESS_STREAMING);

			Bitmap bm=BitmapFactory.decodeStream(is);
		//	Toast.makeText(this, "[width:"+bm.getWidth()+"][height:"+bm.getHeight()+"]",Toast.LENGTH_SHORT).show();
			stroke.setImageBitmap(bm);
		}
		catch(Exception e){

		}
		setSample();
	}

	class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub\

			//	ActMoreAbout.this.back();
			ActKana.this.finish();

		}


	}
	class SpeakListener implements OnClickListener{
		String s;
		public SpeakListener(){

		}
		@Override
		public void onClick(View arg0){


			this.s=getSample();
			SpeakTask st=new SpeakTask(simplify(s));
			st.execute(simplify(s));
		}



	}
	
	boolean hasAudio(String s){
		File file=new File(
				Environment.getExternalStorageDirectory()
						+ "/SimpleGojuon/" + s+".mp3");
		if(file.exists())
			return true;
		return false;
	}
	MediaPlayer mpeg;
	boolean isNoError=true;
	class SpeakTask extends AsyncTask<String, String, String>{
		String text;
		public SpeakTask(String t){
			text=t;
		}
		public void onPreExecute(){
			System.out.println("text:"+text);
			if(!hasAudio(text)){
				Toast toast=Toast.makeText(ActKana.this, R.string.fetch,Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				isNoError=true;
			}
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println("prams[0]:"+params[0]);
			if(hasAudio(params[0])){
				try{
				File file=new File(
						Environment.getExternalStorageDirectory()
								+ "/SimpleGojuon/" + params[0]+".mp3");
				mpeg=new MediaPlayer();
				mpeg.setDataSource(file.getAbsolutePath());
				mpeg.prepare();
				mpeg.start();
				mpeg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//完毕事件  
					@Override 
					public void onCompletion(MediaPlayer arg0) {  
						mpeg.release();  
					}  
				});  			
			}
			catch(Exception e){
				
			}
			}
				
			else{
				
				try {
					System.out.println("http://translate.google.com/translate_tts?tl=ja&q="+URLEncoder.encode(params[0],"utf-8"));
					URI uri = new URI(
							"http://translate.google.com/translate_tts?tl=ja&q="+URLEncoder.encode(params[0],"utf-8"));

					URL u = new URL(uri.toASCIIString());
					// this is the name of the local file you will create
					String targetFileName = params[0]+".mp3";
					boolean eof = false;
					HttpURLConnection c = (HttpURLConnection) u.openConnection();
					c.addRequestProperty("User-Agent", "Mozilla/5.0");
					c.setRequestMethod("GET");
					c.setDoOutput(true);
					c.connect();
					FileOutputStream f = new FileOutputStream(new File(
							Environment.getExternalStorageDirectory()
									+ "/SimpleGojuon/" + targetFileName));
					InputStream in = c.getInputStream();
					byte[] buffer = new byte[1024];
					int len1 = 0;
					while ((len1 = in.read(buffer)) > 0) {
						f.write(buffer, 0, len1);
					}
					f.close();
					System.out.println("here");
					File file=new File(
							Environment.getExternalStorageDirectory()
									+ "/SimpleGojuon/" + params[0]+".mp3");
					mpeg=new MediaPlayer();
					mpeg.setDataSource(file.getAbsolutePath());
					mpeg.prepare();
					mpeg.start();
					mpeg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//完毕事件  
						@Override 
						public void onCompletion(MediaPlayer arg0) {  
							mpeg.release();  
						}  
					});  			
					// Toast.makeText(MainActivity.this, "finish to download",
					// Toast.LENGTH_SHORT).show();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					isNoError=false;
					e.printStackTrace();
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					isNoError=false;
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					isNoError=false;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					isNoError=false;
					e.printStackTrace();

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					isNoError=false;
					e.printStackTrace();
				}
//			try{
//
//				///	System.out.println("Start to speak:"+params[0]);
//				String text=URLEncoder.encode(simplify(params[0]), "UTF-8");
//				String url=String.format(API, text);
//
//				//System.out.println("url:"+url);
//				mpeg=new MediaPlayer();
//				mpeg.setDataSource(url);
//				mpeg.prepare();
//				mpeg.start();
//				mpeg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//完毕事件  
//					@Override 
//					public void onCompletion(MediaPlayer arg0) {  
//						mpeg.release();  
//					}  
//				});  
//			}
//			catch(Exception e){
//				isNoError=false;
//			}
//			
			}
			return params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//progressDialog.dismiss();
			if(isNoError){
				Toast toast=Toast.makeText(ActKana.this,result,Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				LinearLayout toastView=(LinearLayout)toast.getView();
				ImageView image=new ImageView(ActKana.this);
				image.setImageResource(R.drawable.speak_on);
				toastView.addView(image);
				toast.show();
			}
			else{
				if(!hasAudio(text)){
				Toast toast=Toast.makeText(ActKana.this,R.string.fetch_error,Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();	
				}

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

	public String simplify(String s){
		if(s.indexOf('无')!=-1||s.indexOf("empty")!=-1)
			return "";
		else{
			int p=s.indexOf("(");
			int q=s.indexOf('（');
			if(p==-1&&q==-1)
				return s;
			else if(p!=-1)
				return s.substring(0,p);
			else
				return s.substring(0,q);

		}
	}


	public void onDestroy(){
		super.onDestroy();
	}


	public class MyView extends View {

		private static final float MINP = 0.25f;
		private static final float MAXP = 0.75f;
		public MyView(Context c) {
			super(c);

			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
			
			mPaint.setStrokeWidth(5);
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setAntiAlias(true);

		}

		public float min(float a,float b){
			if(a<b)
				return a;
			else
				return b;
		}
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(bitmap);
            width=w;
            height=h;
            
      
            sx=w*1.0f/Common.KVGW;
            sy=h*1.0f/Common.KVGH;
          
            
            sx=min(sx,sy);
            sy=min(sx,sy);
           
            su=new StrokeUtil(109,109,ActKana.this);
			vp=su.getStrokes(kana);
		}

		@Override
		protected void onDraw(Canvas c) {
            
		/*	canvas.drawColor(Color.BLACK);

			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaint);
			
			System.out.println("On Draw called!");
			*/
			c.drawColor(Color.BLACK);
			c.drawBitmap(bitmap, 0, 0, mBitmapPaint);
			c.drawPath(seg,mPaint);
			Rect rect=new Rect(0,0,109,109);
			c.drawRect(rect, mPaint);
			
			
			
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}
		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
				mX = x;
				mY = y;
			}
		}
		private void touch_up() {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			//	touch_start(x, y);
				//invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
			//	touch_move(x, y);
				//invalidate();
				break;
			case MotionEvent.ACTION_UP:
			//	touch_up();
			//	invalidate();
				break;
			}
			return true;
		}

		public void clear(){
			//mBitmap.eraseColor(c)

			mCanvas.drawColor(0xFFAAAAAA);
			invalidate();
		}
	}
	
	class StrokeThread extends Thread{



		public float min(float a,float b){
			if(a<b)
				return a;
			else
				return b;
		}

		public void run() {
			// TODO Auto-generated method stub
			
			System.out.println("Stroke nums:"+vp.size());
			for(int i=0;i<vp.size();++i)
			{
			
			//	System.out.println("Haha");
			//	seg.set(vp.get(i));
				
				
				PathMeasure pm=new PathMeasure(vp.get(i),false);
			    float len=pm.getLength();
			    System.out.println("Len:"+len);
			    float start=0.0f,end;
			    float delta=1.0f;
			        while(start<len)
			    	{
			    	   end=min(start+delta,len);
			    	   seg.reset();
			    	   pm.getSegment(start, end, seg,true);
						matrix.reset();
					//	matrix.setScale(sx, sy);
						//matrix.postScale(sx, sy, dx,dy);
						seg.transform(matrix);
						mCanvas.drawPath(seg, mPaint);
			    	    start+=delta;
			    	    
			    	//   System.out.println("hello")
			    	    			    	   try{
						   Message msg=handler.obtainMessage();
			    			msg.arg1=0;
			    	        handler.sendMessage(msg);
				    	   Thread.sleep(30);
				    	   }
				    	   catch(Exception e){

				    	   }
				    	   finally{
				    		   
				    	   }
			    	}

				 
				
				/*	PathMeasure pm=new PathMeasure(vp.get(i),false);
		    float len=pm.getLength();
		    System.out.println("Len:"+len);
		    float start=0.0f,end;
		    float delta=1.0f;
		        while(start<len)
		    	{
		    	   end=min(start+delta,len);
		    	   pm.getSegment(start, end, seg,true);



		    	   start+=delta;

		    	
		    	//   System.out.println("hello");
		    	}





				 */
			}


		}
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
}