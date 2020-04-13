package demo.tensorflow.org.customvision_sample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by User on 4/15/2017.
 */

public class ActivityFour extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        TextView title = (TextView) findViewById(R.id.activityTitle4);
        title.setText("This is Activityfour");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent0 = new Intent(ActivityFour.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.navigation_record:
                        Intent intent1 = new Intent(ActivityFour.this, ActivityOne.class);
                        startActivity(intent1);
                        break;

                    case R.id.navigation_photo:
                        Intent intent2 = new Intent(ActivityFour.this, ActivityTwo.class);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_save:
                        Intent intent3 = new Intent(ActivityFour.this, ActivityThree.class);
                        startActivity(intent3);
                        break;

                    case R.id.navigation_email:
                        break;
                }


                return false;
            }
        });
    }
}
