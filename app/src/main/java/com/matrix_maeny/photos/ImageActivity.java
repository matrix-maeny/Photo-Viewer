package com.matrix_maeny.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.matrix_maeny.photos.databinding.ActivityImageBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;
    private ScaleGestureDetector gestureDetector;

    private float scaleFactor = 1.0f;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        imagePath = getIntent().getStringExtra("image");
        gestureDetector = new ScaleGestureDetector(ImageActivity.this, new ScaleListener());

        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                Picasso.get().load(file).placeholder(R.drawable.place_holder).into(binding.imageView);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scaleFactor *= gestureDetector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));

            binding.imageView.setScaleX(scaleFactor);
            binding.imageView.setScaleY(scaleFactor);
            return true;
        }


    }

//    private void scaleAnimation() {
//        ScaleAnimation fade_in = new ScaleAnimation(scaleFactor, 1f, scaleFactor, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
//        fade_in.setDuration(1000);     // animation duration in milliseconds
//        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
//        binding.imageView.startAnimation(fade_in);
//        scaleFactor = 1.0f;
//
//    }
}