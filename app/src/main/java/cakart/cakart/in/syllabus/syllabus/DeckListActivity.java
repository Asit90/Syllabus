package cakart.cakart.in.syllabus.syllabus;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import cakart.cakart.in.syllabus.R;

public class DeckListActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    public int stack_count = 0;
    Fragment sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        NavigationView nvDrawer =findViewById(R.id.nv);
        mDrawerlayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);
        if (getIntent().getExtras().getString("show_type").equals("studymat"))
        {

            sf=new ICAIstudyMaterialFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent,sf).commit();
        }
    }
    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerlayout.closeDrawers();
                selectedItemDrawer(item);
                return true;
            }
        });

    }
    public void selectedItemDrawer(MenuItem menuItem) {
        android.support.v4.app.Fragment myFragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId()){
            case R.id.study_material:
                fragmentClass = ICAIstudyMaterialFragment.class;
                break;
                default:
                fragmentClass = ICAIstudyMaterialFragment.class;
        }
        try {
            myFragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            if(myFragment instanceof  ICAIstudyMaterialFragment){
                sf = (ICAIstudyMaterialFragment) myFragment;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
    }
    @Override
    public void onBackPressed() {
        if(sf instanceof ICAIstudyMaterialFragment) {
            ICAIstudyMaterialFragment k = (ICAIstudyMaterialFragment) sf;
            if (sf != null && stack_count > 0) {
                stack_count = stack_count - 1;
                k.goback();
            } else if (sf != null && !k.is_showing) {
                sf = new ICAIstudyMaterialFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
            } else {
                finish();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        showList();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
