package com.limitfan.gojuuon.utils;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class Common {




	  public static int GOJUON_CNT=1;
	public static float MNUM=120.0f;

	public static int APPID=2380;
	public static String APPKEY="00d5ac380b6d82ae6de05541ea692037";
	public static String ORDER_SERIAL="gojuon";
	public static int ACTID=202;
	public static int tabCurrent = 0;
	public static TabActivity activity;
	public static Context context;
	public static ArrayList<Activity> tabCurrentActivities;
	public static final LinearLayout.LayoutParams paramsFillParent;
	public static final LinearLayout.LayoutParams paramsFillWidth;
	public static final String traceDB="trace.db";
	public static final String bookmarkDB="bm.db";
	public static Calendar cal;
	public static int KVGW=109;
	public static int KVGH=109;
	
	public static int W=0;
	public static int H=0;


	static{
		tabCurrent = -1;
		tabCurrentActivities = new ArrayList();
		paramsFillParent = new LinearLayout.LayoutParams(-1, -1);
		paramsFillWidth = new LinearLayout.LayoutParams(-1, -1);
	}
	static String[]hiragana={
		"あいうえお",
		"かきくけこ",
		"さしすせそ",
		"たちつてと",
		"なにぬねの",
		"はひふへほ",
		"まみむめも",
		"や-ゆ-よ",
		"らりるれろ",
		"わ---を",
		"ん----"
	};
	static String[]katakana={
		"アイウエオ",
		"カキクケコ",
		"サシスセソ",
		"タチツテト",
		"ナニヌネノ",
		"ハヒフヘホ",
		"マミムメモ",
		"ヤ-ユ-ヨ",
		"ラリルレロ",
		"ワ---ヲ",
		"ン----"

	};
	static String[]romaji={
		"a","i","u","e","o",
		"ka","ki","ku","ke","ko",
		"sa","shi","su","se","so",
		"ta","chi","tsu","te","to",
		"na","ni","nu","ne","no",
		"ha","hi","fu","he","ho",
		"ma","mi","mu","me","mo",
		"ya","","yu","","yo",
		"ra","ri","ru","re","ro",
		"wa","","","","wo",
		"n","","","",""

	};
	public static String[]dakuon={
		"がぎぐげご",
		"ざじずぜぞ",
		"だぢづでど",
		"ばびぶべぼ",
		"ぱぴぷぺぽ"

	};
	public static String[]dakuonKatakana={
		"ガギグゲゴ",
		"ザジズゼゾ",
		"ダヂヅデド",
		"バビブベボ",
		"パピプペポ"

	};
	public static String[]dakuonRomaji={
		"ga","gi","gu","ge","go",
		"za","ji","zu","ze","zo",
		"da","ji","zu","de","do",
		"ba","bi","bu","be","bo",
		"pa","pi","pu","pe","po"

	};

	static String[]youon={
		"きゃ","きゅ","きょ",
		"ぎゃ","ぎゅ","ぎょ",
		"しゃ","しゅ","しょ",
		"じゃ","じゅ","じょ",
		"ちゃ","ちゅ","ちょ",
		"にゃ","にゅ","にょ",
		"ひゃ","ひゅ","ひょ",
		"びゃ","びゅ","びょ",
		"ぴゃ","ぴゅ","ぴょ",
		"みゃ","みゅ","みょ",
		"りゃ","りゅ","りょ"  
	};
	static String[]youonKatakana={
		"キャ","キュ","キョ",
		"ギャ","ギュ","ギョ",
		"シャ","シュ","ショ",
		"ジャ","ジュ","ジョ",
		"チャ","チュ","チョ",
		"ニャ","ニュ","ニョ",
		"ヒャ","ヒュ","ヒョ",
		"ビャ","ビュ","ビョ",
		"ピャ","ピュ","ピョ",
		"ミャ","ミュ","ミョ",
		"リャ","リュ","リョ"

	};
	public static String[]youonRomaji={
		"kya","kyu","kyo",
		"gya","gyu","gyo",
		"sha","shu","sho",
		"ja","ju","jo",
		"cha","chu","cho",
		"nya","nyu","nyo",
		"hya","hyu","hyo",
		"bya","byu","byo",
		"pya","pyu","pyo",
		"mya","myu","myo",
		"rya","ryu","ryo"
	};
	public static String getHiragana(int x,int y){

		return hiragana[x].charAt(y)+"";
	}
	public static String getKatakana(int x,int y){

		return katakana[x].charAt(y)+"";
	}
	public static String getRomaji(int x,int y){
		return romaji[5*x+y];	  
	}
	public static int getRowcnts(){
		return hiragana.length;
	}
	public static int getDakuonRowcnts(){
		return dakuon.length;
	}
	public static String getDakuon(int x,int y){

		return dakuon[x].charAt(y)+"";
	}
	public static String getDakuonKatakana(int x,int y){

		return dakuonKatakana[x].charAt(y)+"";
	}
	public static String getDakuonRomaji(int x,int y){

		return dakuonRomaji[x*5+y];
	}
	public static int getYouonRowcnts(){
		return youon.length/3;
	}
	public static String getYouon(int x,int y){

		return youon[x*3+y];
	}
	public static String getYouonRomaji(int x,int y){

		return youonRomaji[x*3+y];
	}
	public static String getYouonKatakana(int x,int y){
		return youonKatakana[x*3+y];
	}
	public static String[]hira={
		"あ","い","う","え","お",
		"か","き","く","け","こ",
		"さ","し","す","せ","そ",
		"た","ち","つ","て","と",
		"な","に","ぬ","ね","の",
		"は","ひ","ふ","へ","ほ",
		"ま","み","む","め","も",
		"や","ゆ","よ",
		"ら","り","る","れ","ろ",
		"わ","を",
	"ん"};
	public static String[]kata={
		"ア","イ","ウ","エ","オ",
		"カ","キ","ク","ケ","コ",
		"サ","シ","ス","セ","ソ",
		"タ","チ","ツ","テ","ト",
		"ナ","ニ","ヌ","ネ","ノ",
		"ハ","ヒ","フ","ヘ","ホ",
		"マ","ミ","ム","メ","モ",
		"ヤ","ユ","ヨ",
		"ラ","リ","ル","レ","ロ",
		"ワ","ヲ",
	"ン"};
	public  static String[]roma={
		"a","i","u","e","o",
		"ka","ki","ku","ke","ko",
		"sa","shi","su","se","so",
		"ta","chi","tsu","te","to",
		"na","ni","nu","ne","no",
		"ha","hi","fu","he","ho",
		"ma","mi","mu","me","mo",
		"ya","yu","yo",
		"ra","ri","ru","re","ro",
		"wa","wo",
		"n"
	};

	public static int getSeq(String r){
		int ret=0;
		for(int i=0;i<roma.length;++i)
			if(roma[i].equals(r)){
				ret=i;
				break;
			}

		return ret;
	}

	public static String[]dakuonHira={
		"が","ぎ","ぐ","げ","ご",
		"ざ","じ","ず","ぜ","ぞ",
		"だ","ぢ","づ","で","ど",
		"ば","び","ぶ","べ","ぼ",
		"ぱ","ぴ","ぷ","ぺ","ぽ"

	};
	public static String[]dakuonKata={
		"ガ","ギ","グ","ゲ","ゴ",
		"ザ","ジ","ズ","ゼ","ゾ",
		"ダ","ヂ","ヅ","デ","ド",
		"バ","ビ","ブ","ベ","ボ",
		"パ","ピ","プ","ペ","ポ"

	};

	public static int getDakuonSeq(String r){
		int ret=0;
		for(int i=0;i<dakuonRomaji.length;++i)
			if(dakuonRomaji[i].equals(r)){
				ret=i;
				break;
			}

		return ret;
	}
	public static int getYouonSeq(String r){
		int ret=0;
		for(int i=0;i<youonRomaji.length;++i)
			if(youonRomaji[i].equals(r)){
				ret=i;
				break;
			}

		return ret;
	}

}
