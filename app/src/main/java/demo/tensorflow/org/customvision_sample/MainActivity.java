package demo.tensorflow.org.customvision_sample;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import demo.tensorflow.org.customvision_sample.R;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private Button  sizeBtn;
    private Button  boundsBtn;
    private Button  analysisBtn;


    private static final String TAG = "MAIN ACTIVITY";
    private static final double MIN_OPENGL_VERSION = 3.0;
    private ModelRenderable andyRenderable;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00 m");

    private Anchor anchor1 = null, anchor2 = null;

    private HitResult myhit;

    private TextView descriptionView;

    List<AnchorNode> anchorNodes = new ArrayList<>();

    private boolean measure_height = false;
    private ArrayList<String> arl_saved = new ArrayList<String>();

    private float fl_measurement = 0.0f;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        iniComponent();
        setupBtns();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_record, R.id.navigation_photo, R.id.navigation_save, R.id.navigation_email)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        ModelRenderable.builder()
                .setSource(this, R.raw.andy)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }
                    myhit = hitResult;

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();

                    AnchorNode anchorNode = new AnchorNode(anchor);


                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    if(!measure_height) {
                        if(anchor2 != null){
                            emptyAnchors();
                        }
                        if (anchor1 == null) {
                            anchor1 = anchor;
                        } else {
                            anchor2 = anchor;
                            fl_measurement = getMetersBetweenAnchors(anchor1, anchor2);
                            descriptionView.setText("Width: " +
                                    form_numbers.format(fl_measurement));

                        }
                    }
                    else{
                        emptyAnchors();
                        anchor1 = anchor;
                        descriptionView.setText("Move the slider till the cube reaches the upper base");
                    }

                    myanchornode = anchorNode;

                    anchorNodes.add(anchorNode);

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                    andy.getScaleController().setEnabled(false);
                });
    }


    private void iniComponent(){
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);
        descriptionView = (TextView) findViewById(R.id.text);
        sizeBtn = (Button) findViewById(R.id.sizeBtn);
        boundsBtn = (Button) findViewById(R.id.boundsBtn);
        analysisBtn = (Button) findViewById(R.id.analysisBtn);
    }

    private void setupBtns(){
        setupBoundsBtn();
        setupSizeBtn();
        setupAnalysisBtn();
    }

    private void setupSizeBtn(){
        sizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetLayout();
                setupBtnsAndTextVisibilityWhenClicking();
                descriptionView.setText("Click the extremes you want to measure");
            }
        });
    }

    private void setupBoundsBtn(){
        boundsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetLayout();
                setupBtnsAndTextVisibilityWhenClicking();
                descriptionView.setText("I don't know what to say here");
            }
        });
    }

    private void setupAnalysisBtn(){
        sizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetLayout();
                setupBtnsAndTextVisibilityWhenClicking();
                descriptionView.setText("Something for Analysis!");
            }
        });
    }




    private void setupBtnsAndTextVisibilityWhenClicking(){
        descriptionView.setVisibility(View.VISIBLE);
        boundsBtn.setVisibility(View.GONE);
        sizeBtn.setVisibility(View.GONE);
        analysisBtn.setVisibility(View.GONE);
    }


    /**
     * Function to return the distance in meters between two objects placed in ArPlane
     * @param anchor1 first object's anchor
     * @param anchor2 second object's anchor
     * @return the distance between the two anchors in meters
     */
    private float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for (int i = 0; i < 3; ++i)
            totalDistanceSquared += distance_vector[i] * distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared);
    }


    /**
     * Check whether the device supports the tools required to use the measurement tools
     * @param activity
     * @return boolean determining whether the device is supported or not
     */
    private boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }


    /**
     * Set layout to its initial state
     */
    private void resetLayout(){
        measure_height = false;
        emptyAnchors();
    }

    private void emptyAnchors(){
        anchor1 = null;
        anchor2 = null;
        for (AnchorNode n : anchorNodes) {
            arFragment.getArSceneView().getScene().removeChild(n);
            n.getAnchor().detach();
            n.setParent(null);
            n = null;
        }
    }
}
