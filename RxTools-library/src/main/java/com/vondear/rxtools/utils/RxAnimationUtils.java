package com.vondear.rxtools.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.interfaces.onUpdateListener;
import com.vondear.rxtools.utils.bitmap.RxImageUtils;

/**
 * Created by Administrator on 2017/3/15.
 */

public class RxAnimationUtils {

    /**
     * 颜色渐变动画
     *
     * @param beforeColor 变化之前的颜色
     * @param afterColor  变化之后的颜色
     * @param listener    变化事件
     */
    public static void animationColorGradient(int beforeColor, int afterColor, final onUpdateListener listener) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), beforeColor, afterColor).setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                textView.setTextColor((Integer) animation.getAnimatedValue());
                listener.onUpdate((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    /**
     * 卡片翻转动画
     *
     * @param beforeView
     * @param AfterView
     */
    public static void cardFilpAnimation(final View beforeView, final View AfterView) {
        Interpolator accelerator = new AccelerateInterpolator();
        Interpolator decelerator = new DecelerateInterpolator();
        if (beforeView.getVisibility() == View.GONE) {
            // 局部layout可达到字体翻转 背景不翻转
            RxImageUtils.invisToVis = ObjectAnimator.ofFloat(beforeView,
                    "rotationY", -90f, 0f);
            RxImageUtils.visToInvis = ObjectAnimator.ofFloat(AfterView,
                    "rotationY", 0f, 90f);
        } else if (AfterView.getVisibility() == View.GONE) {
            RxImageUtils.invisToVis = ObjectAnimator.ofFloat(AfterView,
                    "rotationY", -90f, 0f);
            RxImageUtils.visToInvis = ObjectAnimator.ofFloat(beforeView,
                    "rotationY", 0f, 90f);
        }

        RxImageUtils.visToInvis.setDuration(250);// 翻转速度
        RxImageUtils.visToInvis.setInterpolator(accelerator);// 在动画开始的地方速率改变比较慢，然后开始加速
        RxImageUtils.invisToVis.setDuration(250);
        RxImageUtils.invisToVis.setInterpolator(decelerator);
        RxImageUtils.visToInvis.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationEnd(Animator arg0) {
                if (beforeView.getVisibility() == View.GONE) {
                    AfterView.setVisibility(View.GONE);
                    RxImageUtils.invisToVis.start();
                    beforeView.setVisibility(View.VISIBLE);
                } else {
                    AfterView.setVisibility(View.GONE);
                    RxImageUtils.visToInvis.start();
                    beforeView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator arg0) {

            }

            @Override
            public void onAnimationRepeat(Animator arg0) {

            }

            @Override
            public void onAnimationStart(Animator arg0) {

            }
        });
        RxImageUtils.visToInvis.start();
    }

    /**
     * 缩小
     *
     * @param view
     */
    public static void zoomIn(final View view, float scale, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    /**
     * f放大
     *
     * @param view
     */
    public static void zoomOut(final View view, float scale) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    public static void ScaleUpDowm(View view) {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        view.startAnimation(animation);
    }

    /**
     * 抖动动画
     *
     * @param CycleTimes 动画重复的次数
     *                   抖动方式：左上右下
     */
    public static Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 6, 0, 6);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }


    public static Animation RotateAnim(int CycleTimes) {
        Animation translateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        translateAnimation.setRepeatCount(CycleTimes);
        translateAnimation.setRepeatMode(Animation.RESTART);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }


    /**
     * 属性修改控件的高度
     *
     * @param start
     * @param end
     * @param view
     */
    public static void animateHeight(int start, int end, final View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);//设置高度
            }
        });
        valueAnimator.start();
    }


    /**
     * 属性修改控件的高度
     *
     * @param start
     * @param end
     * @param view
     */
    public static void animateMaginTop(int start, int end, final View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                layoutParams.topMargin = value;
                view.setLayoutParams(layoutParams);//设置高度
            }
        });
        valueAnimator.start();
    }

    /**
     * 属性修改控件的宽度
     *
     * @param start
     * @param end
     * @param view
     */
    public static void animateWidth(int start, int end, final View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = value;
                view.setLayoutParams(layoutParams);//设置高度
            }
        });
        valueAnimator.start();
    }


    /**
     * 控件点击反馈动画，控件会放大，然后复原
     *
     * @param view
     * @param duration
     * @param scaleX
     * @param scaleY
     */
    public static void doAction(ViewGroup view, int duration, float scaleX, float scaleY) {
        try {
            if (view.getScaleX() == 1) {
                ViewCompat.animate(view).setDuration(duration).scaleX(scaleX).scaleY(scaleY).setInterpolator(new CycleInterpolator(0.5f))
                        .setListener(new ViewPropertyAnimatorListener() {

                            @Override
                            public void onAnimationStart(final View view) {
                            }

                            @Override
                            public void onAnimationEnd(final View v) {
                            }

                            @Override
                            public void onAnimationCancel(final View view) {
                            }
                        }).withLayer().start();

                for (int index = 0; index < ((ViewGroup) view).getChildCount(); ++index) {
                    View nextChild = ((ViewGroup) view).getChildAt(index);
                    ViewCompat.animate(nextChild).setDuration(duration).scaleX(scaleX).scaleY(scaleY).setInterpolator(new CycleInterpolator(0.5f))
                            .setListener(new ViewPropertyAnimatorListener() {

                                @Override
                                public void onAnimationStart(final View view) {
                                }

                                @Override
                                public void onAnimationEnd(final View v) {
                                }

                                @Override
                                public void onAnimationCancel(final View view) {
                                }
                            }).withLayer().start();
                }
            }
        } catch (Exception e) {
            RxLogUtils.e("only ViewGroups : likes RelativeLayout, LinearLayout, etc could doAction");
        }
    }


    /**
     * * 控件点击反馈动画，控件会放大，然后复原
     *
     * @param view
     * @param duration
     * @param scaleX
     * @param scaleY
     */
    public static void doAction(View view, int duration, float scaleX, float scaleY) {
        try {
            if (view.getScaleX() == 1) {
                ViewCompat.animate(view).setDuration(duration).scaleX(scaleX).scaleY(scaleY).setInterpolator(new CycleInterpolator(0.5f))
                        .setListener(new ViewPropertyAnimatorListener() {

                            @Override
                            public void onAnimationStart(final View view) {
                            }

                            @Override
                            public void onAnimationEnd(final View v) {
                            }

                            @Override
                            public void onAnimationCancel(final View view) {
                            }
                        }).withLayer().start();
            }
        } catch (Exception e) {
            RxLogUtils.e("only ViewGroups : likes RelativeLayout, LinearLayout, etc could doAction");
        }
    }


    /**
     * 倒计时动画
     *
     * @param text_countDown 控件
     **/
    public static void countDownAnim(final TextView text_countDown) {
        try {
            int repeatTimes = Integer.parseInt(text_countDown.getText().toString());
            final AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(text_countDown, "scaleX", 0.0f, 1.0f);
            ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(text_countDown, "scaleY", 0.0f, 1.0f);
            ObjectAnimator mAnimatorAlpha = ObjectAnimator.ofFloat(text_countDown, "alpha", 0.0f, 1.0f);
            mAnimatorScaleX.setRepeatCount(repeatTimes);
            mAnimatorScaleY.setRepeatCount(repeatTimes);
            mAnimatorAlpha.setRepeatCount(repeatTimes);
            //mAnimatorScaleX设置的监听只执行开始、结束、者重复
            //animatorSet设置的监听只能运行start和end,不能监听到重复
            mAnimatorScaleX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    RxLogUtils.d("重复");

                    int i = Integer.parseInt(text_countDown.getText().toString());
                    i--;
                    text_countDown.setText(i + "");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    text_countDown.setVisibility(View.GONE);
                }
            });

            animatorSet.playTogether(mAnimatorScaleX, mAnimatorScaleY, mAnimatorAlpha);
            animatorSet.setDuration(1000);
            animatorSet.start();
        } catch (ClassCastException e) {
            throw new ClassCastException("TextView  setText('')的值必须为正整数");
        }
    }


    /**
     * 点击反馈动画
     *
     * @param view     视图
     * @param duration 时间
     */
    public static void doHeartBeat(View view, final int duration) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.4f, 0.9f, 1.0f),
                        ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.4f, 0.9f, 1.0f)
                );
                set.setDuration(duration);
                set.start();
            }
        });
    }

    /**
     * 点击反馈动画
     *
     * @param view 视图
     */
    public static void doHeartBeat(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.4f, 0.9f, 1.0f),
                        ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.4f, 0.9f, 1.0f)
                );
                set.setDuration(500);
                set.start();
            }
        });
    }


    /**
     * @param triggerView 事件View
     *                    <p>
     *                    带水波动画的Activity跳转
     */

    @SuppressLint("NewApi")
    public static void navigateWithRippleCompat(final Activity activity, final Intent intent,
                                                final View triggerView, @ColorRes int color) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat option = ActivityOptionsCompat.makeClipRevealAnimation(triggerView, 0, 0,
                    triggerView.getMeasuredWidth(), triggerView.getMeasuredHeight());
            ActivityCompat.startActivity(activity, intent, option.toBundle());
            return;
        }

        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth() / 2;
        final int cy = location[1] + triggerView.getHeight() / 2;
        final ImageView view = new ImageView(activity);
        view.setImageResource(color);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);
        int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                decorView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        decorView.removeView(view);
                    }
                }, 500);
            }
        });
        anim.start();
    }

    /**
     * 带水波动画
     */
    @SuppressLint("NewApi")
    public static void navigateWithRippleCompat(final Activity activity,
                                                final View triggerView, @ColorRes int color) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat option = ActivityOptionsCompat.makeScaleUpAnimation(triggerView, 0, 0,
                    triggerView.getMeasuredWidth(), triggerView.getMeasuredHeight());
            return;
        }

        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth() / 2;
        final int cy = location[1] + triggerView.getHeight() / 2;
        final ImageView view = new ImageView(activity);
        view.setImageResource(color);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);
        int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                decorView.removeView(view);
            }
        });
        anim.start();
    }


    /**
     * 水波纹渐渐出现的动画
     * 需要注意的是，当按钮显示的时候会响应用户点击事件，当随渐变动画消失之后，再次点击会失去响应，此外如果我们想要改变波纹的颜色
     * 可以在xml中修改 android:colorControlHighlight 的属性
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void doCircle(View myView) {
        // get the center for the clipping circle
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;
        doCircle(myView, cx, cy);
    }

    /**
     * 水波纹渐渐出现的动画
     * 需要注意的是，当按钮显示的时候会响应用户点击事件，当随渐变动画消失之后，再次点击会失去响应，此外如果我们想要改变波纹的颜色
     * 可以在xml中修改 android:colorControlHighlight 的属性
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void doCircle(View myView, int cx, int cy) {
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        anim.setDuration(3000);
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }


    /**
     * 水波纹渐渐消失的动画
     * 需要注意的是，当按钮显示的时候会响应用户点击事件，当随渐变动画消失之后，再次点击会失去响应，此外如果我们想要改变波纹的颜色
     * 可以在xml中修改 android:colorControlHighlight 的属性
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hideCircle(final View myView) {
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;
        hideCircle(myView, cx, cy);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hideCircle(final View myView, int cx, int cy) {
        int finalRadius = myView.getWidth();
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(3000);
        anim.start();
    }

    /**
     * 跳跃动画
     *
     * @param view       视图
     * @param jumpHeight 跳跃高度
     * @param duration   时间
     * @return Animator
     */
    public static Animator doHappyJump(View view, int jumpHeight, int duration) {
        Keyframe scaleXFrame1 = Keyframe.ofFloat(0f, 1.0f);
        Keyframe scaleXFrame2 = Keyframe.ofFloat(0.05f, 1.5f);
        Keyframe scaleXFrame3 = Keyframe.ofFloat(0.1f, 0.8f);
        Keyframe scaleXFrame4 = Keyframe.ofFloat(0.15f, 1.0f);
        Keyframe scaleXFrame5 = Keyframe.ofFloat(0.5f, 1.0f);
        Keyframe scaleXFrame6 = Keyframe.ofFloat(0.55f, 1.5f);
        Keyframe scaleXFrame7 = Keyframe.ofFloat(0.6f, 0.8f);
        Keyframe scaleXFrame8 = Keyframe.ofFloat(0.65f, 1.0f);
        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofKeyframe("scaleX",
                scaleXFrame1, scaleXFrame2, scaleXFrame3, scaleXFrame4,
                scaleXFrame5, scaleXFrame6, scaleXFrame7, scaleXFrame8);

        Keyframe scaleYFrame1 = Keyframe.ofFloat(0f, 1.0f);
        Keyframe scaleYFrame2 = Keyframe.ofFloat(0.05f, 0.5f);
        Keyframe scaleYFrame3 = Keyframe.ofFloat(0.1f, 1.15f);
        Keyframe scaleYFrame4 = Keyframe.ofFloat(0.15f, 1.0f);
        Keyframe scaleYFrame5 = Keyframe.ofFloat(0.5f, 1.0f);
        Keyframe scaleYFrame6 = Keyframe.ofFloat(0.55f, 0.5f);
        Keyframe scaleYFrame7 = Keyframe.ofFloat(0.6f, 1.15f);
        Keyframe scaleYFrame8 = Keyframe.ofFloat(0.65f, 1.0f);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofKeyframe("scaleY",
                scaleYFrame1, scaleYFrame2, scaleYFrame3, scaleYFrame4,
                scaleYFrame5, scaleYFrame6, scaleYFrame7, scaleYFrame8);

        Keyframe translationY1 = Keyframe.ofFloat(0f, 0f);
        Keyframe translationY2 = Keyframe.ofFloat(0.085f, 0f);
        Keyframe translationY3 = Keyframe.ofFloat(0.2f, -jumpHeight);
        Keyframe translationY4 = Keyframe.ofFloat(0.25f, -jumpHeight);
        Keyframe translationY5 = Keyframe.ofFloat(0.375f, 0f);
        Keyframe translationY6 = Keyframe.ofFloat(0.5f, 0f);
        Keyframe translationY7 = Keyframe.ofFloat(0.585f, 0f);
        Keyframe translationY8 = Keyframe.ofFloat(0.7f, -jumpHeight);
        Keyframe translationY9 = Keyframe.ofFloat(0.75f, -jumpHeight);
        Keyframe translationY10 = Keyframe.ofFloat(0.875f, 0f);
        PropertyValuesHolder translationYHolder = PropertyValuesHolder.ofKeyframe("translationY",
                translationY1, translationY2, translationY3, translationY4, translationY5,
                translationY6, translationY7, translationY8, translationY9, translationY10);

        Keyframe rotationY1 = Keyframe.ofFloat(0f, 0f);
        Keyframe rotationY2 = Keyframe.ofFloat(0.125f, 0f);
        Keyframe rotationY3 = Keyframe.ofFloat(0.3f, -360f * 3);
        PropertyValuesHolder rotationYHolder = PropertyValuesHolder.ofKeyframe("rotationY",
                rotationY1, rotationY2, rotationY3);

        Keyframe rotationX1 = Keyframe.ofFloat(0f, 0f);
        Keyframe rotationX2 = Keyframe.ofFloat(0.625f, 0f);
        Keyframe rotationX3 = Keyframe.ofFloat(0.8f, -360f * 3);
        PropertyValuesHolder rotationXHolder = PropertyValuesHolder.ofKeyframe("rotationX",
                rotationX1, rotationX2, rotationX3);

        ValueAnimator valueAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                scaleXHolder, scaleYHolder, translationYHolder, rotationYHolder, rotationXHolder);
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        return valueAnimator;
    }

}
