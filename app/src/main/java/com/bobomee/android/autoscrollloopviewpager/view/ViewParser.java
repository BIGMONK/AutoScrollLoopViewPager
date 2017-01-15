/*
 * Copyright (C) 2017.  BoBoMEe(wbwjx115@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.bobomee.android.autoscrollloopviewpager.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.bobomee.android.autoscrollloopviewpager.adapter.FragmentStateAdapter;
import com.bobomee.android.autoscrollloopviewpager.transformer.RotateTransformer;
import com.bobomee.android.drawableindicator.widget.BaseIndicator;
import com.bobomee.android.scrollloopviewpager.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created on 2017/1/14.下午3:28.
 *
 * @author bobomee.
 */

public final class ViewParser {

  public static View parseView(LayoutInflater inflater, int layoutResId, ViewGroup root) {
    return inflater.inflate(layoutResId, root, false);
  }

  private static AutoScrollViewPager scrollVp(View _view, int scrollId) {
    AutoScrollViewPager viewPager = (AutoScrollViewPager) _view.findViewById(scrollId);
    viewPager.setFocusable(true);

    return viewPager;
  }

  private static BaseIndicator indicatorVp(View _view, int indicatorId, ViewPager viewPager) {
    BaseIndicator pageIndex = (BaseIndicator) _view.findViewById(indicatorId);
    pageIndex.setViewPager(viewPager);

    return pageIndex;
  }

  private static FragmentStateAdapter adapterVp(FragmentManager _fragmentManager,
      ViewPager _viewPager) {
    FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(_fragmentManager);
    _viewPager.setAdapter(fragmentStateAdapter);
    return fragmentStateAdapter;
  }

  public static void scrollLeftVp(FragmentManager _fragmentManager, View _view, int scrollId,
      int indicatorId) {

    AutoScrollViewPager viewPager = scrollVp(_view, scrollId);
    FragmentStateAdapter fragmentStateAdapter = adapterVp(_fragmentManager, viewPager);
    BaseIndicator indicator = indicatorVp(_view, indicatorId, viewPager);

    viewPager.setDirection(AutoScrollViewPager.LEFT);

    viewPager.startAutoScroll();
  }

  public static void scrollRightVp(FragmentManager _fragmentManager, View view, int scrollId,
      int indicatorId) {
    AutoScrollViewPager viewPager = scrollVp(view, scrollId);
    FragmentStateAdapter fragmentStateAdapter = adapterVp(_fragmentManager, viewPager);
    BaseIndicator indicator = indicatorVp(view, indicatorId, viewPager);

    viewPager.setDirection(AutoScrollViewPager.RIGHT);

    viewPager.startAutoScroll();
  }

  public static void scroll3D(FragmentManager _fragmentManager, View view, int scrollId,
      int indicatorId, int container) {

    final AutoScrollViewPager viewPager = scrollVp(view, scrollId);
    FragmentStateAdapter fragmentStateAdapter = adapterVp(_fragmentManager, viewPager);
    BaseIndicator indicator = indicatorVp(view, indicatorId, viewPager);

    viewPager.setDirection(AutoScrollViewPager.LEFT);
    viewPager.setPageTransformer(true, new RotateTransformer());

    //设置幕后item的缓存数目
    viewPager.setOffscreenPageLimit(fragmentStateAdapter.getCount());

    //设置页与页之间的间距
    int margin = ((ViewGroup.MarginLayoutParams) viewPager.getLayoutParams()).leftMargin;
    viewPager.setPageMargin(-(px2dip(view.getContext(), margin)) / 2);

    //将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
    RelativeLayout vpContainer = (RelativeLayout) view.findViewById(container);
    vpContainer.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return viewPager.dispatchTouchEvent(event);
      }
    });

    viewPager.startAutoScroll();
  }

  private static int px2dip(Context context, int pxValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }
}