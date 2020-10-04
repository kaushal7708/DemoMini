package com.example.demomini;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Notyservice extends FirebaseMessagingService {
	@Override
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		noty(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

	}

	public  void noty(String title,String body) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Noty")
				.setContentText(title)
				.setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
				.setAutoCancel(true)
				.setContentText(body);
		NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
		managerCompat.notify(999,builder.build());

	}
}
