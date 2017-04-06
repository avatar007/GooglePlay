package com.studio.googleplay.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.UiUtils;

import java.util.ArrayList;

/**
 * 应用详情页的安全模块
 */
public class DetailSafeHolder extends BaseHolder<AppInfo> {

    private ImageView[] mSafeIcons;// 安全标识图片
    private ImageView[] mDesIcons;// 安全描述图片
    private TextView[] mSafeDes;// 安全描述文字
    private LinearLayout[] mSafeDesBar;// 安全描述条目(图片+文字)
    private RelativeLayout rlDesRoot;
    private LinearLayout llDesRoot;
    private ImageView ivArrow;
    private LinearLayout.LayoutParams mParams;
    private int mDesHeight;
    private boolean isOpen;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.layout_detail_safeinfo);
        mSafeIcons = new ImageView[4];
        mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
        mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
        mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
        mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);

        mDesIcons = new ImageView[4];
        mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
        mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
        mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
        mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);

        mSafeDes = new TextView[4];
        mSafeDes[0] = (TextView) view.findViewById(R.id.tv_des1);
        mSafeDes[1] = (TextView) view.findViewById(R.id.tv_des2);
        mSafeDes[2] = (TextView) view.findViewById(R.id.tv_des3);
        mSafeDes[3] = (TextView) view.findViewById(R.id.tv_des4);

        mSafeDesBar = new LinearLayout[4];
        mSafeDesBar[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
        mSafeDesBar[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
        mSafeDesBar[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
        mSafeDesBar[3] = (LinearLayout) view.findViewById(R.id.ll_des4);
        llDesRoot = (LinearLayout) view.findViewById(R.id.ll_des_root);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        rlDesRoot = (RelativeLayout) view.findViewById(R.id.rl_des_root);

        rlDesRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });


        return view;
    }

    private void toggle() {
        ValueAnimator animator = null;
        if (isOpen) {//打开状态
            isOpen = false;
            animator = ValueAnimator.ofInt(mDesHeight, 0);
        } else {//收起状态
            isOpen = true;
            animator = ValueAnimator.ofInt(0, mDesHeight);
        }

        //监听动画值得改变
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                mParams.height = height;
                llDesRoot.setLayoutParams(mParams);
            }
        });

        //动画执行结束的回调
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override//动画结束的回调
            public void onAnimationEnd(Animator animation) {
                if (isOpen){//打开箭头朝上
                    ivArrow.setImageResource(R.drawable.arrow_up);
                }else {//关闭箭头朝下
                    ivArrow.setImageResource(R.drawable.arrow_down);
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

    @Override
    public void refreshView(AppInfo data) {
        ArrayList<AppInfo.SafeInfo> safe = data.safe;
        for (int i = 0; i < 4; i++) {
            if (i < safe.size()) {
                AppInfo.SafeInfo safeInfo = safe.get(i);
                mSafeDes[i].setText(safeInfo.safeDes);//安全描述文字
                //安全标识图标
                Picasso.with(UiUtils.getContext()).
                        load(ConstantValue.SERVER_URL + "image?name=" + safeInfo.safeUrl).into(mSafeIcons[i]);
                //安全描述图标
                Picasso.with(UiUtils.getContext()).
                        load(ConstantValue.SERVER_URL + "image?name=" + safeInfo.safeDesUrl).into(mDesIcons[i]);

            } else {
                mSafeIcons[i].setVisibility(View.GONE);
                mSafeDesBar[i].setVisibility(View.GONE);
            }
        }
        // 获取安全描述的完整高度
        llDesRoot.measure(0, 0);
        mDesHeight = llDesRoot.getMeasuredHeight();
        mParams = (LinearLayout.LayoutParams) llDesRoot.getLayoutParams();
        // 修改安全描述布局高度为0,达到隐藏效果
        mParams.height = 0;
        llDesRoot.setLayoutParams(mParams);
    }
}
