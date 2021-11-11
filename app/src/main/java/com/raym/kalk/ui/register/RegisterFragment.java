package com.raym.kalk.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.raym.kalk.R;
import com.raym.kalk.databinding.FragmentRegisterCourseBinding;
import com.raym.kalk.models.Course;
import com.raym.kalk.models.KalkDataManager;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    private final ArrayList<Course> mCourseArrayList = new ArrayList<>();
    private EditText mCourseCodeEditText;
    private EditText mCreditUnitEditText;
    private Button mCalculateGpButton;
    private Button mAddCourseButton;
    private Course mSingleCourse;
    private String mCourseCode;
    private int mCreditUnit;
    private final ArrayList<Course> mCoursesArrayList = new ArrayList<>();
    static String EMPTY = "";



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register_course, container, false);

        mCourseCodeEditText = root.findViewById(R.id.editTxt_course_code);
        mCreditUnitEditText = root.findViewById(R.id.editTxt_credit_unit);
        mCalculateGpButton = root.findViewById(R.id.calculate_gp);
        mAddCourseButton = root.findViewById(R.id.button_add_course);

        mCalculateGpButton.setOnClickListener(view -> {
//            Intent courseActivityIntent = new Intent(CourseActivity.this, CalculationActivity.class);
//            startActivity(courseActivityIntent);
            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        });

        mAddCourseButton.setOnClickListener(view -> {
            //get the course code and credit unit from the user and convert them to a string and
            //and int respectively assign them to their respective variables and clear their fields
            /*for course code*/
            mCourseCode = mCourseCodeEditText.getText().toString();
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

            //when done display a success toast
            Toast.makeText(getContext(), "ADDED", Toast.LENGTH_LONG).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}