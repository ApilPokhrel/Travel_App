package com.example.coreandroid.entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {
   private LatLng position;
   private String title;
   private String snippet;
   private int iconPicture;
   private String iconPictureUrl;
   private PlaceModel placeModel;

    public ClusterMarker(LatLng position, String title, String snippet, int iconPicture, String iconPictureUrl, PlaceModel placeModel) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.iconPicture = iconPicture;
        this.iconPictureUrl = iconPictureUrl;
        this.placeModel = placeModel;
    }

    public ClusterMarker() {
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(int iconPicture) {
        this.iconPicture = iconPicture;
    }

    public PlaceModel getPlaceModel() {
        return placeModel;
    }

    public void setPlaceModel(PlaceModel placeModel) {
        this.placeModel = placeModel;
    }

    public String getIconPictureUrl() {
        return iconPictureUrl;
    }

    public void setIconPictureUrl(String iconPictureUrl) {
        this.iconPictureUrl = iconPictureUrl;
    }
}
