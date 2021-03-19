package xyz.kotlout.kotlout.controller;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import xyz.kotlout.kotlout.model.user.User;

/**
 * The user helper function class
 */
public final class UserHelper {

  /**
   * Firestore collection name
   */
  public static final String USER_COLLECTION = "users";
  /**
   * Firestore collection name
   */
  public static final String UUID_INTENT = "uuid";
  /**
   * Pattern to validate email addresses </br> Regex from: https://emailregex.com, Accessed on * Friday March 5th 2021
   */
  private static final Pattern EMAIL_REGEX = Pattern.compile(
      "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
  );
  /**
   * Pattern to validate phone numbers </br> Notation taken from Figma diagram: 000-111-2222
   */
  private static final Pattern PHONE_REGEX = Pattern.compile("^[\\d\\-\\+\\(\\)]*$");
  static Context ctx;

  /**
   * Validate email boolean.
   *
   * @param email the email
   * @return the boolean
   */
  public static boolean validateEmail(String email) {
    return email == null || email.equals("") || EMAIL_REGEX.matcher(email).matches();
  }

  /**
   * Validate phone number boolean.
   *
   * @param phoneNumber the phone number
   * @return the boolean
   */
  public static boolean validatePhoneNumber(String phoneNumber) {
    return phoneNumber == null || phoneNumber.equals("") || PHONE_REGEX.matcher(phoneNumber).matches();
  }


  /**
   * Register user.
   *
   * @param userUUID the user uuid
   */
  public static void registerUser(String userUUID) {
    if (fetchUser(userUUID) == null) {
      User newUser = new User();
      newUser.setUuid(userUUID);
      FirebaseFirestore firestore = FirebaseController.getFirestore();
      firestore.collection(USER_COLLECTION).document(newUser.getUuid()).set(newUser);
    } else {
      throw new RuntimeException("User already registered!");
    }
  }

  /**
   * Sets info.
   *
   * @param user      the user
   * @param firstName the first name
   * @param lastName  the last name
   * @param email     the email
   * @param phone     the phone
   */
  public static void setInfo(User user, String firstName, String lastName, String email, String phone) {
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPhoneNumber(phone);
  }

  /**
   * Fetch user user.
   *
   * @param userUUID the user uuid
   * @return the user
   */
  public static User fetchUser(String userUUID) {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    final CollectionReference collection = firestore.collection(USER_COLLECTION);
    final User[] readUser = {null};
    collection.document(userUUID).get().addOnCompleteListener(snapshot -> {
      if (snapshot.isSuccessful()) {
        DocumentSnapshot document = snapshot.getResult();
        if (document.exists()) {
          Log.d("FIRESTORE", "Document snapshot: " + document.getData());
          readUser[0] = (User) document.toObject(User.class);
        } else {
          Log.d("FIRESTORE", "Document snapshot not found");
        }
      } else {
        Log.d("FIRESTORE", "Get failed with: " + snapshot.getException());
      }
    });
    return readUser[0];
  }

  /**
   * Fetch user user.
   *
   * @param docName   the doc name
   * @param firestore the firestore
   * @return the user
   */
  public static User fetchUser(String docName, FirebaseFirestore firestore) {
    final User[] user = {new User()};
    firestore.collection("users").document(docName).get().addOnCompleteListener(
        task -> {
          if (task.isSuccessful()) {
            DocumentSnapshot documentSnapshot = task.getResult();
            if (documentSnapshot.exists()) {
              user[0] = documentSnapshot.toObject(User.class);
            }
          }
        });
    return user[0];
  }

  public static void initUserHelper(Context context) {
    ctx = context;
  }

  /**
   * Read uuid from storage
   *
   * @return UUID stored in internal storage
   */
  public static String readUUID() {
    assert ctx != null;
    String id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
    assert id != null;
    return id;
  }

  public static void initalizeUser() {
    User user = new User();
    AtomicBoolean foundUser = new AtomicBoolean();
    user.setUuid(readUUID());
    DocumentReference ref = FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION)
        .document(UserHelper.readUUID());
    ref.get().addOnCompleteListener(task -> {
      if (!task.isSuccessful() || task.getResult().get("uuid") == null) {
        ref.set(user);
      }
    });
  }

}
