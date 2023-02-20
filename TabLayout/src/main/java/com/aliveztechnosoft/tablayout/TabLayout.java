package com.aliveztechnosoft.tablayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class TabLayout extends LinearLayout implements View.OnClickListener {

    private final List<TabItem> tabItemsList = new ArrayList<>();
    private final Context context;
    private int fragmentContainer;
    private int selectedItemTextColor;
    private int selectedItemBackgroundColor;
    private int tabItemsTextColor;
    private int selectedTabPosition = 0;
    private int tabAnimateSpeed = 200;
    private TabItemSelectListener tabItemSelectListener;
    private int radius;

    public TabLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setTabAnimateSpeed(int animationSpeedInMs){
        this.tabAnimateSpeed = animationSpeedInMs;
    }

    public void setSelectedItemBackgroundColor(int selectedItemBackgroundColor){
        this.selectedItemBackgroundColor = selectedItemBackgroundColor;
        initTabs();
    }

    public void setTabRadius(int radius){
        this.radius = radius;
        initTabs();
    }

    public void setSelectedItemTextColor(int selectedItemTextColor){
        this.selectedItemTextColor = selectedItemTextColor;
        initTabs();
    }

    public void setTabItemsTextColor(int tabItemsTextColor){
        this.tabItemsTextColor = tabItemsTextColor;
        initTabs();
    }

    public void setSelected(int itemIndex){
        this.selectedTabPosition = itemIndex;
        initTabs();
    }

    public void addTabItem(String itemName) {
        TabItem tabItem = new TabItem(itemName, null);
        TextView textView = createTab(itemName, tabItemsList.size());

        tabItemsList.add(tabItem);
        tabItem.setTextView(textView);

        setWeightSum(tabItemsList.size());

        initTabs();
    }

    public void setOnTabItemSelectedListener(TabItemSelectListener tabItemSelectListener){
        this.tabItemSelectListener = tabItemSelectListener;
    }

    public void addTabItem(String itemName, Fragment fragment, int fragmentContainer) {
        this.fragmentContainer = fragmentContainer;
        TextView textView = createTab(itemName, tabItemsList.size());

        TabItem tabItem = new TabItem(itemName, fragment);
        tabItemsList.add(tabItem);
        tabItem.setTextView(textView);

        setWeightSum(tabItemsList.size());

        initTabs();
    }

    private void init() {
        this.radius = 100;
        this.selectedTabPosition = 0;
        this.fragmentContainer = 0;
        this.tabAnimateSpeed = 200;
        this.tabItemSelectListener = null;
        setOrientation(LinearLayout.HORIZONTAL);
        selectedItemBackgroundColor = Color.parseColor("#FFFFFF");
        selectedItemTextColor = Color.parseColor("#000000");
        tabItemsTextColor = Color.parseColor("#80FFFFFF");
    }

    private void initTabs() {

        setBackground(createBackground(selectedItemBackgroundColor, 0.1f));

        for (int i = 0; i < tabItemsList.size(); i++) {

            final TextView textView = tabItemsList.get(i).getTextView();

            if (i == selectedTabPosition) {
                textView.setTextColor(selectedItemTextColor);
                textView.setBackground(createBackground(Color.WHITE, 1f));
                textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.BOLD));

                if (tabItemsList.get(selectedTabPosition).getFragment() != null) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(fragmentContainer, tabItemsList.get(selectedTabPosition).getFragment(), null)
                            .commit();
                }
            } else {
                textView.setTextColor(tabItemsTextColor);
                textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.NORMAL));
                textView.setBackground(createBackground(Color.TRANSPARENT, 1f));
            }
        }
    }

    private TextView createTab(String text, int position) {

        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);

        textView.setText(text);
        textView.setTag(position);

        textView.setOnClickListener(this);
        addView(textView);

        return textView;

    }

    private Drawable createBackground(int color, float ratio) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setShape(GradientDrawable.RECTANGLE);

        int alpha = Math.round(Color.alpha(color) * ratio);

        int r = Color.red(selectedItemBackgroundColor);
        int g = Color.green(selectedItemBackgroundColor);
        int b = Color.blue(selectedItemBackgroundColor);

        drawable.setColor(Color.argb(alpha, r, g, b));

        return drawable;
    }

    private int convertDpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View view) {

        int tabPosition = (int) view.getTag();

        TextView textView = tabItemsList.get(tabPosition).getTextView();

        float slideTo = (tabPosition - selectedTabPosition) * textView.getWidth();

        // creating translate animation
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(tabAnimateSpeed);

        tabItemsList.get(selectedTabPosition).getTextView().startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }


            @Override
            public void onAnimationEnd(Animation animation) {
                initTabs();

                if(tabItemSelectListener != null){
                    tabItemSelectListener.onItemSelected(tabPosition, tabItemsList.get(tabPosition));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        selectedTabPosition = tabPosition;
    }
}