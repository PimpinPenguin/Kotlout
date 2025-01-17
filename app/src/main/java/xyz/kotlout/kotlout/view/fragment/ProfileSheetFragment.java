package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.user.User;

public class ProfileSheetFragment extends BottomSheetDialogFragment {

  private static final String ARG_USER = "USER_ID";

  private User user;

  public ProfileSheetFragment() {
    // Required empty public constructor
  }

  public static ProfileSheetFragment newInstance(User user) {
    ProfileSheetFragment fragment = new ProfileSheetFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_USER, user);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      user = (User) getArguments().getSerializable(ARG_USER);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.fragment_profile_sheet, container, false);
    TextView nameView = view.findViewById(R.id.tv_profile_sheet_name);
    TextView emailView = view.findViewById(R.id.tv_profile_sheet_email);
    TextView phoneView = view.findViewById(R.id.tv_profile_sheet_phone_number);
    TextView uuidView = view.findViewById(R.id.tv_profile_sheet_uuid);

    if (user != null) {
      nameView.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
      uuidView.setText(user.getUuid());
      emailView.setText(user.getEmail());
      phoneView.setText(user.getPhoneNumber());
    }

    return view;
  }
}