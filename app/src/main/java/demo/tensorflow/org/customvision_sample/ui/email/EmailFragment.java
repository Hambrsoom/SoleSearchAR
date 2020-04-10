package demo.tensorflow.org.customvision_sample.ui.email;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import demo.tensorflow.org.customvision_sample.R;

public class EmailFragment extends Fragment {

    private EmailViewModel emailViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        emailViewModel =
                ViewModelProviders.of(this).get(EmailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_save, container, false);
        final TextView textView = root.findViewById(R.id.text_save);
        emailViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}