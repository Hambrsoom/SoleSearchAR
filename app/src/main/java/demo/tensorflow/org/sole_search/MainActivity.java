package demo.tensorflow.org.sole_search;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;


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

    private ImageButton screenShotBtn;


    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        iniComponent();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        demo.tensorflow.org.sole_search.BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(MainActivity.this, ActivityOne.class);
                        startActivity(intent1);
                        break;

                    case R.id.navigation_measure:
                        break;

                    case R.id.navigation_photo:
                        Intent intent2 = new Intent(MainActivity.this, ClassifierActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_email:
                        Intent intent4 = new Intent(MainActivity.this, ActivityFour.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        screenShotBtn = (ImageButton) findViewById(R.id.screenShot_btn);

        screenShotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = getScreenShot();
                Date date = new Date();
                CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
                store(bitmap,now+".png");
                Toast.makeText(getApplicationContext(),"A screenshot has been taken and stored at " +"/MyFiles/SoleSearch/" +now,Toast.LENGTH_SHORT).show();
            }
        });


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
    }


    public Bitmap getScreenShot() {
        ArSceneView view = arFragment.getArSceneView();
        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                Date date = new Date();
                CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
                store(bitmap, now + ".png");
            } else {
                Toast toast = Toast.makeText(MainActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
        return bitmap;
    }

    public void store(Bitmap bitmap, String fileName){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFiles/SoleSearch";
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dirPath+"/"+fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
