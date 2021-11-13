package com.raym.kalk.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.raym.kalk.R;
import com.raym.kalk.models.Course;
import com.raym.kalk.models.KalkDataManager;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    private final ArrayList<Course> mCourseArrayList = new ArrayList<>();
    private EditText mCourseCodeEditText;
    private EditText mCreditUnitEditText;
    private Button mRemoveCourseButton;
    private Button mAddCourseButton;
    private Course mSingleCourse;
    private String mCourseCode;
    private int mCreditUnit;
    private final ArrayList<Course> mCoursesArrayList = new ArrayList<>();
    static String EMPTY = "";
    private InputMethodManager mInputMethodManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register_course, container, false);

        mCourseCodeEditText = root.findViewById(R.id.editTxt_course_code);
        mCreditUnitEditText = root.findViewById(R.id.editTxt_credit_unit);
        mRemoveCourseButton = root.findViewById(R.id.remove_course);
        mAddCourseButton = root.findViewById(R.id.button_add_course);

        mAddCourseButton.setOnClickListener(view -> {
            //get the course code and credit unit from the user and convert them to a string and
            //and int respectively assign them to their respective variables and clear their fields
            /*for course code*/
            if (mCourseCodeEditText.getText().toString().equals("") || mCreditUnitEditText.getText().toString().equals("")) {
                hideKeyBoard();
                Snackbar.make(root, "Cannot Input Empty Values for Either Course code or Credit Unit, DUH!!", Snackbar.LENGTH_SHORT).show();
            } else if (mCourseCodeEditText.getText().toString().length() < 3 || Integer.parseInt(mCreditUnitEditText.getText().toString()) < 0) {
                hideKeyBoard();
                Snackbar.make(root, "Please input the Correct items for the either fields", Snackbar.LENGTH_SHORT).show();
            } else {
                hideKeyBoard();
                mCourseCode = mCourseCodeEditText.getText().toString().trim();
                String courseCode = mCourseCode.toUpperCase();
                mCourseCodeEditText.setText(EMPTY);
                /*for credit unit*/
                mCreditUnit = Integer.parseInt(mCreditUnitEditText.getText().toString());
                int cu = mCreditUnit;
                mCreditUnitEditText.setText(EMPTY);
                //pass it to the single object of the Course class
                mSingleCourse = new Course(courseCode, cu);
                //set those variables into our course class
                mSingleCourse.setCourseCode(courseCode);
                mSingleCourse.setCreditUnit(cu);
                //finally add that single set course in our arraylist of courses
                mCoursesArrayList.add(mSingleCourse);
                //pass that list to our data manager
                KalkDataManager.getInstance().setCourseArrayList(mCoursesArrayList);
                //when done display a success SnackBar
                Snackbar.make(root, "Added: " + mSingleCourse.getCourseCode() + "{ " + mSingleCourse.getCreditUnit()
                        + " credit unit(s)}", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        mRemoveCourseButton.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    public void hideKeyBoard() {
        mInputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getView()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}