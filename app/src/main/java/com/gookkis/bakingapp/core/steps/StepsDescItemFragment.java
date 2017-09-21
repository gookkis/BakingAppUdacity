package com.gookkis.bakingapp.core.steps;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.utils.Const;
import com.gookkis.bakingapp.utils.Helpers;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by herikiswanto on 8/22/17.
 */
public class StepsDescItemFragment extends Fragment {


    public SimpleExoPlayer simpleExoPlayer;
    @BindView(R.id.simple_exo_player)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_step_detail)
    TextView tvDescription;
    @BindView(R.id.iv_prev)
    ImageButton ivPrev;
    @BindView(R.id.tv_steps)
    TextView tvSteps;
    @BindView(R.id.iv_next)
    ImageButton ivNext;
    @BindView(R.id.view_bottom)
    LinearLayout viewBottom;
    private MediaSessionCompat mMediaSession;

    public static final String PLAYWHENREADY = "PLAYWHENREADY";
    public static final String CURRENTWINDOW = "CURRENTWINDOW";
    public static final String PLAYBACKPOSITION = "PLAYBACKPOSITION";
    public boolean playWhenReady = true;
    public int currentWindow;
    public long playBackPosition;

    public String videoURL, description;
    public int stepPos;
    public Recipe mRecipe;

    public StepsDescItemFragment() {
    }

    public static StepsDescItemFragment newInstance(int stepPos) {
        StepsDescItemFragment f = new StepsDescItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.STEP_POS, stepPos);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && getArguments() != null && getArguments().containsKey(Const.STEP_POS)) {
            Bundle bundle = getArguments();
            stepPos = bundle.getInt(Const.STEP_POS);
        } else if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(PLAYWHENREADY);
            currentWindow = savedInstanceState.getInt(CURRENTWINDOW);
            playBackPosition = savedInstanceState.getLong(PLAYBACKPOSITION);
        }

        stepPos = Prefs.getInt(Const.STEP_POS, 0);
        String json = Prefs.getString(Const.RECIPE, "");
        mRecipe = new Gson().fromJson(json, Recipe.class);

        videoURL = mRecipe.getSteps().get(stepPos).getVideoURL();
        description = mRecipe.getSteps().get(stepPos).getDescription();


    }


    private void expandVideoView(SimpleExoPlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_steps_desc_item, container, false);
        ButterKnife.bind(this, rootView);

        tvDescription.setText(description);
        tvSteps.setText(String.valueOf(stepPos + 1) + "/" +
                String.valueOf(mRecipe.getSteps().size()));

        initializeMediaSession();

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !Helpers.isMultiPane(getActivity()) && !videoURL.equals("")) {
            expandVideoView(mPlayerView);
            viewBottom.setVisibility(View.INVISIBLE);
            hideSystemUI();
        }

        //if (videoURL.equals("")) {

        //} else {
        initializePlayer(Uri.parse(videoURL));
        //mPlayerView.setVisibility(View.VISIBLE);
        hideSystemUI();
        //}


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAYWHENREADY, playWhenReady);
        outState.putInt(CURRENTWINDOW, currentWindow);
        outState.putLong(PLAYBACKPOSITION, playBackPosition);
        //outState.putInt(Const.STEP_POS, stepPos);
        Prefs.putInt(Const.STEP_POS, stepPos);
    }


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), "StepsDescItemFragment");

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                simpleExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                simpleExoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                simpleExoPlayer.seekTo(0);
            }
        });
        mMediaSession.setActive(true);
    }


    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayerView.setPlayer(simpleExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "StepDetailVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);

            simpleExoPlayer.seekTo(currentWindow, playBackPosition);
        }
    }

    public void releasePlayer() {
        if (simpleExoPlayer != null) {
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playBackPosition = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    private void hideSystemUI() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @OnClick({R.id.iv_prev, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_prev:
                if (stepPos == 0) {
                    ivPrev.setClickable(false);
                } else {
                    ivNext.setClickable(true);
                    stepPos--;
                    changeSteps();
                }
                break;
            case R.id.iv_next:
                if (stepPos == mRecipe.getSteps().size() - 1) {
                    ivNext.setClickable(false);
                } else {
                    ivPrev.setClickable(true);
                    stepPos++;
                    changeSteps();
                }
                break;
        }
    }

    private void changeSteps() {
        Prefs.putInt(Const.STEP_POS, stepPos);
        releasePlayer();
        recreateFragment();
    }

    private void recreateFragment() {
        StepsDescItemFragment stepsDescItemFragment = StepsDescItemFragment.newInstance(stepPos);
        getFragmentManager().beginTransaction().replace(R.id.frag_steps_detail, stepsDescItemFragment).commit();
    }

}
