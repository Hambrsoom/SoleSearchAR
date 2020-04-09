package demo.tensorflow.org.sole_search_ar;

import android.graphics.Bitmap;

public interface ICognitiveServicesClassifier {
    Classifier.Recognition classifyImage(Bitmap sourceImage, int orientation);
    void close();
}
