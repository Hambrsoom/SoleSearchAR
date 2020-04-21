package demo.tensorflow.org.sole_search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class ActivityFour extends AppCompatActivity {
    private EditText editTO, editSubject, editMessage;
    private Button btnSend, btnChoosePicture;
    private BottomNavigationView bottomNavigationView;
    private ImageView chooseImage;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        initComponents();
        initNavBar();
    }

    private void initComponents(){
        editTO = findViewById(R.id.text_to);
        editSubject = findViewById(R.id.text_subject);
        editMessage = findViewById(R.id.text_message);
        btnSend = findViewById(R.id.btn_send);
        chooseImage = findViewById(R.id.image);
        btnChoosePicture = findViewById(R.id.pick_Image_btn);
        setupChoosePictureBtn();
        setupSendBtn();
    }

    private void setupChoosePictureBtn(){
        btnChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent,"Pick an image"),1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            try {
                imageUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                chooseImage.setImageBitmap(bitmap);
            }catch (FileNotFoundException e ){
                e.printStackTrace();
            }
        }
    }

    private void setupSendBtn(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:" + editTO.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, editSubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT,editMessage.getText().toString());
                intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///mnt/sdcard/Pictures/1.jpg"));
                startActivity(intent);
            }
        });
    }

    private void initNavBar(){
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent0 = new Intent(ActivityFour.this, ActivityOne.class);
                        startActivity(intent0);
                        break;

                    case R.id.navigation_measure:
                        Intent intent1 = new Intent(ActivityFour.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.navigation_photo:
                        Intent intent2 = new Intent(ActivityFour.this, ClassifierActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_email:
                        break;
                }
                return false;
            }
        });
    }
}
