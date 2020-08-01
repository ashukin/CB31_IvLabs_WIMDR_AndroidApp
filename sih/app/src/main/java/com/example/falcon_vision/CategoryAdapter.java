package com.example.falcon_vision;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**

 */
public class CategoryAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Today", "Last Week", "Last Month"};

    /**
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public CategoryAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TodayFragment();
        } else if (position == 1) {
            return new LastWeekFragment();
        } else  {
            return new LastMonthFragment();
        }
    }


    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 3;
    }

    /** Return title of the page*/
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}