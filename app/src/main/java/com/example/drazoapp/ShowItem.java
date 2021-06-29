package com.example.drazoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;


public class ShowItem extends AppCompatActivity {

    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        imageSlider = findViewById(R.id.slider_show_item);

        String url = "https://cdn.staticneo.com/w/naruto/Nprofile2.jpg";

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(url, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel("https://www.tamashiinations.com/product_detail/shf_db/images/goku_ss4/img_goku_ss4.png", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel("https://photos.tf1.fr/780/780/perso-showpage-dragon-ball-z-v3-aab796-42e2b9-0@1x.png", ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdQJWKXhm8ake5pjrtHdeKQJZe8FXm0IrL2w&usqp=CAU","4 IMAGE", ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);
    }
}