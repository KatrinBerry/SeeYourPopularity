package com.kathryniagodkin.seeyourpopularity;

public class FBPost {

    private String id;
    private String message;
    private String full_picture;
    private Boolean isPopular;

//    public FBPost(String id, String message, String full_picture) {
//        this.id = id;
//        this.message = message;
//        this.full_picture = full_picture;
//    }

    public FBPost(String id, String full_picture, Boolean isPopular) {
        this.id = id;
        this.full_picture = full_picture;
        this.isPopular = isPopular;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getFull_picture() {
        return full_picture;
    }

    public Boolean getPopular() {
        return isPopular;
    }
}
