package com.limitfan.gojuuon.utils;

import java.util.Vector;

import android.content.Context;
import android.graphics.Path;

public class StrokeUtil {

	public int width;
	public int height;
	Strokes strokes;

	public StrokeUtil(int w,int h,Context c){
		width=w;
		height=h;
		strokes=new Strokes(c);
	}

	public String[] normalize(String[]brr){
		int ind=0;
		String[]tmp=new String[200];
		String []arr;
		for(int i=0;i<brr.length;++i)
			if(!brr[i].equals(""))
				tmp[ind++]=brr[i];
		arr=new String[ind];
		for(int i=0;i<ind;++i)
			arr[i]=tmp[i];

		return arr;

	}
	public void parseStroke(Vector<Path> vp, String s){
		String[]arr=normalize(s.split(" "));
		Path tpath=new Path();
		float curx=0.0f,cury=0.0f,secx=0.0f,secy=0.0f;
		String preCMD="";
		for(int i=0;i<arr.length;){
			if(arr[i].equals("M")){
				float x=Float.parseFloat(arr[i+1]);
				float y=Float.parseFloat(arr[i+2]);
				i+=3;


				tpath.moveTo(x, y);

				curx=x;
				cury=y;
				secx=curx;
				secy=cury;
				preCMD="M";

			}
			else if(arr[i].equals("c")){

				float x1,y1,x2,y2,x,y;
				x1=Float.parseFloat(arr[i+1]);
				y1=Float.parseFloat(arr[i+2]);
				x2=Float.parseFloat(arr[i+3]);
				y2=Float.parseFloat(arr[i+4]);
				x=Float.parseFloat(arr[i+5]);
				y=Float.parseFloat(arr[i+6]);
				i+=7;	

				tpath.rCubicTo(x1, y1, x2, y2, x, y);

				secx=curx+x2;
				secy=cury+y2;
				curx+=x;
				cury+=y;

				preCMD="c";

			}
			else if(arr[i].equals("C")){
				float x1,y1,x2,y2,x,y;
				x1=Float.parseFloat(arr[i+1]);
				y1=Float.parseFloat(arr[i+2]);
				x2=Float.parseFloat(arr[i+3]);
				y2=Float.parseFloat(arr[i+4]);
				x=Float.parseFloat(arr[i+5]);
				y=Float.parseFloat(arr[i+6]);
				i+=7;	
				tpath.cubicTo(x1, y1, x2, y2, x, y);

				secx=x2;
				secy=y2;
				curx=x;
				cury=y;
				preCMD="C";
			}
			else if(arr[i].equals("S")){
				float x1,y1,x2,y2,x,y;
				x1=2*curx-secx;
				y1=2*cury-secy;
				x2=Float.parseFloat(arr[i+1]);
				y2=Float.parseFloat(arr[i+2]);
				x=Float.parseFloat(arr[i+3]);
				y=Float.parseFloat(arr[i+4]);

				i+=5;
				tpath.cubicTo(x1, y1, x2, y2, x, y);
				
				secx=x2;
				secy=y2;
				curx=x;
				cury=y;
			}
			else if(arr[i].equals("s")){
				float x1,y1,x2,y2,x,y;
				x1=2*curx-secx;
				y1=2*cury-secy;
				x2=Float.parseFloat(arr[i+1]);
				y2=Float.parseFloat(arr[i+2]);
				x=Float.parseFloat(arr[i+3]);
				y=Float.parseFloat(arr[i+4]);
                x2+=curx;
                y2+=cury;
                x+=curx;
                y+=cury;
				i+=5;
				tpath.cubicTo(x1, y1, x2, y2, x, y);
				
				secx=x2;
				secy=y2;
				curx=x;
				cury=y;
				
				

			}
			else{
				System.out.println("WARNNING");
				i+=1000;
			}

		}
		vp.add(tpath);

	}
	
