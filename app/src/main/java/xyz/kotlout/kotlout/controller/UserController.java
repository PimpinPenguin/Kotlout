package xyz.kotlout.kotlout.controller;

import android.os.Build.VERSION_CODES;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.DoubleSummaryStatistics;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import xyz.kotlout.kotlout.model.user.User;

public class UserController {

  private DocumentReference userDoc;
  private Consumer<User> updateCallback;
  /**
   * Pattern to validate email addresses </br> Regex from: https://emailregex.com, Accessed on
   * Friday March 5th 2021
   */
  private static final Pattern EMAIL_REGEX = Pattern.compile(
      "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
  );

  /**
   * Pattern to validate email addresses </br> Notation taken from Figma diagram: 000-111-2222
   */
  private static final Pattern PHONE_REGEX = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
  private static String TAG = "USER CONTROLLER";

  private User user;

  public static boolean validateEmail(String email) {
    return EMAIL_REGEX.matcher(email).matches();
  }

  public static boolean validatePhoneNumber(String phoneNumber) {
    return PHONE_REGEX.matcher(phoneNumber).matches();
  }

  public UserController(String userId) {
    userDoc = FirebaseController.getFirestore().collection("users").document(userId);
    userDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
      @RequiresApi(api = VERSION_CODES.N)
      @Override
      public void onEvent(
          @Nullable DocumentSnapshot value,
          @Nullable FirebaseFirestoreException error) {
        Log.d(TAG, "Firebase event");
        if(error != null) {
          Log.d(TAG, "Firebase Error: " + error.getMessage());
          return;
        }
        if(value != null && value.exists()) {
          Log.d(TAG, "FOUND VALID UPDATE");
          updateCallback.accept(value.toObject(User.class));
        } else {
          Log.d(TAG, "no update");
        }
      }
    });
  }

  public void setUpdateCallback(Consumer<User> updateCallback)  {
    this.updateCallback = updateCallback;
  }

  public void updateUserData(User newData) {
    userDoc.set(newData);
  }

  public User getUser() {
    return user;
  }

  public static void setInfo(User user, String firstName, String lastName, String email, String phone) {
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPhoneNumber(phone);
  }

  public static User fetchUser(String docName, FirebaseFirestore firestore) {
    final User[] user = {new User()};
    firestore.collection("users").document(docName).get().addOnCompleteListener(
        new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot documentSnapshot = task.getResult();
              if (documentSnapshot.exists()) {
                user[0] = documentSnapshot.toObject(User.class);
              }
            }
          }
        });
    return user[0];
  }
}
