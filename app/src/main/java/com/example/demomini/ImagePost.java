package com.example.demomini;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImagePost extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_post);
		//checkImgae
		ImageView im= findViewById(R.id.checkImgae);
		if (getIntent().hasExtra("s_Image")) {
			Log.v("TT","Enter In this PArt");
			String val = getIntent().getStringExtra("s_Image");
			Picasso.get().load(val).fit().into(im);

		}
	}
}
