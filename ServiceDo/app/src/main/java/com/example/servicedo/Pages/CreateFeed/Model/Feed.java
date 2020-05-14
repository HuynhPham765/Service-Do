package com.example.servicedo.Pages.CreateFeed.Model;

import java.util.ArrayList;

public class Feed {
    public enum EnumType {People, Work}
    public enum EnumStatus {
        New, Close, Progress, Done;
    }

    public static final String FEED_STRING = "feed";

    private String feedId;
    private String title;
    private String reward;
    private String address;
    private String description;
    private String type;
    private String status;
    private long createAt;
    private String createBy;
    private String workBy;
    private ArrayList<String> image;

    public Feed() {

    }

    public Feed(String feedId, String title, String reward, String address, String description, String type, String status, long createAt, String createBy, String workBy, ArrayList<String> image) {
        this.feedId = feedId;
        this.title = title;
        this.reward = reward;
        this.address = address;
        this.description = description;
        this.type = type;
        this.status = status;
        this.createAt = createAt;
        this.createBy = createBy;
        this.workBy = workBy;
        this.image = image;
    }

    public Feed(String feedId, String title, String reward, String description, String type, long createAt, String createBy, ArrayList<String> image) {
        this.feedId = feedId;
        this.title = title;
        this.reward = reward;
        this.description = description;
        this.type = type;
        this.createAt = createAt;
        this.createBy = createBy;
        this.image = image;
    }

    public Feed(String title, String reward, String description, String type, long createAt, String createBy, ArrayList<String> image) {
        this.title = title;
        this.reward = reward;
        this.description = description;
        this.type = type;
        this.createAt = createAt;
        this.createBy = createBy;
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkBy() {
        return workBy;
    }

    public void setWorkBy(String workBy) {
        this.workBy = workBy;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "feedId='" + feedId + '\'' +
                ", title='" + title + '\'' +
                ", reward='" + reward + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", createAt=" + createAt +
                ", createBy='" + createBy + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
