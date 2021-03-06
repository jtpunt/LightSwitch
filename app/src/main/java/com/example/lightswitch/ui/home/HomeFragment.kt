package com.example.lightswitch.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lightswitch.MyApplication
import com.example.lightswitch.R


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    fun buildStrRequests(
        url: String?,
        toggleBtn: ToggleButton,
        justOpened: BooleanArray,
        idx: Int
    ): StringRequest? {
        Log.d("STATE1", "In buildStrRequests");
        return StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                // Display the first 500 characters of the response string.
                Log.d("STATE1", response)
                if (response == "0") { // the relay switch is off
                    Log.d("STATE1", "Relay is OFF")
                    toggleBtn.isChecked = false
                } else if (response == "1") {
                    Log.d("STATE1", "Relay is ON")
                    justOpened[idx] = true
                    toggleBtn.isChecked = true
                }
            }, Response.ErrorListener { error ->
                Log.d("STATE1", "Response Error Caught: ${error}");
            
        })
    }

    fun setOnCheckedListener(
        url: String?,
        toggleBtn: ToggleButton,
        justOpened: BooleanArray,
        idx: Int,
        queue: RequestQueue
    ) {
        toggleBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("STATE1", "In setOnCheckListener");
            if (buttonView.isPressed) {
                if (justOpened[idx]) {
                    Log.d("STATE1", "button is pressed");
                    justOpened[idx] = false
                } else {
                    Log.d("STATE1", "button is not pressed");
                    queue.add(buildStrRequests(url, toggleBtn, justOpened, idx))


                }
            }
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val toggleBtn1StatusURL: (String) = "http://192.168.254.202:5000/status/2";
        val toggleBtn2StatusURL: (String) = "http://192.168.254.202:5000/status/3";
        val toggleBtn1ActivateURL: String = "http://192.168.254.202:5000/activate/2";
        val toggleBtn2ActivateURL: String = "http://192.168.254.202:5000/activate/3";

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {textView.text = it})

        Log.d("STATE1", "Before grabbing buttonIds")
        val toggleBtn1: (ToggleButton) = root.findViewById(R.id.toggleButton1);
        val toggleBtn2: (ToggleButton) = root.findViewById(R.id.toggleButton2);
        val justOpened: BooleanArray = BooleanArray(2);
        Log.d("STATE1", "Before building request")
        val queue = Volley.newRequestQueue(context);

        // Make an HTTP get request to get the status of our relay switch - is it on or off?
        //  - if the relay switch is activated - change our toggle button to be set to "on",
        //  - so that when the app is first opened, the toggle buttons are correctly set
        val toggleBtn1Status: (StringRequest?) = buildStrRequests(toggleBtn1StatusURL, toggleBtn1, justOpened, 0);
        val toggleBtn2Status: (StringRequest?) = buildStrRequests(toggleBtn2StatusURL, toggleBtn2, justOpened, 1);
        queue.add(toggleBtn1Status);
        queue.add(toggleBtn2Status);

        // Set an event listener for when our button is turned on or off to make an HTTP request to turn
        // the relay switch on or off
        setOnCheckedListener(toggleBtn1ActivateURL, toggleBtn1, justOpened, 0, queue);
        setOnCheckedListener(toggleBtn2ActivateURL, toggleBtn2, justOpened, 1, queue);
        return root
    }
}