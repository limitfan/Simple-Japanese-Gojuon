package com.limitfan.gojuuon.acts;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Picture;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limitfan.gojuuon.R;
import com.limitfan.gojuuon.TMan;
import com.limitfan.gojuuon.utils.Common;
import com.limitfan.gojuuon.utils.StrokeUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;

import java.util.HashMap;
import java.util.Vector;

public class ActSeion extends Activity implements OnLoadCompleteListener {
	LinearLayout table;
	TextView button;



	Button egspeak,sketch,show,pre,next;
	String kana="",romaji="";
	int current=0;
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
	private Paint bgPaint=new Paint();
	private Rect r=new Rect(0,0,100,100);
	private Matrix matrix=new Matrix();

	private Bitmap bitmap;
	private Paint mBitmapPaint;
	private Canvas mCanvas;
	private Path    mPath=new Path();
	private Vector<Path> vp;
	
	boolean showAd=true;
	boolean isInitDone=false;
	public Handler handler=new Handler(){

		public  void handleMessage(Message msg){
			myKana.invalidate();

		}

	};




	public void resetWritePad(){

	
	}


	QuickAction quickAction;
	//action id
	private static final int ID_HIRAPRON     = 1;
	private static final int ID_HIRASTROKES   = 2;
	private static final int ID_HIRADEMO = 3;
	private static final int ID_KATAPRON  = 4;
	private static final int ID_KATASTROKES = 5;	
	private static final int ID_KATADEMO= 6;
	private static final int ID_HELP=7;



	TextView modeName;
	int curMode=1;

