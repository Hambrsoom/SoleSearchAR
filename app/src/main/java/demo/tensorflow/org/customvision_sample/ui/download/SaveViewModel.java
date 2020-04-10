package demo.tensorflow.org.customvision_sample.ui.download;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SaveViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SaveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is save fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}