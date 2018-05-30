package edu.rosehulman.wangy16.comicviewer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ComicPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<ComicWrapper> comicWrappers = new ArrayList<>();
    private int comicCount=5;

    public ComicPagerAdapter(FragmentManager fm) {
        super(fm);
        for (int i = 0; i < 5; i++) {
            comicWrappers.add(new ComicWrapper(i));
        }
    }

    public ArrayList<ComicWrapper> getComicWrappers() {
        return comicWrappers;
    }

    public void setComicWrappers(ArrayList<ComicWrapper> comicWrappers) {
        this.comicWrappers = comicWrappers;
    }

    public int getComicCount() {
        return comicCount;
    }

    public void setComicCount(int comicCount) {
        this.comicCount = comicCount;
    }

    public void addComicWrapper() {
        comicWrappers.add(new ComicWrapper(comicWrappers.size()));
        comicCount++;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return ComicFragment.newInstance(comicWrappers.get(position));
    }

    @Override
    public int getCount() {
        return comicWrappers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Issue: " + comicWrappers.get(position).getXkcdIssue();
    }
}
