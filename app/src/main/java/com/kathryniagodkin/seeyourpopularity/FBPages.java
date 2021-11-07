package com.kathryniagodkin.seeyourpopularity;

import java.util.ArrayList;

public class FBPages {

    private String page_id;
    private String name;
    private ArrayList<FBPost> fbPosts;

    public FBPages(String page_id, String name) {
        this.page_id = page_id;
        this.name = name;
//        this.fbPosts = fbPosts;
    }

    public String getPage_id() {
        return page_id;
    }

    public String getName() {
        return name;
    }

    public void setFbPosts(ArrayList<FBPost> fbPosts) {
        this.fbPosts = fbPosts;
    }

    public ArrayList<FBPost> getFbPosts() {
        return fbPosts;
    }
}
