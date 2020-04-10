package demo.tensorflow.org.customvision_sample.ui.email;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmailViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EmailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is email fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}