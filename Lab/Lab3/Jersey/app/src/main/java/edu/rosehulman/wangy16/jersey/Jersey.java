package edu.rosehulman.wangy16.jersey;

/**
 * Created by localmgr on 3/18/2018.
 */

public class Jersey {
    private String mName;
    private int mNumber;
    private boolean mIsBlue;

    public Jersey() {
        mName = "ANDROID";
        mNumber = 17;
        mIsBlue = false;
    }

    public Jersey(String name, int quantity, boolean isBlue) {
        mName = name;
        mNumber = quantity;
        mIsBlue = isBlue;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public boolean getColor() {
        return mIsBlue;
    }

    public void setColor(boolean isBlue) {
        mIsBlue = isBlue;
    }
}