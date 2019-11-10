package com.example.demomini;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class Login extends AppCompatActivity {
	private FirebaseAuth mAuth;
	private static final int GOOGLE_SIGN = 123;
	private Button login, logout;
	private ImageView im;
	private ProgressBar pb;
	private GoogleSignInClient gsc;
	FirebaseUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login = findViewById(R.id.btu);
		logout = findViewById(R.id.btu1);
		im = findViewById(R.id.img_post);
		pb = findViewById(R.id.progress_circular);

		mAuth = FirebaseAuth.getInstance();


		// Configure Google Sign In
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		gsc = GoogleSignIn.getClient(this, gso);

		user = FirebaseAuth.getInstance().getCurrentUser();
			login.setOnClickListener(v -> SignInGoogle());

			logout.setOnClickListener(v -> signout());

	}

	private void SignInGoogle() {
		pb.setVisibility(View.VISIBLE);
		Intent signIntent = gsc.getSignInIntent();
		Log.d("AND","Go to startactivtyforresult intent");
		startActivityForResult(signIntent, GOOGLE_SIGN);

	}


	@Override
	protected void onStart() {
		super.onStart();
		user = FirebaseAuth.getInstance().getCurrentUser();
		if(user!=null){
			login.setVisibility(View.INVISIBLE);
			logout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("AND","enter to request");
		if (requestCode == GOOGLE_SIGN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			Log.d("AND","enter to condition");
			try {
				GoogleSignInAccount googlesigninaccount = task.getResult(ApiException.class);
				if (googlesigninaccount != null) {
					Log.d("AND","enter not null");
					googlefirebaseauth(googlesigninaccount);

				}
			} catch (ApiException e) {
				Log.d("AND","enter errrorrorororo " + e);
				e.printStackTrace();
			}

		}
	}

	private void googlefirebaseauth(GoogleSignInAccount googlesigninaccount) {
		Log.d("AND", "FireBase Auth Boss hello " + googlesigninaccount.getId());
		AuthCredential credential = GoogleAuthProvider.getCredential(googlesigninaccount.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							pb.setVisibility(View.INVISIBLE);
							Log.d("AND", "sign in success full");

							FirebaseUser user = mAuth.getCurrentUser();
							updateUI(user);
						} else {
							pb.setVisibility(View.INVISIBLE);
							Log.d("AND", "sign in fail");
							Toast.makeText(getApplicationContext(), "something is worng", Toast.LENGTH_LONG).show();
							updateUI(null);
						}
					}
				});
	}

	private void updateUI(FirebaseUser user) {
		if (user != null) {
			String photo = String.valueOf(user.getPhotoUrl());
			Log.d("AND", "pic  "+photo);
		//	Picasso.get().load(photo).into(im);
			startActivity(new Intent(this, MainActivity.class));
			finish();
			login.setVisibility(View.INVISIBLE);
			logout.setVisibility(View.VISIBLE);
		} else {
			login.setVisibility(View.VISIBLE);
			logout.setVisibility(View.INVISIBLE);
		}
	}
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this,MainActivity.class));
	}

	private void signout(){
		FirebaseAuth.getInstance().signOut();
		gsc.signOut().addOnCompleteListener(this, task -> updateUI(null));

	}

}
