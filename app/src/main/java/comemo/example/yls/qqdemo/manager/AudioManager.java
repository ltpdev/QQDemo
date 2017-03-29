package comemo.example.yls.qqdemo.manager;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by asus- on 2017/3/19.
 */

public class AudioManager {
    private MediaRecorder mediaRecorder;
    private String mDir;
    private String mCurrentFilePath;
    private boolean isPrepared;
    private static AudioManager mInstance;

    public AudioManager(String dir) {
        mDir=dir;
    }

    public String getCurrentPath() {
        return mCurrentFilePath;
    }

    public interface AudioPreparedListener{
        void wellPrepared();
    }
    private AudioPreparedListener mAudioPreparedListener;
    public void setAudioPreparedListener(AudioPreparedListener mAudioPreparedListener){
        this.mAudioPreparedListener=mAudioPreparedListener;
    }
    public static AudioManager getInstance(String dir){
       if(mInstance==null){
           synchronized (AudioManager.class){
               if (mInstance==null){
                   mInstance=new AudioManager(dir);
               }
           }
       }
        return mInstance;
    }
    public void prepareAudio(){
        try {
            isPrepared=false;
            File dir=new File(mDir);
            if (!dir.exists()){
                dir.mkdirs();
            }
            String fileName=generateFileName();
            File file=new File(dir,fileName);
            mCurrentFilePath=file.getAbsolutePath();
            mediaRecorder=new MediaRecorder();
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isPrepared=true;
            if (mAudioPreparedListener!=null){
                mAudioPreparedListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {
        return UUID.randomUUID().toString()+".amr";
    }

    public int getVoiceLevel(int maxLevel){
        try{
            if (isPrepared){
                return maxLevel*mediaRecorder.getMaxAmplitude()/32768+1;
            }
        }catch (Exception e){

        }
       return 1;
    }
    public void release(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;
    }
    public void cancel(){
        release();
        if (mCurrentFilePath!=null){
            File file=new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath=null;
        }
    }
}
