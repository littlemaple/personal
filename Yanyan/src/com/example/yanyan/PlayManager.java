package com.example.yanyan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.provider.MediaStore;
import android.util.Log;

public class PlayManager {
	
	public static PlayManager pManager = null;
	private MediaPlayer mediaPlayer = null;
	private String[] fileArray = null;  //  播放文件的路径数组，相当于文件列表 
	private Context mcontext;
	private int playPostition = 0; // 当前播放文件的位置，可用于暂停和继续播放的   
    private int currentFilePosition = -1;   //  当前播放的是第几个文件
    private String[] playStatus = { "end of music list", "top of music list",  
    "play" };   //  播放状态 
    
    public static PlayManager getInstance(Context context) {  
        if (pManager == null) {  
            pManager = new PlayManager(context);  
        }  
        return pManager;  
    }  
    
    private PlayManager(Context context) {                 // 在该管理类初始化时，就读取MainActivity.URL下的文件，找出mp3文件  
        // 这里是将路径写在代码里面了，你也可以将它写在一个文件夹浏览处，这样就可以动态的改变路径了  
    	mcontext = context;
    	
    	ArrayList<HashMap<String, Object>> mList = scanAllAudioFiles();
    	fileArray = new String[mList.size()];
    	for(int i = 0;i < mList.size();i++){
    		HashMap<String, Object> map = mList.get(i);
    		fileArray[i] = (String) map.get("musicFileUrl");    		
    	}
    	
    }  
	public ArrayList<HashMap<String, Object> > scanAllAudioFiles(){
		//生成动态数组，并且转载数据  
		ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();  

		if(mcontext == null){
			return null;
		}
		ContentResolver resolver = mcontext.getContentResolver();  
        if (resolver == null) {  
            return null;  
        }  
		//查询媒体数据库
		Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if(cursor.getCount() == 0){
			return null;
		}
		//遍历媒体数据库
		if(cursor.moveToFirst()){
		 
		       while (!cursor.isAfterLast()) {
		        //歌曲编号
		        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));  
		        //歌曲标题
		        String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));  
		        //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
		        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));  
		        //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
		        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));  
		        //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
		        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));    
		        //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
		        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));    
		        //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
		        Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
		       
		        if(size>1024*800){//大于800K
		        	HashMap<String, Object> map = new HashMap<String, Object>();
		        	map.put("musicId", id); 
		        	map.put("musicSinger", album);
		        	map.put("musicTitle", tilte);  
		        	map.put("musicFileUrl", url); 
		        	map.put("music_file_name", artist);  
		        	map.put("state", false);
		        	mylist.add(map);  
		        }
		        cursor.moveToNext(); 
		      } 
		  }
		return mylist;
	}
	
	public String startPlayVideo(int postion) throws Exception {   // 开始播放  
        currentFilePosition = postion;    //  因为currentFilePosition的初始值是-1，所以此处强制赋值为0，即播放第一个音频文件，返回播放状态  
        return playMusic();  
    }  
  
    private String playMusic() throws Exception {  
        if (currentFilePosition >= fileArray.length) {   //  首先判断当前播放的文件是否超多了列表  
        	currentFilePosition = 0;
//            return playStatus[0];   // 返回到低了，你也可以直接写成currentFilePosition=0，这样就能循环播放列表了  
        }  
        if (currentFilePosition < 0) {  
        	currentFilePosition = fileArray.length;
//            return playStatus[1];   //  返回到顶了，你也可以写成currentFilePosition=fileArray.length，这样反过来循环播放列表  
        }  
        releaseMedia(); //  每次开始播放列表时，都要将mediaPlayer释放掉，这样一边准备下一首或者上一首  
        mediaPlayer = new MediaPlayer();      
        mediaPlayer.setDataSource(fileArray[currentFilePosition]);  // 设置播放文件的路径  
        
        Log.d("PlayManger","this file's path is "  
                + fileArray[currentFilePosition]); 
        mediaPlayer.prepare();  //  准备  
        mediaPlayer.start();    //  开始播放  
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {  
            public void onCompletion(MediaPlayer mp) {  
                try {  
                    playNextMusic();    //  在每次播放完成之后都用播放下一首  
                    Intent register = new Intent("com.example.yanyan.intent.USER_ACTION");
                	mcontext.sendBroadcast(register);
//                    Log.d("PlayManger","completion this music, play the next"); 
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
        return playStatus[2];   //  返回正在播放  
    }  
  
    public String playNextMusic() throws Exception {  
        currentFilePosition++;  //  播放下一首时当前播放文件加1  
        return playMusic();  
    }  
  
    public String playBackMusic() throws Exception {  
        currentFilePosition--;  //  减1，播放上一首  
        return playMusic();  
    }  
  
    public void pausePlayMusic() {  //  暂停  
        if (mediaPlayer != null) {  
            playPostition = mediaPlayer.getCurrentPosition();   //  得到当前播放的位置  
            mediaPlayer.pause();  
        }  
    }  
  
    public void continuePlay() {    //  继续  
        if (mediaPlayer != null) {  
            mediaPlayer.seekTo(playPostition);  //  从记录的位置开始播放  
            mediaPlayer.start();  
        }  
    }  
  
    public void stopPlay() {  
        if (mediaPlayer != null) {  //  停止播放器  
            mediaPlayer.stop();  
        }  
    }  
  
    public void releaseMedia() {    //  这里是释放mediaPlayer播放对象  
        if (mediaPlayer != null) {  
            try {  
                mediaPlayer.release();  
                mediaPlayer = null;  
                System.gc();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
}  
