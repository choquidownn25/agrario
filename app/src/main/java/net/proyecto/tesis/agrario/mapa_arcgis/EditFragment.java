package net.proyecto.tesis.agrario.mapa_arcgis;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.proyecto.tesis.agrario.R;

/**
 * Created by choqu_000 on 17/09/2015.
 */
public class EditFragment extends DialogFragment {

    // To feed the values for teh break points
    EditText break1, break2, break3;
    // The Ok button
    Button button;
    // The action listener to click on dialog box
    protected OnDialogClickedListener callback = null;

    /**
     * The default constructor for the fragment to link with the activity
     */
    public EditFragment() {

    }

    /**
     * The interface for OnDialogClickListener
     */
    public interface OnDialogClickedListener {
        public abstract void onDialogClicked(double a1, double a2, double a3);
    }

    /**
     * To attach the fragment to the parent activity
     */
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        callback = (OnDialogClickedListener) activity;
    }

    /**
     * To set on click on the dialog
     * @param l
     */
    public void setOnDialogClickedListener(OnDialogClickedListener l) {
        callback = l;
    }

    /**
     * Initial creation of the fragment
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called once the fragment is created for it to create the UI.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editfragment, container);
        break1 = (EditText) view.findViewById(R.id.break1);
        break2 = (EditText) view.findViewById(R.id.break2);
        break3 = (EditText) view.findViewById(R.id.break3);
        button = (Button) view.findViewById(R.id.button);

        view.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return false;
            }

        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v.getId() == R.id.button) {
                    callback.onDialogClicked(
                            Double.parseDouble(break1.getText().toString()),
                            Double.parseDouble(break2.getText().toString()),
                            Double.parseDouble(break3.getText().toString()));
                } else {

                }
            }
        });
        getDialog().setTitle("Impedance (Set Breaks)");
        return view;
    }
}
