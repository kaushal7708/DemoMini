package com.example.demomini;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class postactivity extends AppCompatActivity {
	ImageButton btu;
	EditText tt, dd;
	Button btupost;
	Uri Image_path = null;
	String date;
	private final static int GALLERY_REQUEST = 1;
	StorageReference sr;
	DatabaseReference dRef;
	ProgressDialog d;
	String title, desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postactivity);

		btu = findViewById(R.id.img_post);

		tt = findViewById(R.id.title);
		dd = findViewById(R.id.desc);
		sr = FirebaseStorage.getInstance().getReference();
		dRef = FirebaseDatabase.getInstance().getReference().child("post");
		d = new ProgressDialog(this);
		btupost = findViewById(R.id.submit_post);

		btu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType("image/*");
				startActivityForResult(i, GALLERY_REQUEST);
			}
		});

		btupost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startpost();
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void startpost() {

		d.setMessage("Post is uploading...");
		d.show();Log.v("TT", "YTRTRR   start" );
		title = tt.getText().toString().trim();
		desc = dd.getText().toString().trim();
		date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(date) && Image_path != null) {
			StorageReference reference = sr.child("Post_Image").child(Image_path.getLastPathSegment());
			final StorageReference reference2 = sr.child("Post_Image");
			reference.putFile(Image_path).addOnSuccessListener(taskSnapshot -> {
				Toast.makeText(postactivity.this,"Image is Uploaded. ",Toast.LENGTH_LONG).show();
				reference2.child(Image_path.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(uri -> {
					//a.setImage(uri.toString());
					//i=uri;
					DatabaseReference dref2 = dRef.push();
					dref2.child("title").setValue(title);
					dref2.child("name").setValue(desc);
					dref2.child("time").setValue(date);
					dref2.child("Image").setValue(uri.toString());
					Log.v("TT", "YTRTRR   " + uri.toString());
					d.dismiss();
					Toast.makeText(postactivity.this,"Data is Uploaded. ",Toast.LENGTH_LONG).show();
					startActivity(new Intent(postactivity.this, MainActivity.class));
					finish();
				});
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception exception) {
					Log.v("TT",exception.getLocalizedMessage());
				}
			});
		}else{
			d.dismiss();

		}

	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this,MainActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
			Uri img = data.getData();
			Image_path = img;
			btu.setImageURI(img);
		}
	}
}

/*Log.v("TT", "Connected   with "+reference2.getName() );
			reference.putFile(Image_path).addOnSuccessListener(taskSnapshot -> {
				Log.v("TT", "YTRTRR   uploading" );
				Toast.makeText(postactivity.this,"Image is Uploaded. ",Toast.LENGTH_LONG).show();
				reference2.child(Image_path.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(uri -> {
					//a.setImage(uri.toString());
					//i=uri;
					DatabaseReference dref2 = dRef.push();
					dref2.child("title").setValue(title);
					dref2.child("name").setValue(desc);
					dref2.child("time").setValue(date);
					dref2.child("Image").setValue(uri.toString());
					Log.v("TT", "YTRTRR   " + uri.toString());
					d.dismiss();
					Toast.makeText(postactivity.this,"Data is Uploaded. ",Toast.LENGTH_LONG).show();
					startActivity(new Intent(postactivity.this, MainActivity.class));
					finish();
				});
			});

 */
