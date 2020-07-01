package com.qiniu.droid.rtc.demo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableLayout;

import com.qiniu.droid.rtc.demo.R;

/**
 * Fragment for call control.
 */
public class ControlFragment extends Fragment {
    private View mControlView;
    private ImageButton mDisconnectButton;
    private ImageButton mCameraSwitchButton;
    private ImageButton mToggleMuteButton;
    private ImageButton mToggleBeautyButton;
    private ImageButton mToggleSpeakerButton;
    private ImageButton mToggleVideoButton;
    private ImageButton mLogShownButton;
    private ImageButton mStreamingConfigButton;
    private ImageButton mEffectButton;
    private ImageButton mStickerButton;

    private LinearLayout mLogView;
    private TableLayout mBottomBtnLayout;
    private TextView mLocalTextViewForVideo;
    private TextView mLocalTextViewForAudio;
    private TextView mRemoteTextView;
    private StringBuffer mRemoteLogText;
    private Chronometer mTimer;
    private OnCallEvents mCallEvents;
    private boolean mIsVideoEnabled = true;
    private boolean mIsShowingLog = false;
    private boolean mIsScreenCaptureEnabled = false;
    private boolean mIsAudioOnly = false;

    /**
     * Call control interface for container activity.
     */
    public interface OnCallEvents {
        void onCallHangUp();

        void onCameraSwitch();

        boolean onToggleMic();

        boolean onToggleVideo();

        boolean onToggleSpeaker();

        boolean onToggleBeauty();

        void onCallStreamingConfig();

        void onToggleEffect();

        void onToggleSticker();
    }

    public void setScreenCaptureEnabled(boolean isScreenCaptureEnabled) {
        mIsScreenCaptureEnabled = isScreenCaptureEnabled;
    }

    public void setAudioOnly(boolean isAudioOnly) {
        mIsAudioOnly = isAudioOnly;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mControlView = inflater.inflate(R.layout.fragment_room, container, false);

        mDisconnectButton = (ImageButton) mControlView.findViewById(R.id.disconnect_button);
        mCameraSwitchButton = (ImageButton) mControlView.findViewById(R.id.camera_switch_button);
        mToggleBeautyButton = (ImageButton) mControlView.findViewById(R.id.beauty_button);
        mToggleMuteButton = (ImageButton) mControlView.findViewById(R.id.microphone_button);
        mToggleSpeakerButton = (ImageButton) mControlView.findViewById(R.id.speaker_button);
        mToggleVideoButton = (ImageButton) mControlView.findViewById(R.id.camera_button);
        mLogShownButton = (ImageButton) mControlView.findViewById(R.id.log_shown_button);
        mLogView = (LinearLayout) mControlView.findViewById(R.id.log_text);
        mStreamingConfigButton = mControlView.findViewById(R.id.streaming_config_button);
        mLocalTextViewForVideo = (TextView) mControlView.findViewById(R.id.local_log_text_video);
        mLocalTextViewForVideo.setMovementMethod(ScrollingMovementMethod.getInstance());
        mLocalTextViewForAudio = (TextView) mControlView.findViewById(R.id.local_log_text_audio);
        mLocalTextViewForAudio.setMovementMethod(ScrollingMovementMethod.getInstance());
        mRemoteTextView = (TextView) mControlView.findViewById(R.id.remote_log_text);
        mRemoteTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTimer = (Chronometer) mControlView.findViewById(R.id.timer);
        mEffectButton = (ImageButton) mControlView.findViewById(R.id.effect_button);
        mStickerButton = (ImageButton) mControlView.findViewById(R.id.sticker_button);
        mBottomBtnLayout = (TableLayout) mControlView.findViewById(R.id.bottom_button_layout);

        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallEvents.onCallHangUp();
            }
        });

        if (!mIsScreenCaptureEnabled && !mIsAudioOnly) {
            mCameraSwitchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallEvents.onCameraSwitch();
                }
            });
        }

        if (!mIsScreenCaptureEnabled && !mIsAudioOnly) {
            mToggleBeautyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean enabled = mCallEvents.onToggleBeauty();
                    mToggleBeautyButton.setImageResource(enabled ? R.mipmap.face_beauty_open : R.mipmap.face_beauty_close);
                }
            });
        }

        mToggleMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enabled = mCallEvents.onToggleMic();
                mToggleMuteButton.setImageResource(enabled ? R.mipmap.microphone : R.mipmap.microphone_disable);
            }
        });

        if (mIsScreenCaptureEnabled || mIsAudioOnly) {
            mToggleVideoButton.setImageResource(R.mipmap.video_close);
        } else {
            mToggleVideoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean enabled = mCallEvents.onToggleVideo();
                    mToggleVideoButton.setImageResource(enabled ? R.mipmap.video_open : R.mipmap.video_close);
                }
            });
        }

        mToggleSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enabled = mCallEvents.onToggleSpeaker();
                mToggleSpeakerButton.setImageResource(enabled ? R.mipmap.loudspeaker : R.mipmap.loudspeaker_disable);
            }
        });

        mLogShownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogView.setVisibility(mIsShowingLog ? View.INVISIBLE : View.VISIBLE);
                mIsShowingLog = !mIsShowingLog;
            }
        });

        mStreamingConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallEvents.onCallStreamingConfig();
            }
        });


        mEffectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallEvents.onToggleEffect();
            }
        });

        mStickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallEvents.onToggleSticker();
            }
        });

        return mControlView;
    }

    public void setEffectButtonClicked(boolean isClicked) {
        mEffectButton.setImageResource(isClicked ? R.mipmap.effect_open : R.mipmap.effect_close);
    }

    public void setStickerButtonClicked(boolean isClicked) {
        mStickerButton.setImageResource(isClicked ? R.mipmap.sticker_open : R.mipmap.sticker_close);
    }

    public void setBottomBtnLayoutVisible(boolean isVisible) {
        mBottomBtnLayout.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public void startTimer() {
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.start();
    }

    public void stopTimer() {
        mTimer.stop();
    }

    public void updateLocalVideoLogText(String logText) {
        if (mLogView.getVisibility() == View.VISIBLE) {
            mLocalTextViewForVideo.setText(logText);
        }
    }

    public void updateLocalAudioLogText(String logText) {
        if (mLogView.getVisibility() == View.VISIBLE) {
            mLocalTextViewForAudio.setText(logText);
        }
    }

    public void updateRemoteLogText(String logText) {
        if (mRemoteLogText == null) {
            mRemoteLogText = new StringBuffer();
        }
        if (mLogView != null) {
            mRemoteTextView.setText(mRemoteLogText.append(logText + "\n"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mIsVideoEnabled) {
            mCameraSwitchButton.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallEvents = (OnCallEvents) activity;
    }
}
