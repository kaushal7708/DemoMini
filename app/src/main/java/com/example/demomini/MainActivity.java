package com.example.demomini;

import com.example.demomini.R.layout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.MenuItem;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	DatabaseReference databaseReference;
	private RecyclerView recyclerView;
	private FirebaseRecyclerAdapter adapter;
	NavigationView navigationView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		FloatingActionButton fab = findViewById(R.id.fab);

		recyclerView = findViewById(R.id.list_here);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		databaseReference = FirebaseDatabase.getInstance().getReference().child("post");

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Only Admin use this feature", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		navigationView = findViewById(R.id.nav_view);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		navigationView.setNavigationItemSelectedListener(this);
		checkuser();
		fetch();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			NotificationChannel channel = new NotificationChannel("Noty","Noty", NotificationManager.IMPORTANCE_DEFAULT);
			NotificationManager manager = getSystemService(NotificationManager.class);
			manager.createNotificationChannel(channel);
		}
		FirebaseMessaging.getInstance().subscribeToTopic("weather")
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						String msg ="Done";
						if (!task.isSuccessful()) {
							msg = "Fail";
						}
						Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				});

	}


	private void checkuser() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		if (user != null) {
			String str = user.getDisplayName();
			//	View headerView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
			View headerView = navigationView.getHeaderView(0);
			Log.d("AND","elce wolllksdkjsnkdjvb              hello bosssss "+str);
			TextView navUsername = headerView.findViewById(R.id.gname);
			TextView user_mail = headerView.findViewById(R.id.user_email_id);
			ImageView img = headerView.findViewById(R.id.profilepic);
			String mail = user.getEmail();
			Log.d("AND","elce wolllksdkjsnkdjvb              hello bosssss mail "+mail);
			String imgurl = String.valueOf(user.getPhotoUrl());
			Picasso.get().load(imgurl).into(img);
			navUsername.setText(str);
			user_mail.setText(mail);
		} else {
//			View headerView = navigationView.getHeaderView(0);
//			//	View headerView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
//			TextView navUsername = headerView.findViewById(R.id.gname);
			Log.d("AND","elce wolllksdkjsnkdjvb               jkdfnvkj            jkndfkj");
//			TextView Email = headerView.findViewById(R.id.email);
//			ImageView img = headerView.findViewById(R.id.profilepic);
//			Email.setText("Name@gmail.com");
//			navUsername.setText("User Name");
//			img.setImageResource(R.drawable.ic_firebase_logo);
			startActivity(new Intent(this, Login.class));
			finish();
		}
	}

	private void fetch() {
		Query query = FirebaseDatabase.getInstance()
				.getReference()
				.child("post");

		FirebaseRecyclerOptions<postDetails> options =
				new FirebaseRecyclerOptions.Builder<postDetails>()
						.setQuery(query, new SnapshotParser<postDetails>() {
							@NonNull
							@Override
							public postDetails parseSnapshot(@NonNull DataSnapshot snapshot) {
								Log.v("TT", "Imsge tr  sd   " + snapshot.child("Image").getValue().toString());


								return new postDetails(snapshot.child("title").getValue().toString(),
										snapshot.child("name").getValue().toString(),
										snapshot.child("time").getValue().toString(),
										snapshot.child("Image").getValue().toString());
							}
						})
						.build();

		adapter = new FirebaseRecyclerAdapter<postDetails, postHolder>(options) {
			@Override
			public postHolder onCreateViewHolder(ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext())
						.inflate(layout.ramlal, parent, false);

				return new postHolder(view);
			}


			@Override
			protected void onBindViewHolder(postHolder holder, final int position, postDetails model) {
				holder.setDept_name(model.getTitle());
				holder.setPost_time(model.getTime());
				holder.setPost_text(model.getName());
				holder.setPost_Image(model.getImage());

				holder.cv.setOnClickListener(view ->{
					Intent i = new Intent(getApplicationContext(),ItemCheckView.class);

					i.putExtra("Image",model.getImage());
					i.putExtra("time",model.getTime());
					i.putExtra("title",model.getTitle());
					i.putExtra("desc",model.getName());
					Log.v("TT","Check box");
					startActivity(i);

				});

				holder.post_Image.setOnClickListener(view->{

						Intent i = new Intent(getApplicationContext(),ImagePost.class);
						i.putExtra("s_Image",model.getImage());
						Log.v("TT","I click Main ");
						startActivity(i);
				});
				holder.details.setOnClickListener(view -> {
					Intent i = new Intent(getApplicationContext(),ItemCheckView.class);

					i.putExtra("Image",model.getImage());
					i.putExtra("time",model.getTime());
					i.putExtra("title",model.getTitle());
					i.putExtra("desc",model.getName());
					Log.v("TT","Ho gya kaand");
					startActivity(i);
//					holder.t.setVisibility(View.VISIBLE);
//					//Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
//					holder.t.setText("");
//					holder.query.setEnabled(true);
//					holder.details.setEnabled(false);
				});
//				holder.query.setOnClickListener(view -> {
//					String s = holder.t.getText().toString();
//					Toast.makeText(MainActivity.this, "You select "+String.valueOf(position+1)+" And "+s, Toast.LENGTH_SHORT).show();
//					holder.t.setVisibility(View.GONE);
//					holder.details.setEnabled(true);
//					holder.query.setEnabled(false);
//				});
			}

		};
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	//34861014
	//android:background="#749FEB"


	@Override
	protected void onStop() {
		super.onStop();
		adapter.stopListening();
	}


	@Override
	protected void onResume() {
		super.onResume();
		checkuser();
		adapter.startListening();
	}

	@Override
	protected void onStart() {
		super.onStart();
		checkuser();
		adapter.startListening();
	}

	public static class postHolder extends RecyclerView.ViewHolder {
		View view;
		TextView dept_name, post_time, post_text;
		CardView cv;
		Button query, details;
		ImageView dept_Image, post_Image;
		EditText t;
		public postHolder(@NonNull View itemView) {
			super(itemView);
			details=itemView.findViewById(R.id.std_details);
			t=itemView.findViewById(R.id.editText);
			cv = itemView.findViewById(R.id.head_holder);
			dept_name = itemView.findViewById(R.id.dept_name);
			post_time = itemView.findViewById(R.id.post_time);
			post_text = itemView.findViewById(R.id.post_text);
			post_Image = itemView.findViewById(R.id.image_post);
			view = itemView;
		}

		public void setDept_name(String name) {
			dept_name.setText(name);
		}

		public void setPost_time(String time) {
			post_time.setText(time);
		}

		public void setPost_text(String text) {
			post_text.setText(text);
		}

		public void setPost_Image(String Image) {

			Picasso.get().load(Image).fit().into(post_Image);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_home) {
			// Handle the camera action
			startActivity(new Intent(this, MainActivity.class));
			finish();
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_tools) {

		} else if (id == R.id.nav_share) {
			FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
			if (user != null) {
				String mail = user.getEmail();
				if (mail.equals("kaushal7708@gmail.com")) {
					startActivity(new Intent(this, postactivity.class));
					finish();
				}else{
					Toast.makeText(this,"Only Admin can post ",Toast.LENGTH_LONG).show();
				}

			}
		} else if (id == R.id.nav_send) {
			startActivity(new Intent(this,Login.class));
		}

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
