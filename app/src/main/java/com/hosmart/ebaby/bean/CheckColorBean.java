package com.hosmart.ebaby.bean;

public class CheckColorBean {

    private  int id;
    private boolean checkStart;
    private   int SelectedDrawable;
    private  int unSelectedDrawable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheckStart() {
        return checkStart;
    }

    public void setCheckStart(boolean checkStart) {
        this.checkStart = checkStart;
    }

    public int getSelectedDrawable() {
        return SelectedDrawable;
    }

    public void setSelectedDrawable(int selectedDrawable) {
        SelectedDrawable = selectedDrawable;
    }

    public int getUnSelectedDrawable() {
        return unSelectedDrawable;
    }

    public void setUnSelectedDrawable(int unSelectedDrawable) {
        this.unSelectedDrawable = unSelectedDrawable;
    }
}
