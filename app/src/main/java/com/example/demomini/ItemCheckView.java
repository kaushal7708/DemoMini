package com.example.demomini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemCheckView extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_check_view);
		ImageView im = findViewById(R.id.pic);
		TextView ti = findViewById(R.id.title);

		TextView dc = findViewById(R.id.desc_view);
		TextView t = findViewById(R.id.tim);
		if (getIntent().hasExtra("Image") && getIntent().hasExtra("time")) {
			String val = getIntent().getStringExtra("Image");
			String val2 = getIntent().getStringExtra("time");
			String val3 = getIntent().getStringExtra("title");
			String val4 = getIntent().getStringExtra("desc");
			Picasso.get().load(val).fit().into(im);
			t.setText(val2);

			ti.setText(val3);
			dc.setText(val4);

		}
		im.setOnClickListener(view->{

			if (getIntent().hasExtra("Image")) {
				String val = getIntent().getStringExtra("Image");
				Intent i = new Intent(getApplicationContext(),ImagePost.class);
				i.putExtra("s_Image",val);
				Log.v("TT","Image View Only");
				startActivity(i);


			}
		});
				}

				}
