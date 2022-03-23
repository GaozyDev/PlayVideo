package com.gzy.playvideo.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gzy.playvideo.R;
import com.gzy.playvideo.video.data.AdParameters;
import com.gzy.playvideo.video.data.SDKConstant;
import com.gzy.playvideo.video.utils.Utils;

@SuppressLint("ViewConstructor")
public class VideoView extends FrameLayout {

    private final String TAG = "VideoView";

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    private int mPlayerState = STATE_IDLE;


    private final ViewGroup mParentView;
    private TextureView mTextureView;
    private ImageView mIvPreview;
    private ProgressBar mProgressBar;
    private final AudioManager mAudioManager;
    private Surface mSurface;

    private final String mUrl;
    private final String mPreviewURL;

    private MediaPlayer mMediaPlayer;
    private VideoPlayerListener mVideoPlayerListener;

    public VideoView(Context context, ViewGroup parentView, LayoutParams params, String sourceUrl, String previewUrl) {
        super(context);
        mParentView = parentView;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mUrl = sourceUrl;
        mPreviewURL = previewUrl;
        initView(params);
    }

    private void initView(LayoutParams params) {
        setLayoutParams(params);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fl_video_layout, this);

        mTextureView = layout.findViewById(R.id.ttv_video_layout);
        mTextureView.setKeepScreenOn(true);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mSurface = new Surface(surface);
                mMediaPlayer = createMediaPlayer();
                mMediaPlayer.setSurface(mSurface);
                load();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }
        });
        mTextureView.setOnClickListener(v -> {
            if (v == mTextureView) {
                mVideoPlayerListener.onVideoClick();
            }
        });
        mIvPreview = layout.findViewById(R.id.iv_video_layout_preview);
        Glide.with(mIvPreview).load(mPreviewURL).into(mIvPreview);
        mProgressBar = layout.findViewById(R.id.pb_video_layout);
    }

    private MediaPlayer createMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        mMediaPlayer.setOnPreparedListener(mp -> {
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onVideoLoadSuccess();
            }
            mProgressBar.setVisibility(GONE);
            mMediaPlayer = mp;

            if (Utils.canAutoPlay(getContext(), AdParameters.getCurrentSetting()) &&
                    Utils.getVisiblePercent(mParentView) > SDKConstant.VIDEO_SCREEN_PERCENT) {
                resume();
            }
        });
        mMediaPlayer.setOnCompletionListener(mp -> {
            if (mVideoPlayerListener != null) {
                mVideoPlayerListener.onVideoPlayComplete();
            }
            playBack();
        });
        mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
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

    public void load() {
        if (this.mPlayerState != STATE_IDLE) {
            return;
        }
        mProgressBar.setVisibility(VISIBLE);
        try {
            mMediaPlayer.setDataSource(this.mUrl);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            stop();
        }
    }

    public void resume() {
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
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        mPlayerState = STATE_IDLE;
    }

    public void playBack() {
        mPlayerState = STATE_IDLE;
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
        }
    }

    public void destroy() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        mPlayerState = STATE_IDLE;
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

    public interface VideoPlayerListener {

        void onVideoLoadSuccess();

        void onVideoLoadFailed();

        void onVideoPlayComplete();

        void onVideoClick();
    }
}