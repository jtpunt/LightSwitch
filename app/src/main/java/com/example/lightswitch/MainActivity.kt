package com.example.lightswitch

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoIterable
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val jsonfile: String = applicationContext.assets.open("credentials.json").bufferedReader().use {it.readText()}
        val jsonObject = JSONObject(jsonfile)
        Log.d("STATE1", "${jsonObject["host"]}");
        val host = jsonObject["host"]
        val port = jsonObject["port"]
        val db = jsonObject["db"]
        val username = jsonObject["username"]
        val password = jsonObject["password"]
        val queryString = jsonObject["queryString"]
        val connectionString = "mongodb://$username:$password@$host/$db?$queryString";
        val uri: (MongoClientURI) =  MongoClientURI(connectionString);
        val mongoClient: (MongoClient) = MongoClient(uri);
        //val myDb: (MongoDatabase) = mongoClient.getDawwwwwwwwwwwwwtabase(uri.database);

        val myDbs: MongoIterable<String> = mongoClient.listDatabaseNames();
        val dhtSensorsDb = mongoClient.getDatabase("dht-sensors");
        val dhtDBCollection: MongoIterable<String> = dhtSensorsDb.listCollectionNames();
        Log.d("STATE1" , dhtSensorsDb.name)
        Log.d("STATE1", dhtDBCollection.toString())
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}