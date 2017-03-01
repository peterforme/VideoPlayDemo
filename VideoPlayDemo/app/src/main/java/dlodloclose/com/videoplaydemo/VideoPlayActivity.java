package dlodloclose.com.videoplaydemo;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayActivity extends Activity implements SurfaceHolder.Callback {


    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_sound)
    TextView tvSound;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    private SurfaceHolder holder;
    private MediaPlayer mediaPlayer;
    private String fileUrl = "";
    private String filePath = "";
    private Uri uri;
    private Handler handler;
    private float downX, downY;
    private int screenWidth;
    private int FACTOR = 100;
    private boolean isPlaying = false, isShowing = true, has_move = false;
    private BaseDownloadTask task;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.pwh("activity oncreate");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);

        fileUrl = getIntent().getStringExtra("file_url");
        filePath = Const.VIDEO_FOLDER_NAME + FileUtil.getFileName(fileUrl);
        LogUtils.pwh("本地文件的路径为:" + filePath);
        handler = new Handler();

        //如果是希望直接播放本地的视频 那么就执行ini  iniVideo就可以了
        //ini();
        //iniVideo();

        //如果是希望下载网络视频后才播放，那么就在下载完后执行ini  iniVideo
        //执行下载
        task = FileDownloader.getImpl().create(fileUrl).setPath(filePath)
                .setListener(new FileDownloadListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        LogUtils.pwh("progress:" + soFarBytes);

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        try {
                            LogUtils.pwh("download completed");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ini();
                                    iniVideo();
                                }
                            }, 1000);
                        } catch (Exception e) {
                            LogUtils.pwh(e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                });
        task.start();




    }

    private void ini() {
        LogUtils.pwh("ini");
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    return;
                }
                if (mediaPlayer.isPlaying()) {
                    LogUtils.pwh("onclick , is playing");
                    mediaPlayer.pause();
                    ivPlay.setImageResource(R.drawable.video_play);
                } else {
                    LogUtils.pwh("onclick , not playing, play now");
                    startPlay();
                    ivPlay.setImageResource(R.drawable.video_pause);
                }
            }
        });


        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void iniVideo() {
        LogUtils.pwh("iniVideo");
        //这样做就可以延迟surfaceview的创建
        surfaceView.setVisibility(View.VISIBLE);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        has_move = false;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (!has_move) {
                            //如果没有移动过，就说明只是点击了一下屏幕
                            if (isShowing) {
                                ivClose.setVisibility(View.GONE);
                                bottom.setVisibility(View.GONE);
                                isShowing = false;
                            } else {
                                ivClose.setVisibility(View.VISIBLE);
                                bottom.setVisibility(View.VISIBLE);
                                isShowing = true;
                            }
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // TODO 音量
                        float distanceX = event.getX() - downX;
                        float distanceY = event.getY() - downY;
                        if (downX > screenWidth - 200
                                && Math.abs(distanceX) < 50
                                && distanceY > FACTOR) {
                            // TODO 减小音量
                            setVolume(false);
                            has_move = true;
                        } else if (downX > screenWidth - 200
                                && Math.abs(distanceX) < 50
                                && distanceY < -FACTOR) {
                            // TODO 增加音量
                            setVolume(true);
                            has_move = true;
                        }
                        // TODO 播放进度调节
                        if (Math.abs(distanceY) < 50 && distanceX > FACTOR) {
                            // TODO 快进
                            int currentT = mediaPlayer.getCurrentPosition();//播放的位置
                            mediaPlayer.seekTo(currentT + 15000);
                            downX = event.getX();
                            downY = event.getY();

                            has_move = true;
                        } else if (Math.abs(distanceY) < 50
                                && distanceX < -FACTOR) {
                            // TODO 快退
                            int currentT = mediaPlayer.getCurrentPosition();
                            mediaPlayer.seekTo(currentT - 15000);
                            downX = event.getX();
                            downY = event.getY();

                            has_move = true;
                        }
                        break;
                }
                return true;
            }
        });
        holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        LogUtils.pwh("addCallback now");
        holder.addCallback(this);
        LogUtils.pwh("addCallback finish");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        // TODO　给videoview设置播放源(通过本地存储卡来设置)
        uri = Uri.fromFile(new File(filePath));

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //这个时候才能正确获取到时长
                tvDuration.setText(TimeUtil.getTimeStr(mediaPlayer.getDuration() / 1000));
                tvCurrent.setText(TimeUtil.getTimeStr(mediaPlayer.getCurrentPosition() / 1000));
                sbProgress.setMax(mediaPlayer.getDuration());

                if (currentPosition == 0) {
                    LogUtils.pwh("currentPosition == 0, performclick");
                    ivPlay.performClick();
                } else {
                    //表示是切出去再进来的
                    currentPosition = 0;
                }
            }
        });
    }

    private void startPlay() {
        LogUtils.pwh("startPlay");
        // 开始播放视频
        mediaPlayer.start();
        isPlaying = true;
        updateView();
    }

    private void setVolume(boolean flag) {
        // 获取音量管理器
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 获取当前音量
        int curretnV = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (flag) {
            curretnV++;
        } else {
            curretnV--;
        }
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, curretnV,
                AudioManager.FLAG_SHOW_UI);
//        tvSound.setVisibility(View.VISIBLE);
//        tvSound.setText("音量:" + curretnV);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvSound.setVisibility(View.GONE);
            }
        }, 1000);
        /**
         * 1.AudioManager.STREAM_MUSIC 多媒体 2.AudioManager.STREAM_ALARM 闹钟
         * 3.AudioManager.STREAM_NOTIFICATION 通知 4.AudioManager.STREAM_RING 铃音
         * 5.AudioManager.STREAM_SYSTEM 系统提示音 6.AudioManager.STREAM_VOICE_CALL
         * 电话
         *
         * AudioManager.FLAG_SHOW_UI:显示音量控件
         */
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.pwh("surfaceCreated :" + currentPosition);
        // 当surfaceView被创建完成之前才能绘制画布,所以只能在此回调方法之后开始播放
        try {
            if (currentPosition > 0) {
                //大于0说明是切换出了重新回来的，那么需要重新初始化
                iniVideo();
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.setDisplay(holder);
                mediaPlayer.prepare();
                mediaPlayer.seekTo(currentPosition);
                ivPlay.setImageResource(R.drawable.video_play);


            } else {
                // 1.指定播放源
                mediaPlayer.setDataSource(this, uri);
                // 2.将mediaplayer和surfaceView时行绑定
                mediaPlayer.setDisplay(holder);
                // 3.准备进行异步播放(当prepareAsync被调用后会执行mediaPlayer的onPrepared回调方法)
                mediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            LogUtils.pwh("exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        LogUtils.pwh("surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.pwh("surfaceDestroyed");
        try {
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
            }

            isPlaying = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新播放进度的递归
     */
    private void updateView() {
        if (isPlaying && mediaPlayer != null) {
            tvCurrent.setText(TimeUtil.getTimeStr(mediaPlayer.getCurrentPosition()
                    / 1000));
            sbProgress.setProgress(mediaPlayer.getCurrentPosition());
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO 设置进度控件
                updateView();
            }
        }, 100);
    }
}