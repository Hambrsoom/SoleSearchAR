
package demo.tensorflow.org.sole_search;

import demo.tensorflow.org.sole_search.Classifier.Recognition;

import java.util.List;

public interface ResultsView {
  public void setResults(final List<Recognition> results);
}
