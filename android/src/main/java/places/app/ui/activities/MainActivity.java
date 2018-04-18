package places.app.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import places.app.R;
import places.app.ui.fragments.FragmentMap;
import places.app.ui.fragments.PlaceEditFragment;

public class MainActivity extends AppCompatActivity implements PlaceEditFragment.OnPlacesChangedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            openMap();
        } else {
            showBackArrowIfNeed();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                showBackArrowIfNeed();
            }
        });
    }


    public void addToBackStack(Fragment fragment, boolean toBackStack) {
        addToBackStack(fragment, toBackStack, null);
    }

    public void addToBackStack(Fragment fragment, boolean toBackStack, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (toBackStack) {
            ft.addToBackStack(null);
        } else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.add(R.id.content, fragment, tag);
        ft.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            finish();
        }
    }

    private void showBackArrowIfNeed() {
        FragmentManager fm = getSupportFragmentManager();
        if (getSupportActionBar() == null) return;
        if (fm.getBackStackEntryCount() > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void openMap() {
        addToBackStack(FragmentMap.newInstance(), false, "map");
    }


    @Override
    public void onPlacesChanged() {
        FragmentMap frag = (FragmentMap) getSupportFragmentManager().findFragmentByTag("map");
        frag.loadPlaces();
    }
}
