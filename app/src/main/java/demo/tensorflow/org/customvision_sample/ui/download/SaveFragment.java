package demo.tensorflow.org.customvision_sample.ui.download;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.soen357_2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SaveFragment extends Fragment {

    private SaveViewModel saveViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        saveViewModel =
                ViewModelProviders.of(this).get(SaveViewModel.class);
        View root = inflater.inflate(R.layout.fragment_save, container, false);
        final TextView textView = root.findViewById(R.id.text_save);
        saveViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}