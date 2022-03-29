package com.gzy.playvideo.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gzy.playvideo.R;
import com.gzy.playvideo.video.data.VideoParameters;
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
    private Surface mSurface;

    private MediaPlayer mMediaPlayer;

    private boolean mInitComplete = false;

    private boolean mIsProcessDetached;

    /**
     * Listener
     */
    private VideoInitListener mVideoInitListener;
    private VideoPlayListener mVideoPlayListener;

    public VideoView(Context context, LayoutParams layoutParams, boolean isProcessDetached) {
        super(context);
        setLayoutParams(layoutParams);
        initView();
        mIsProcessDetached = isProcessDetached;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.layout_video, this);
        mTextureView = layout.findViewById(R.id.ttv_video_layout);
        mTextureView.setKeepScreenOn(true);
        mTextureView.setOnClickListener(v -> {
            if (v == mTextureView) {
                mVideoPlayListener.onVideoClick();
            }
        });
        mIvPreview = layout.findViewById(R.id.iv_video_layout_preview);
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
                    mVideoInitListener.onComplete(VideoView.this);
                }

                if (mMediaPlayer != null) {
                    mMediaPlayer.setSurface(mSurface);
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
            mMediaPlayer = mp;
            if (Utils.canAutoPlay(getContext(), VideoParameters.getCurrentSetting())) {
                start();
            }
        });
        mMediaPlayer.setOnCompletionListener(mp -> {
            Log.e(TAG, "Completion");
            mVideoPlayListener.onVideoPlayComplete();
            playBack();
        });
        mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "Error");
            mVideoPlayListener.onVideoPlayFailed();
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
        try {
            mMediaPlayer = createMediaPlayer();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "loadVideo exception");
            e.printStackTrace();
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
            mVideoPlayListener.onVideoPlayStart();
            postDelayed(() -> mIvPreview.setVisibility(GONE), 500);
        }
    }

    public void pause() {
        if (this.mPlayerState != STATE_PLAYING) {
            return;
        }
        mPlayerState = STATE_PAUSING;
        if (isPlaying()) {
            mMediaPlayer.pause();
            mVideoPlayListener.onVideoPlayPause();
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
        float volume = mute ? 0.0f : 1.0f;
        mMediaPlayer.setVolume(volume, volume);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
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

    public void setVideoPlayerListener(VideoPlayListener videoPlayListener) {
        this.mVideoPlayListener = videoPlayListener;
    }

    public void setVideoInitListener(VideoInitListener mVideoInitListener) {
        this.mVideoInitListener = mVideoInitListener;
        if (mInitComplete) {
            mVideoInitListener.onComplete(this);
        } else {
            initTextureView();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e(TAG, "onDetachedFromWindow");
        if (mIsProcessDetached) {
            stop();
            destroy();
        }
        super.onDetachedFromWindow();
    }

    public void setIsProcessDetached(boolean processDetached) {
        this.mIsProcessDetached = processDetached;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.e(TAG, "visibility:" + visibility);
    }

    public interface VideoInitListener {
        void onComplete(VideoView videoView);
    }

    public interface VideoPlayListener {

        void onVideoPlayStart();

        void onVideoPlayPause();

        void onVideoPlayComplete();

        void onVideoPlayFailed();

        void onVideoClick();
    }


}