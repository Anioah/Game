package com.example.loteria.ui.home;

import android.widget.ImageView;

class CardInterface {

    ImageView imageView;
    int card_number;
    boolean checked;

    public CardInterface(){
        this.checked = false;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getCard_number() {
        return card_number;
    }

    public boolean isChecked(){
        return this.checked;
    }

    public void checkCard(){
        this.checked = true;
    }

    public void setCard_number(int card_number) {
        this.card_number = card_number;
    }

}
