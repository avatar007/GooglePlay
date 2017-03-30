package com.studio.googleplay.fragment;

import java.util.HashMap;

/**
 * fragment的工厂类,生产fragment
 */
public class FragmentFactory {
    private static HashMap<Integer, BaseFragment> sFragmentMap = new HashMap<>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = sFragmentMap.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new SubjectFragment();
                    break;
                case 4:
                    fragment = new RecommendFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;
            }
            sFragmentMap.put(position, fragment);
        }
        return fragment;
    }
}
