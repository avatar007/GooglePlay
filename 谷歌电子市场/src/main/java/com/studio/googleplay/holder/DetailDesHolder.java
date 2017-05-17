package com.studio.googleplay.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2017/4/6.
 */
public class DetailDesHolder extends BaseHolder<AppInfo> {
    private TextView tv_des, tv_author;
    private RelativeLayout rl_toggle;
    private ImageView iv_arrow;
    private boolean isOpen;
    private LinearLayout.LayoutParams mParams;
    private int longHeight;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.layout_detail_desinfo);
        tv_des = (TextView) view.findViewById(R.id.tv_detail_des);
        rl_toggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
        tv_author = (TextView) view.findViewById(R.id.tv_detail_author);
        iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);

        rl_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        tv_des.setText(data.des);
        tv_author.setText(data.author);

        // 放在消息队列中运行, 解决当只有三行描述时也是7行高度的bug
        tv_des.post(new Runnable() {
            @Override
            public void run() {
                longHeight = tv_des.getMeasuredHeight();
                int shortHeight = getShortHeight();
                mParams = (LinearLayout.LayoutParams) tv_des.getLayoutParams();
                //设置默认最多显示7行的高度
                mParams.height = shortHeight;
                tv_des.setLayoutParams(mParams);
            }
        });


    }

    private void toggle() {
        int shortHeight = getShortHeight();
        ValueAnimator animator = null;
        if (isOpen) {//打开状态点击关闭
            isOpen = false;
            if (shortHeight < longHeight) {
                animator = ValueAnimator.ofInt(longHeight, shortHeight);
            }
        } else {//关闭状态点击打开
            isOpen = true;
            if (shortHeight < longHeight) {
                animator = ValueAnimator.ofInt(shortHeight, longHeight);
            }
        }
        if (animator != null) {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int height = (int) animation.getAnimatedValue();
                    mParams.height = height;
                    tv_des.setLayoutParams(mParams);
                }
            });

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    final ScrollView scrollView = getScrollView();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    if (isOpen) {
                        iv_arrow.setImageResource(R.drawable.arrow_up);
                    } else {
                        iv_arrow.setImageResource(R.drawable.arrow_down);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.setDuration(200);
            animator.start();
        }

    }

    private int getShortHeight() {
        //通过克隆一个同样的textView,来获取7行的高度
        TextView tv = new TextView(UiUtils.getContext());
        tv.setText(getData().des);//显示内容也一致
        tv.setMaxLines(7);//设置最大行数7行
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//文字大小也一致
        //获取tv的宽度和高度模式
        int width = tv_des.getMeasuredWidth();//获取控件宽度,宽度不变
        //宽度模式是确定的,原空间是包裹内容
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //高度模式为至多模式,大小2000像素足够
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);
        //重新测量,参数不能写0,0,写0,0是在布局中申明过的控件,这里是直接new出来的
        tv.measure(widthMeasureSpec, heightMeasureSpec);

        return tv.getMeasuredHeight();
    }

    private ScrollView getScrollView() {
        ViewParent parent = tv_des.getParent();
        while (!(parent instanceof ScrollView)) {
            parent = parent.getParent();
        }
        return (ScrollView) parent;
    }


}
