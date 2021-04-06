package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowMetrics;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button mTransBtn;
    private Button mStartBtn;
    private Button mEndBtn;
    private Button mCancelBtn;
    private Button mPauseBtn;
    private Button mResumeBtn;
    private Button mReverseBtn;
    private ProgressBar mProgressBar;
    private ImageView mMyLove;
    private ImageView mMyLoveIdleBasha;
    private ImageView mMyLoveIdle;


    private ValueAnimator mProgressAnimator;
    private ValueAnimator mColorAnimator;
    private ObjectAnimator mAlphaAnimator;
    private ObjectAnimator mScaleAnimatorX;
    private ObjectAnimator mScaleAnimatorY;
    private AnimatorSet mAnimatorSet;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTransBtn = findViewById(R.id.trans_btn);
        mStartBtn = findViewById(R.id.start_btn);
        mEndBtn = findViewById(R.id.end_btn);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mPauseBtn = findViewById(R.id.pause_btn);
        mResumeBtn = findViewById(R.id.resume_btn);
        mReverseBtn = findViewById(R.id.reverse_btn);
        mProgressBar = findViewById(R.id.progress_bar);
        mMyLove = findViewById(R.id.my_love);
        mMyLoveIdleBasha = findViewById(R.id.my_love_basha);
        mMyLoveIdle = findViewById(R.id.my_love_idle);

        initValueAnimator();
        initObjectAnimator();
        initOnClickListener();

        initXmlObjectAnimator();

        initTransationAnimation();
    }

    private void initTransationAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1.5f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setFillAfter(true);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTransBtn.startAnimation(translateAnimation);
            }
        }, 2000);
    }

    private void initXmlObjectAnimator() {
        //加载xml中属性动画
        AnimatorSet animatorset = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animator_set);
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.animator_alpha);

        //使用组合属性动画
        animatorset.setTarget(mMyLove);
        animatorset.start();

        //使用属性动画
        animator.setTarget(mMyLove);
        animator.start();
    }

    private void initTransationDrawable() {
        Drawable drawableA = mMyLoveIdleBasha.getDrawable();
        Drawable drawableB = mMyLoveIdle.getDrawable();

        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{drawableA, drawableB});
        transitionDrawable.setCrossFadeEnabled(true);
        mMyLoveIdle.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(3000);
    }

    private void initObjectAnimator() {
        mAlphaAnimator = ObjectAnimator.ofFloat(mMyLove, "alpha", 0.2f, 1f);

        mScaleAnimatorX = ObjectAnimator.ofFloat(mMyLove, "scaleX", 1f, 1.1f);
        mScaleAnimatorY = ObjectAnimator.ofFloat(mMyLove, "scaleY", 1f, 1.1f);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(3000);
        mAnimatorSet.playTogether(mScaleAnimatorX, mScaleAnimatorY, mAlphaAnimator);
    }

    private void initOnClickListener() {
        mTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getString(R.string.click_trans_btn), Toast.LENGTH_SHORT).show();
            }
        });

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressAnimator.start();
                mColorAnimator.start();
                mAnimatorSet.start();
                initTransationDrawable();
            }
        });
        mEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressAnimator.end();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressAnimator.cancel();
                Toast.makeText(MainActivity.this, "动画取消", Toast.LENGTH_SHORT).show();
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mProgressAnimator.pause();
                Toast.makeText(MainActivity.this, "动画暂停", Toast.LENGTH_SHORT).show();
            }
        });

        mResumeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mProgressAnimator.resume();
                Toast.makeText(MainActivity.this, "动画继续", Toast.LENGTH_SHORT).show();
            }
        });

        mReverseBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mProgressAnimator.reverse();
                Toast.makeText(MainActivity.this, "动画重复", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initValueAnimator() {
        mProgressAnimator = ValueAnimator.ofInt(0, 100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mColorAnimator = ValueAnimator.ofArgb(0xffffffff, 0xffff0000, 0xff0000ff, 0xff00ff00);
        }
        mColorAnimator.setDuration(3000);
        mProgressAnimator.setDuration(3000);

        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate :: " + animation.getAnimatedValue());
                mProgressBar.setProgress((Integer) animation.getAnimatedValue());
            }
        });

        mColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartBtn.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        mProgressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Toast.makeText(MainActivity.this, "动画开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(MainActivity.this, "动画结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Toast.makeText(MainActivity.this, "动画取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Toast.makeText(MainActivity.this, "动画重复", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}