	public void parseStroke(Path path, String s){
		String[]arr=normalize(s.split(" "));;
		float curx=0.0f,cury=0.0f,secx=0.0f,secy=0.0f;
		String preCMD="";
		for(int i=0;i<arr.length;){
			if(arr[i].equals("M")){
				float x=Float.parseFloat(arr[i+1]);
				float y=Float.parseFloat(arr[i+2]);
				i+=3;


				path.moveTo(x, y);

				curx=x;
				cury=y;
				secx=curx;
				secy=cury;
				preCMD="M";

			}
			else if(arr[i].equals("c")){

				float x1,y1,x2,y2,x,y;
				x1=Float.parseFloat(arr[i+1]);
				y1=Float.parseFloat(arr[i+2]);
				x2=Float.parseFloat(arr[i+3]);
				y2=Float.parseFloat(arr[i+4]);
				x=Float.parseFloat(arr[i+5]);
				y=Float.parseFloat(arr[i+6]);
				i+=7;	

				path.rCubicTo(x1, y1, x2, y2, x, y);

				secx=curx+x2;
				secy=cury+y2;
				curx+=x;
				cury+=y;

				preCMD="c";

			}
			else if(arr[i].equals("C")){
				float x1,y1,x2,y2,x,y;
				x1=Float.parseFloat(arr[i+1]);
				y1=Float.parseFloat(arr[i+2]);
				x2=Float.parseFloat(arr[i+3]);
				y2=Float.parseFloat(arr[i+4]);
				x=Float.parseFloat(arr[i+5]);
				y=Float.parseFloat(arr[i+6]);
				i+=7;	
				path.cubicTo(x1, y1, x2, y2, x, y);

				secx=x2;
				secy=y2;
				curx=x;
				cury=y;
				preCMD="C";
			}
			else if(arr[i].equals("S")){
				float x1,y1,x2,y2,x,y;
				x1=2*curx-secx;
				y1=2*cury-secy;
				x2=Float.parseFloat(arr[i+1]);
				y2=Float.parseFloat(arr[i+2]);
				x=Float.parseFloat(arr[i+3]);
				y=Float.parseFloat(arr[i+4]);

				i+=5;
				path.cubicTo(x1, y1, x2, y2, x, y);
				
				secx=x2;
				secy=y2;
				curx=x;
				cury=y;
			}
			else if(arr[i].equals("s")){
				float x1,y1,x2,y2,x,y;
				x1=2*curx-secx;
				y1=2*cury-secy;
				x2=Float.parseFloat(arr[i+1]);
				y2=Float.parseFloat(arr[i+2]);
				x=Float.parseFloat(arr[i+3]);
				y=Float.parseFloat(arr[i+4]);
                x2+=curx;
                y2+=cury;
                x+=curx;
                y+=cury;
				i+=5;
				path.cubicTo(x1, y1, x2, y2, x, y);
				
				secx=x2;
				secy=y2;
				curx=x;
				cury=y;
				
				

			}
			else{
				System.out.println("StrokeUtil:WARNNING");
				i+=1000;
			}

		}

	}
	public void formPath(Vector<Path> vp, Vector<String>vs){

		for(int i=0;i<vs.size();++i){
			String stroke_piece=vs.get(i);
			parseStroke(vp,stroke_piece);

		}

	}
	
	public void formPath(Path path,Vector<String> vs){
		
		for(int i=0;i<vs.size();++i)
		{
		String stroke_piece=vs.get(i);
		parseStroke(path,stroke_piece);
			
		}
		
	}
	public Path getKanaPath(String s){
		Path a=new Path();
		Vector<String> tmp=strokes.getStrokeDescription(s);
		formPath(a,tmp);
		return a;
	}
	public Vector<Path> getStrokes(String s){
		Vector<Path> vp=new Vector<Path>();
		Vector<String> tmp=strokes.getStrokeDescription(s);
		formPath(vp,tmp);


		return vp;

	}



}
