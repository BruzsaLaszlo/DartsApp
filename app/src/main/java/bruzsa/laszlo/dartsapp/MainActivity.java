package bruzsa.laszlo.dartsapp;

import static android.speech.tts.TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import bruzsa.laszlo.dartsapp.databinding.ActivityMainBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES = "MySharedPref";
    private AppBarConfiguration mAppBarConfiguration;
    private SharedViewModel sharedViewModel;
    private ActivityMainBinding binding;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    Log.d("MainActivity", "EXTRA_AVAILABLE_VOICES: " + result.getData()
                            .getCharSequenceArrayListExtra(EXTRA_AVAILABLE_VOICES));
                } else {
                    // missing data, install it
                    Intent installIntent = new Intent();
                    installIntent.setAction(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        mStartForResult.launch(checkIntent);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_dartsX01Fragment, R.id.nav_cricketFragment)
                .setOpenableLayout(drawer)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null : "navHostFragment not found";
        NavController navController = navHostFragment.getNavController();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Fetch the stored data in onResume()
    // Because this is what will be called
    // when the app opens again
    @Override
    protected void onResume() {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        String s1 = sh.getString("name", "");
        int a = sh.getInt("age", 0);

        // Setting the fetched data
        // in the EditTexts
    }

    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called
    // and data will be stored
    @Override
    protected void onPause() {
        super.onPause();

        // Creating a shared pref object
        // with a file name "MySharedPref"
        // in private mode
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
//        myEdit.putString("name", name.getText().toString());
//        myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
//        myEdit.apply();
    }
}