	public void hira_kata_exchange(){

		if(isHira){
			if(curMode>3){
				hira2kata();
				isHira=false;
			}
		}
		else{
			if(curMode<4){
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
			case ID_HIRASTROKES:modeStr=this.getResources().getString(R.string.mode_hiragana_draw);
			modeName.setText(modeStr);
			curMode=newMode;
			hira_kata_exchange();
			break;
			case ID_HIRADEMO: modeStr=this.getResources().getString(R.string.mode_hiragana_demo);
			modeName.setText(modeStr);
			curMode=newMode;
			hira_kata_exchange();
			break;
			case ID_KATAPRON: modeStr=this.getResources().getString(R.string.mode_katakana_pronounce);
			modeName.setText(modeStr);
			curMode=newMode;
			hira_kata_exchange();
			break;
			case ID_KATASTROKES:modeStr=this.getResources().getString(R.string.mode_katakana_draw);
			modeName.setText(modeStr);
			curMode=newMode;
			hira_kata_exchange();
			break;
			case ID_KATADEMO:modeStr=this.getResources().getString(R.string.mode_katakana_demo);
			modeName.setText(modeStr);
			curMode=newMode;
			hira_kata_exchange();
			break;
			case ID_HELP:
				

				LayoutInflater li = LayoutInflater.from(ActSeion.this);
	            View view = li.inflate(R.layout.help_manual, null);
				Builder p = new AlertDialog.Builder(ActSeion.this).setView(view);
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
		ActionItem hsItem 	= new ActionItem(ID_HIRASTROKES, hs, getResources().getDrawable(R.drawable.mymenu));
		ActionItem hdItem 	= new ActionItem(ID_HIRADEMO, hd, getResources().getDrawable(R.drawable.mymenu));
		ActionItem kpItem 	= new ActionItem(ID_KATAPRON, kp, getResources().getDrawable(R.drawable.mymenu));
		ActionItem ksItem 	= new ActionItem(ID_KATASTROKES,ks, getResources().getDrawable(R.drawable.mymenu));
		ActionItem kdItem 		= new ActionItem(ID_KATADEMO, kd, getResources().getDrawable(R.drawable.mymenu));
		ActionItem helpItem= new ActionItem(ID_HELP, help, getResources().getDrawable(R.drawable.mymenu));

		//orientation


		//add action items into QuickAction
		quickAction.addActionItem(hpItem);
		quickAction.addActionItem(hsItem);
		quickAction.addActionItem(hdItem);
		quickAction.addActionItem(kpItem);
		quickAction.addActionItem(ksItem);
		quickAction.addActionItem(kdItem);
		quickAction.addActionItem(helpItem);

		//Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				changeMode(actionId);

			}
		});



	}
	public void alertWritePad(){

		
		//System.out.println("alert write pad!");
		
		String title=this.getResources().getString(R.string.kana_writing);
		String close=this.getResources().getString(R.string.close);

		counter=0;
		isExit=false;
		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		initializeActionBar();
		AlertDialog alertDialog = new AlertDialog.Builder(ActSeion.this)
		.setTitle(title).
		setNeutralButton(close, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){


                    isExit=true;
			}
		})
		.create();




		LayoutInflater li = LayoutInflater.from(ActSeion.this);
		View view = li.inflate(R.layout.sketchpad, null);

		myKana=new MyView(this);
		LinearLayout ll=(LinearLayout)view.findViewById(R.id.sketchView);
		ll.addView(myKana);
		alertDialog.setView(view);
		next=(Button)view.findViewById(R.id.third);
		show=(Button)view.findViewById(R.id.second);
		pre=(Button)view.findViewById(R.id.first);

		pre.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myKana.preKana();
				new StrokeThread().start();
			}

		});
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myKana.nextKana();
				new StrokeThread().start();

			}

		});
		show.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				new StrokeThread().start();
			}
		}
			);

       alertDialog.show();
     //  myKana.drawCurKana();
       
     /// new StrokeThread().start();
	}

	Vector<Boolean> vb=new Vector<Boolean>();
	public void initColorState(){
		SharedPreferences sp=this.getPreferences(Context.MODE_PRIVATE);
		for(int i=0;i<55;i++){
			vb.add(sp.getBoolean(String.valueOf(i), false));
		}
	}

	
	int CAMERA_WIDTH;
	int CAMERA_HEIGHT;
	public  void initWindowSize(){
		DisplayMetrics dm=new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		int height=dm.heightPixels;
		
		System.out.println("width&height:"+width+" "+height);
		Rect statusFrame=new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(statusFrame);
	//	System.out.println()
		height-=statusFrame.top;
		CAMERA_WIDTH=width;
	    CAMERA_HEIGHT=height;
	    
	    
	    System.out.println("Width&Height:"+CAMERA_WIDTH+" "+CAMERA_HEIGHT);
		
	}
	
	 
	HashMap<Integer,Integer> hm=new HashMap<Integer,Integer>();
	SoundPool soundPool;
    public void initSoundPool(){
    	soundPool=new SoundPool(3, AudioManager.STREAM_MUSIC, 100); 
    	soundPool.setOnLoadCompleteListener(this);
    	try{
    	for(int i=1;i<=Common.roma.length;++i){
    		AssetFileDescriptor afd=getAssets().openFd("voices/"+Common.roma[i-1]+".wav");
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
		setContentView(R.layout.seion);
		new SoundPoolThread().start();
		ViewGroup parent = (ViewGroup)findViewById(R.id.parent);		
		
		/* 应用联盟集成方式， 请在AndroidManifest.xml中添加 UMENG_APPKEY */
	
		
		//this.initWindowSize();
		if(this.isUnlocked()==1){
		showAd=false;	
		}
		if(showAd){
//			ExchangeConstants.ONLY_CHINESE=false;
//			ExchangeDataService service = new ExchangeDataService();
//			ExchangeViewManager viewMgr = new ExchangeViewManager(this, service);
//			viewMgr.addView(parent, ExchangeConstants.type_standalone_handler);
		}
		
		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		initializeActionBar();
		TextView title=(TextView)findViewById(R.id.title);	
		title.setText(R.string.seion);
		modeName=(TextView)findViewById(R.id.modeName);

		LinearLayout.LayoutParams localParams=Common.paramsFillParent;
		localParams.weight=1.0f;

		table=(LinearLayout)findViewById(R.id.kana);
		int rows=Common.getRowcnts();
		
		initColorState();
		for(int i=0;i<rows;++i){
			LinearLayout kanaRow=new LinearLayout(this);
			kanaRow.setOrientation(LinearLayout.HORIZONTAL);
			for(int j=0;j<5;++j){
				LinearLayout kana=(LinearLayout)this.getLayoutInflater().inflate(R.layout.kana,null);
				TextView main=(TextView)kana.getChildAt(0);
				main.setText(Common.getHiragana(i, j));
				TextView sub=(TextView)kana.getChildAt(1);
				sub.setText(Common.getRomaji(i, j));
				kanaRow.addView(kana,localParams);
				
				int location=i*5+j;
			if(vb.get(location))
				kana.setBackgroundResource(R.drawable.common_color_background_selector);
				kana.setOnClickListener(new KanaClickListener());
				kana.setOnLongClickListener(new ColorListener(location));
				
			//	kana.setOnLongClickListener(l)
			}
			table.addView(kanaRow);
		}


		/*  ViewGroup listView = (ViewGroup)findViewById(R.id.list); //More Lists
	    ViewGroup listItem = (ViewGroup)getLayoutInflater().inflate(R.layout.common_list_item, null);
	    ((TextView)listItem.findViewById(R.id.text)).setText(R.string.more_about);
	    ((ImageView)listItem.findViewById(R.id.icon)).setImageResource(R.drawable.more_howto);
	    listView.addView(listItem);
	    System.out.println("ActMore taskId:"+this.getTaskId());


	    listItem.setOnClickListener(new ForwardListener());

		 */
		button=(TextView)findViewById(R.id.button);
		button.setOnClickListener(new KataClickListener());

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		UmengUpdateAgent.update(this);
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);



	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK) {
//			new AlertDialog.Builder(this)
//			.setTitle(R.string.exit)
//			.setMessage(R.string.confirm_exit)
//			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					ActSeion.this.finish();
//				}
//
//			})
//			.setNegativeButton(R.string.cancel, null)
//			.show();
//
//			return true;
//		}
//		else {
//			return super.onKeyDown(keyCode, event);
//		}
//
//	}

	
	public void invColorState(int j){
		SharedPreferences sp=this.getPreferences(Context.MODE_PRIVATE);
		if(vb.get(j))
			vb.set(j, false);
		else
			vb.set(j, true);
		Editor ed=sp.edit();
		for(int i=0;i<55;++i){
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
			LinearLayout kanaLayout=(LinearLayout)v;
			TextView main=(TextView)kanaLayout.getChildAt(0);
			TextView sub=(TextView)kanaLayout.getChildAt(1);
			//System.out.println("Kana Clicked"+main.getText());
			//System.out.println("here!!!");
			if(!sub.getText().toString().trim().equals("")){
				kana=main.getText().toString();
				romaji=sub.getText().toString();

				if(curMode==1||curMode==4){
					try {
						
					    soundPool.play(hm.get(Common.getSeq(sub.getText().toString())+1), 1, 1, 0, 0, 1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{

					}


				}
				else if(curMode==2||curMode==5){
					current=Common.getSeq(romaji);
					//System.out.println("here!");
					alertWritePad();
				}
				else if(curMode==3||curMode==6){

					Intent intent=new Intent(ActSeion.this,ActKana.class);	
					Bundle bundle=new Bundle();
					bundle.putBoolean("isHira", isHira);
					bundle.putString("romaji", sub.getText().toString());
					bundle.putString("kana", main.getText().toString());
					intent.putExtras(bundle);
					ActSeion.this.startActivity(intent);
				}

			}
		}


	}

	boolean isHira=true;
	boolean isExit=false;
	class KataClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			quickAction.show(v);


			/*   

			 */

		}

	}
	public void hira2kata(){
		int rowcnt=Common.getRowcnts();
		for(int i=0;i<rowcnt;++i)
		{
			LinearLayout row=(LinearLayout)table.getChildAt(i);
			for(int j=0;j<5;++j)
			{
				LinearLayout kana=(LinearLayout)row.getChildAt(j);
				TextView main=(TextView)kana.getChildAt(0);
				main.setText(Common.getKatakana(i, j));

			}

		}


	}
	public void kata2hira(){
		int rowcnt=Common.getRowcnts();
		for(int i=0;i<rowcnt;++i)
		{
			LinearLayout row=(LinearLayout)table.getChildAt(i);
			for(int j=0;j<5;++j)
			{
				LinearLayout kana=(LinearLayout)row.getChildAt(j);
				TextView main=(TextView)kana.getChildAt(0);
				main.setText(Common.getHiragana(i, j));

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
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//退出弹出
//			TMan.getInstance(this).showExitDialog(this, new View.OnClickListener() {
//				public void onClick(View arg0) {
//					//释放资源
//				}
//			});
		}
		return super.onKeyDown(keyCode, event);
	}
	protected void onDestroy(){
		//关闭弹出
		TMan.getInstance(this).colseExitDialog();
		super.onDestroy();
	}



	boolean isPlayed=false;
	int counter=0;
	public class MyView extends View {

		private static final float MINP = 0.25f;
		private static final float MAXP = 0.75f;
		public MyView(Context c) {
			super(c);

			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);

			mPaint.setStrokeWidth(15);
			mPaint.setColor(Color.rgb(51, 181,229));
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setAntiAlias(true);


			bgPaint.setStrokeWidth(15);
			bgPaint.setColor(Color.LTGRAY);
			bgPaint.setStyle(Style.STROKE);
			bgPaint.setStrokeJoin(Paint.Join.ROUND);
			bgPaint.setStrokeCap(Paint.Cap.ROUND);
			bgPaint.setAntiAlias(true);

            isInitDone=true;
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
            if(su==null)
			su=new StrokeUtil(109,109,ActSeion.this);
			mPath.set(su.getKanaPath(kana));
			matrix.reset();
			matrix.setScale(sx, sy);
			//matrix.postScale(sx, sy, dx,dy);
			mPath.transform(matrix);
			vp=su.getStrokes(kana);
			mCanvas.drawPath(mPath, bgPaint);
			isInitDone=true;
		}

		@Override
		protected void onDraw(Canvas c) {

			/*	canvas.drawColor(Color.BLACK);

			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaint);

			System.out.println("On Draw called!");
			 */

			if(!isPlayed){
				c.drawColor(Color.BLACK);
				//	c.drawBitmap(bitmap, 0, 0, mBitmapPaint);
				c.drawPath(mPath,bgPaint);
			}
			else{
				if(counter%2==0){
					c.drawColor(Color.BLACK);
					c.drawBitmap(bitmap, 0, 0, mBitmapPaint);
					c.drawPath(seg,mPaint);
				}
				else{
					c.drawColor(Color.BLACK);
					c.drawBitmap(bitmap, 0, 0, mBitmapPaint);
					c.drawPath(seg,bgPaint);

				}

			}

			//Rect rect=new Rect(0,0,Common.KVGW,Common.KVGH);
			//	c.drawRect(rect, mPaint);



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

			//Color
			mCanvas.drawColor(Color.BLACK);
			invalidate();
		}

		public void drawCurKana(){
			if(isHira)
				mPath.set(su.getKanaPath(Common.hira[current]));
			else
				mPath.set(su.getKanaPath(Common.kata[current]));
			matrix.reset();
			matrix.setScale(sx, sy);
			//matrix.postScale(sx, sy, dx,dy);
			mPath.transform(matrix);
			if(isHira)
				vp=su.getStrokes(Common.hira[current]);
			else
				vp=su.getStrokes(Common.kata[current]);
			mCanvas.drawColor(Color.BLACK);
			mCanvas.drawPath(mPath, bgPaint);
			counter=0;
			invalidate();
			
			new StrokeThread().start();
		}
		public void preKana(){
			if(current==0){
				Toast.makeText(ActSeion.this,R.string.no_more, Toast.LENGTH_SHORT).show();
				
			}
			else{
				current--;
				if(isHira)
					mPath.set(su.getKanaPath(Common.hira[current]));
				else
					mPath.set(su.getKanaPath(Common.kata[current]));
				matrix.reset();
				matrix.setScale(sx, sy);
				//matrix.postScale(sx, sy, dx,dy);
				mPath.transform(matrix);
				if(isHira)
					vp=su.getStrokes(Common.hira[current]);
				else
					vp=su.getStrokes(Common.kata[current]);
				mCanvas.drawColor(Color.BLACK);
				mCanvas.drawPath(mPath, bgPaint);
				counter=0;
				invalidate();

			}


		}
		public void nextKana(){
			if(current==45){
				Toast.makeText(ActSeion.this,R.string.no_more, Toast.LENGTH_SHORT).show();
			}
			else{
				current++;
				if(isHira)
					mPath.set(su.getKanaPath(Common.hira[current]));
				else
					mPath.set(su.getKanaPath(Common.kata[current]));
				matrix.reset();
				matrix.setScale(sx, sy);
				//matrix.postScale(sx, sy, dx,dy);
				mPath.transform(matrix);
				if(isHira)
					vp=su.getStrokes(Common.hira[current]);
				else
					vp=su.getStrokes(Common.kata[current]);
				mCanvas.drawColor(Color.BLACK);
				mCanvas.drawPath(mPath, bgPaint);
				counter=0;
				invalidate();

			}


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
			//while(!isInitDone);
			show.setClickable(false);
			next.setClickable(false);
			pre.setClickable(false);
			//	System.out.println("counter:"+counter);
			//System.out.println("Stroke nums:"+vp.size());
			isPlayed=true;
			for(int i=0;i<vp.size();++i)
			{

				//	System.out.println("Haha");
				//	seg.set(vp.get(i));


				PathMeasure pm=new PathMeasure(vp.get(i),false);
				float len=pm.getLength();
				//	System.out.println("Len:"+len);
				float start=0.0f,end;
				float delta=3.0f;
				while(start<len)
				{
					if(isExit)
						break;
					end=min(start+delta,len);
					seg.reset();
					pm.getSegment(start, end, seg,true);
					matrix.reset();
					matrix.setScale(sx, sy);
					//matrix.postScale(sx, sy, dx,dy);
					seg.transform(matrix);
					if(counter%2==0)
						mCanvas.drawPath(seg, mPaint);
					else
						mCanvas.drawPath(seg, bgPaint);
					start+=delta;

					//   System.out.println("hello")
					try{
						Message msg=handler.obtainMessage();
						msg.arg1=0;
						handler.sendMessage(msg);
						Thread.sleep(50);
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

			seg.reset();
			show.setClickable(true);
			next.setClickable(true);
			pre.setClickable(true);
			counter++;
			isPlayed=false;
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
    int cnt=1;
    boolean isReady=false;
	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		// TODO Auto-generated method stub

		
	}
	
	class SoundPoolThread extends Thread{
		public void run(){
			initSoundPool();
		}
	}

}