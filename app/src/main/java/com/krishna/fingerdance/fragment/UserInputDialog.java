package com.krishna.fingerdance.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.krishna.fingerdance.R;

/**
 * Created by krishna on 06/02/17.
 */

public class UserInputDialog extends DialogFragment implements View.OnClickListener {
    private EditText input;
    private Continue onContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_user_input, container, false);
        input = (EditText) layout.findViewById(R.id.input);
        layout.findViewById(R.id.btn_continue).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onContinue = (Continue) context;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_continue) {
            if (input.getText() != null && !input.getText().toString().isEmpty()) {
                try {
                    int n = Integer.parseInt(input.getText().toString());
                    onContinue.onContinue(n);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Enter valid number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Enter a number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface Continue {
        void onContinue(int n);
    }
}
