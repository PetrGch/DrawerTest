package com.example.petr.drawertest;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import static android.support.v4.media.MediaDescriptionCompatApi21.Builder.setTitle;

public class drawer extends AppCompatActivity {

    private String[] mLinkTitles;
    private ListView mDrawerListView;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = mDrawerTitle = getTitle();
        mLinkTitles = getResources().getStringArray(R.array.link_numbers_ru);

        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // подключим адаптер для списка
        mDrawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mLinkTitles));
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        /*Icon in actionBar */

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                null,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d("myLog", "Выбран пункт " + position + "");
       /* Toast.makeText(drawer.this, "Выбран пункт " + position, Toast.LENGTH_SHORT).show();*/

            selectItem(position);
        }

    }

    private void selectItem(int position) {
        // Обновляем содержимое экрана, заменяя фрагменты
        newFragment fragment = new newFragment();
        Bundle args = new Bundle();
        args.putInt(newFragment.LINK_NUMBERS, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // обновим выбранный элемент списка и закрываем панель
        mDrawerListView.setItemChecked(position, true);
        setTitle(mLinkTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    @Override
    public void setTitle (CharSequence title) {
        mTitle = title;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTitle);
        } else {
            setTitle(mTitle);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        boolean drawerOpen =mDrawerLayout.isDrawerOpen(mDrawerListView);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle actio n bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this cat
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public class newFragment extends android.support.v4.app.Fragment{
        public static final String LINK_NUMBERS = "link_numbers";

        public newFragment() {
            // Для фрагмента требуется пустой конструктор
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.new_content, container, false);
            int i = getArguments().getInt(LINK_NUMBERS);
            // нахождения имен файлов
            String catName = getResources().getStringArray(R.array.link_numbers)[i];

            String catNameTitle = getResources().getStringArray(R.array.link_numbers_ru)[i];

            int imageId = getResources().getIdentifier(catName.toLowerCase(Locale.ROOT),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.imageViewCat)).setImageResource(imageId);
            getActivity().setTitle(catNameTitle);
            return rootView;
        }
    }

}