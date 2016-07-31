package app.example.veuge.com.saludnfc.views;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutionException;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.PatientsAdapter;
import app.example.veuge.com.saludnfc.models.Patient;
import app.example.veuge.com.saludnfc.models.User;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class PatientsList extends AppCompatActivity {

    private String url;
    private User user;
    private DrawerLayout drawerLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PatientsAdapter adapter;
    private List<Patient> patients;
    private FloatingActionButton fab;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patients_list);

        String url = ((Variables) this.getApplication()).getUrl();
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("USER");
        token = ((Variables) this.getApplication()).getToken();
        setupUI();

        getPatientsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private List<Patient> getPatientsList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente";
        String resp;
        List<Patient> patients = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();

            JSONArray patientsArray = hmt.getJsonFromString(resp);
            patients = hmt.buildPatientObject(patientsArray);
            //patients = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return patients;
    }

    public void setupUI(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        Menu menu = navigationView.getMenu();
        MenuItem userName = menu.findItem(R.id.user_name);
        userName.setTitle(user.name);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        GetAsyncTask gat = new GetAsyncTask("http://192.168.1.159:8000/", "api/logout", token);
                        gat.execute();
                        Log.d("CERRAR SESION", "SESION");
                        try {
                            String resp = gat.get();
                            Log.d("RESPONSE", resp);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        finish();
                        System.exit(0);

                        // Closing drawer on item click
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.patient_recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new PatientsAdapter(mRecyclerView.getContext());
        patients = getPatientsList();
        adapter.setPatientsList(patients);
        mRecyclerView.setAdapter(adapter);
        fab = (FloatingActionButton) findViewById(R.id.add_patient);
    }

    public void createPatient(View view){
        Intent intent = new Intent(this, PatientCreate.class);
        intent.putExtra("USER", user);
        this.startActivity(intent);
    }
}
