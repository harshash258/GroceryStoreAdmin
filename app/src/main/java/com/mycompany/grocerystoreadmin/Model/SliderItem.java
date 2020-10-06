package com.mycompany.grocerystoreadmin.Model;

public class SliderItem {
    String description;
    String imageUrl;
    String sliderid;

    public SliderItem(){}
    public SliderItem(String description, String imageUrl, String sliderid) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.sliderid = sliderid;
    }
    public String getDescription() {
        return description;
    }

    public String getSliderid() {
        return sliderid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
