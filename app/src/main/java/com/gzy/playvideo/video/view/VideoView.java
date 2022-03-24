package com.gzy.playvideo.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gzy.playvideo.R;
import com.gzy.playvideo.video.data.AdParameters;
import com.gzy.playvideo.video.utils.Utils;

import java.io.IOException;

@SuppressLint("ViewConstructor")
public class VideoView extends FrameLayout {

    private final String TAG = "VideoView";

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    private int mPlayerState = STATE_IDLE;

    private TextureView mTextureView;
    private ImageView mIvPreview;
    private ProgressBar mProgressBar;
    private final AudioManager mAudioManager;
    private Surface mSurface;

    private MediaPlayer mMediaPlayer;
    private VideoPlayerListener mVideoPlayerListener;

    private boolean mInitComplete = false;

    private VideoInitListener mVideoInitListener;

    public VideoView(Context context, LayoutParams layoutParams) {
        super(context);
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initView(layoutParams);
    }

    private void initView(LayoutParams layoutParams) {
        setLayoutParams(layoutParams);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.layout_video, this);
        mTextureView = layout.findViewById(R.id.ttv_video_layout);
        mTextureView.setKeepScreenOn(true);
        mTextureView.setOnClickListener(v -> {
            if (v == mTextureView) {
                mVideoPlayerListener.onVideoClick();
            }
        });
        mIvPreview = layout.findViewById(R.id.iv_video_layout_preview);
        mProgressBar = layout.findViewById(R.id.pb_video_layout);
        initTextureView();
    }

    private void initTextureView() {
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                Log.e(TAG, "onSurfaceTextureAvailable");
                mSurface = new Surface(surface);
                mInitComplete = true;
                if (mVideoInitListener != null) {
                    mVideoInitListener.onComplete();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
                Log.e(TAG, "onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                Log.e(TAG, "onSurfaceTextureDestroyed");
                mInitComplete = false;
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
            }
        });
    }

    private MediaPlayer createMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setSurface(mSurface);
        mMediaPlayer.setOnPreparedListener(mp -> {
            Log.e(TAG, "Prepared");
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onVideoLoadSuccess();
            }
            mProgressBar.setVisibility(GONE);
            mMediaPlayer = mp;

            if (Utils.canAutoPlay(getContext(), AdParameters.getCurrentSetting())) {
                start();
            }
        });
        mMediaPlayer.setOnCompletionListener(mp -> {
            Log.e(TAG, "Completion");
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onVideoPlayComplete();
            }
            playBack();
        });
        mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "Error");
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onVideoLoadFailed();
            }
            mProgressBar.setVisibility(GONE);
            mPlayerState = STATE_ERROR;
            mMediaPlayer = mp;
            stop();
            return true;
        });
        mMediaPlayer.setOnInfoListener((mp, what, extra) -> true);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return mMediaPlayer;
    }

    public void loadPreview(String url) {
        mIvPreview.setVisibility(VISIBLE);
        Glide.with(mIvPreview).load(url).into(mIvPreview);
    }

    public void loadVideo(String url) {
        Log.e(TAG, "loadVideo");
        if (this.mPlayerState != STATE_IDLE) {
            return;
        }
        mProgressBar.setVisibility(VISIBLE);
        try {
            mMediaPlayer = createMediaPlayer();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "loadVideo Exception");
            stop();
        }
    }

    public void start() {
        Log.e(TAG, "start");
        if (this.mPlayerState == STATE_PLAYING) {
            return;
        }
        if (!isPlaying()) {
            mPlayerState = STATE_PLAYING;
            mMediaPlayer.start();
            mIvPreview.setVisibility(GONE);
        }
    }

    public void pause() {
        if (this.mPlayerState != STATE_PLAYING) {
            return;
        }
        mPlayerState = STATE_PAUSING;
        if (isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        Log.e(TAG, "stop");
        // MediaPlayer 进入播放状态才能调用 stop
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mPlayerState = STATE_IDLE;
    }

    public void destroy() {
        Log.e(TAG, "destroy");
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void playBack() {
        mPlayerState = STATE_IDLE;
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
        }
    }

    public void mute(boolean mute) {
        if (mMediaPlayer != null && this.mAudioManager != null) {
            float volume = mute ? 0.0f : 1.0f;
            mMediaPlayer.setVolume(volume, volume);
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void setVideoPlayerListener(VideoPlayerListener videoPlayerListener) {
        this.mVideoPlayerListener = videoPlayerListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    public void seekAndPause(int position) {
        if (mPlayerState != STATE_PLAYING) {
            return;
        }
        mPlayerState = STATE_PAUSING;
        if (isPlaying()) {
            mMediaPlayer.seekTo(position);
            mMediaPlayer.setOnSeekCompleteListener(mp -> mMediaPlayer.pause());
        }
    }

    public void setVideoInitListener(VideoInitListener mVideoInitListener) {
        this.mVideoInitListener = mVideoInitListener;
        if (mInitComplete) {
            mVideoInitListener.onComplete();
        } else {
            initTextureView();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e(TAG, "onDetachedFromWindow");
        stop();
        destroy();
        super.onDetachedFromWindow();
    }

    public interface VideoInitListener {
        void onComplete();
    }

    public interface VideoPlayerListener {

        void onVideoLoadSuccess();

        void onVideoLoadFailed();

        void onVideoPlayComplete();

        void onVideoClick();
    }


}