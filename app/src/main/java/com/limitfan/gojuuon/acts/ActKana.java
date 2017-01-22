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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
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

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.limitfan.gojuuon.R;
import com.limitfan.gojuuon.utils.Common;
import com.limitfan.gojuuon.utils.ScreenSlidePageFragment;
import com.limitfan.gojuuon.utils.StrokeUtil;
import com.umeng.analytics.MobclickAgent;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import static com.limitfan.gojuuon.utils.Common.context;

public class ActKana extends FragmentActivity {
	
	
	ImageView stroke;
	LinearLayout write;
	public static TextView speak,title,previous,next,sample;
	Button egspeak,sketch,show,action,clear;
	String API="https://api.limitfan.com/tts/query.php?lang=ja&text=%s";
	int current=0;
	public static boolean isHira=true;
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

	ExoPlayer player;


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
				quickAction.show(v);
			}

		});
		clear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				myKana.clear();
				myKana.invalidate();
			}

		});
		show.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
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

	Handler mainHandler;

	public void showTipDialog(){
		AlertDialog alertDialog = new AlertDialog.Builder(ActKana.this)
				.setTitle(R.string.tipTitle)
				.setMessage(R.string.tipContent)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which){
					}
				}).create();
		alertDialog.show();
	}

	public boolean isTipShown(){
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		if(prefs.getBoolean("tip", false)){
			return true;
		}
		else {
			SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
			editor.putBoolean("tip", true);
			editor.apply();
			return false;
		}
	}

	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
	 * sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ScreenSlidePageFragment.create(position);
		}

		@Override
		public int getCount() {
			return 46;
		}
	}


	/* The pager widget, which handles animation and allows swiping horizontally to access previous
	* and next wizard steps.
			*/
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;


	public void onCreate(Bundle paramBundle)
	{
		// System.out.println("ActMore:Here!!!!");
		super.onCreate(paramBundle);


		Bundle bundle=this.getIntent().getExtras();
		isHira=bundle.getBoolean("isHira");
		kana=bundle.getString("kana");
		romaji=bundle.getString("romaji");
		current=Common.getSeq(romaji);
		setContentView(R.layout.detail);

		mainHandler = new Handler();

		BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
		TrackSelection.Factory videoTrackSelectionFactory =
				new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
		TrackSelector trackSelector =
				new DefaultTrackSelector(videoTrackSelectionFactory);
		LoadControl loadControl = new DefaultLoadControl();

	    player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);


		mkdir();
		if(this.isUnlocked()==1)
			showAd=false;



		title=(TextView)findViewById(R.id.title);	
		previous=(TextView)findViewById(R.id.previous);
		next=(TextView)findViewById(R.id.next);
		if(isHira)
			title.setText(this.getString(R.string.hira)+"•"+kana);
		else
			title.setText(this.getString(R.string.kata)+"•"+kana);	

		TextView back=(TextView)findViewById(R.id.back);
		back.setOnClickListener(new BackListener());

		speak=(TextView)findViewById(R.id.button);
		speak.setText(R.string.pronounce);


		previous.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				previous();
			}

		});
		next.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				next();
			}
		});

		speak.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					final MediaPlayer mp=new MediaPlayer();
					mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//完毕事件  
						@Override 
						public void onCompletion(MediaPlayer arg0) {  
							mp.stop();
							mp.release();  
						}  
					});  
					AssetFileDescriptor descriptor=getAssets().openFd("voices/"+Common.roma[current]+".wav");
					mp.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
					descriptor.close();
					mp.prepare();
					mp.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{

				}

			}
		});



		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		if(!isTipShown()){
			showTipDialog();
		}

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageTransformer(true, new CubeOutTransformer());
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				current = position;

				if(isHira){
					kana=Common.hira[position];
					title.setText(ActKana.this.getString(R.string.hira)+"•"+kana);

				}
				else{
					kana=Common.kata[position];
					title.setText(ActKana.this.getString(R.string.kata)+"•"+kana);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		mPager.setCurrentItem(current);

		//  System.out.println("sample:"+getSample());
	}

	public void speak(String s){
		ActKana.SpeakTask st=new SpeakTask();
		st.execute(simplify(s));
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
			resID=this.getResources().getIdentifier(txt, "string", "com.limitfan.gojuuon");
			ret=this.getString(resID);
		}
		catch(Exception e){

		}
		return ret;

	}


	public void next(){
		if(current==45){
			Toast.makeText(this,R.string.nextwarning,Toast.LENGTH_SHORT).show();
			return;
		}
		else{
			current++;
			mPager.setCurrentItem(current);
		}
	}

	public void previous(){
		if(current==0){
			Toast.makeText(this,R.string.prevwarning,Toast.LENGTH_SHORT).show();
			return;
		}
		else{
			current--;
			mPager.setCurrentItem(current);
		}
	}

	class BackListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			ActKana.this.finish();
		}
	}


	public String followRedirects(URL url) throws URISyntaxException {
		HttpsURLConnection urlConnection = null;
		try {
			HttpsURLConnection.setFollowRedirects(true);
			urlConnection = (HttpsURLConnection)url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(urlConnection!=null) {
			System.out.println(urlConnection.getHeaderFields().get("Location"));
			System.out.println("audio url:"+urlConnection.getURL().toURI().toString());
			String temp = urlConnection.getHeaderFields().get("Location").toString();
			return "https://"+temp.substring(8, temp.length()-1);
			// should give u the new url dynamically created by server.
		}
		else{
			return "";
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
		public void onPreExecute(){
			Toast toast=Toast.makeText(ActKana.this, R.string.fetch,Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			isNoError=true;
		}
		@Override
		protected String doInBackground(String... params) {
			try{
				String text=URLEncoder.encode(simplify(params[0]), "UTF-8");
				String url=String.format(API, text);
//				final MediaPlayer mpeg=new MediaPlayer();



				//mpeg.setDataSource();
				//getMainLooper().prepare();
//				Handler mainHandler = new Handler();


// Produces DataSource instances through which media data is loaded.

				DefaultBandwidthMeter bandwidthMeter2 = new DefaultBandwidthMeter();
				DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(ActKana.this, Util.getUserAgent(ActKana.this, "SimpleGojuuon"), bandwidthMeter2);
// Produces Extractor instances for parsing the media data.
				ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.

				System.out.println("extract audio url:"+followRedirects(new URL(url)));
				MediaSource videoSource = new ExtractorMediaSource(Uri.parse(followRedirects(new URL(url))),
						dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
				player.prepare(videoSource);
				player.setPlayWhenReady(true);
//				EMAudioPlayer player = new EMAudioPlayer(ActKana.this);
//
//				player.setDataSource(ActKana.this, Uri.parse());
//				player.prepareAsync();
//				player.start();
//				MediaPlayer.create()
//
//				mpeg.setDataSource(ActKana.this, Uri.parse(followRedirects(new URL(url))));
//				mpeg.prepareAsync();
//				mpeg.start();
//				mpeg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//完毕事件
//					@Override
//					public void onCompletion(MediaPlayer arg0) {
//						mpeg.release();
//					}
//				});
			}
			catch(Exception e){
				e.printStackTrace();
				isNoError=false;
			}
			return params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			if(isNoError){
				Toast toast=Toast.makeText(ActKana.this,result,Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				ViewGroup toastView=(ViewGroup) toast.getView();
				ImageView image=new ImageView(ActKana.this);
				image.setImageResource(R.drawable.speak_on);
				toastView.addView(image);
				toast.show();
			}
			else{
				Toast toast=Toast.makeText(ActKana.this,R.string.fetch_error,Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
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