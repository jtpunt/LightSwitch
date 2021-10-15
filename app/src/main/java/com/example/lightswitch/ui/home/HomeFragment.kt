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
import com.example.lightswitch.R
import com.example.lightswitch.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    fun buildStrRequests(
        url: String?,
        toggleBtn: ToggleButton,
    ): StringRequest? {
        Log.d("STATE1", "In buildStrRequests");
        return StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.d("STATE1", response)
                if (response == "0") { // the relay switch is off
                    Log.d("STATE1", "Relay is OFF")
                    toggleBtn.isChecked = false
                } else if (response == "1") {
                    Log.d("STATE1", "Relay is ON")
                    toggleBtn.isChecked = true
                }
            }, { error ->
                Log.d("STATE1", "Response Error Caught: ${error}");
            })
    }

    fun setOnCheckedListener(
        url: String?,
        toggleBtn: ToggleButton,
        queue: RequestQueue
    ) {
        toggleBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("STATE1", "In setOnCheckListener");
            if (buttonView.isPressed) {
                queue.add(buildStrRequests(url, toggleBtn))
            }
        }
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        /***********************************************************************************
         * Define our API urls to retrieve the status of our smart outlets - are the outlets
         * on or off?
         * ********************************************************************************/
        // Jonathan and Thaerin's Living Room Smart Outlets
        val toggleBtn1StatusURL = "http://192.168.1.202:5000/outlet/gpio/2/status";
        val toggleBtn2StatusURL = "http://192.168.1.202:5000/outlet/gpio/3/status";
        // Jonathan and Thaerin's Computer Room Smart Outlets
        val toggleBtn3StatusURL = "http://192.168.1.200:5000/outlet/gpio/2/status";
        val toggleBtn4StatusURL = "http://192.168.1.200:5000/outlet/gpio/3/status";
        // Stephen and Lacey's Living Room Smart Outlets
        val toggleBtn5StatusURL = "http://192.168.1.201:5000/outlet/gpio/2/status";
        val toggleBtn6StatusURL = "http://192.168.1.201:5000/outlet/gpio/3/status";
        /***********************************************************************************
         * Define our API urls to toggle our smarts outlets on or off
         * ********************************************************************************/
        // Jonathan and Thaerin's Living Room Smart Outlets
        val toggleBtn1ActivateURL = "http://192.168.1.202:5000/outlet/gpio/2/toggle";
        val toggleBtn2ActivateURL = "http://192.168.1.202:5000/outlet/gpio/3/toggle";
        // Jonathan and Thaerin's Computer Room Smart Outlets
        val toggleBtn3ActivateURL = "http://192.168.1.200:5000/outlet/gpio/2/toggle";
        val toggleBtn4ActivateURL = "http://192.168.1.200:5000/outlet/gpio/3/toggle";
        // Stephen and Lacey's Living Room Smart Outlets
        val toggleBtn5ActivateURL = "http://192.168.1.201:5000/outlet/gpio/2/toggle";
        val toggleBtn6ActivateURL = "http://192.168.1.201:5000/outlet/gpio/3/toggle";

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {textView.text = it})

        Log.d("STATE1", "Before grabbing buttonIds")
        // toggle buttons to turn Jonathan and Thaerin's Living Room Smart Outlets on or off
        val toggleBtn1: (ToggleButton) = root.findViewById(R.id.toggleButton1);
        val toggleBtn2: (ToggleButton) = root.findViewById(R.id.toggleButton2);

        // toggle buttons to turn Jonathan and Thaerin's Computer Room Smart Outlets on or off
        val toggleBtn3: (ToggleButton) = root.findViewById(R.id.toggleButton3);
        val toggleBtn4: (ToggleButton) = root.findViewById(R.id.toggleButton4);

        // toggle buttons to turn Stephen and Laceu's Living Room Smart Outlets on or off
        val toggleBtn5: (ToggleButton) = root.findViewById(R.id.toggleButton5);
        val toggleBtn6: (ToggleButton) = root.findViewById(R.id.toggleButton6);

        Log.d("STATE1", "Before building request")
        val queue = Volley.newRequestQueue(context);

        // Make an HTTP get request to get the status of our relay switch - is it on or off?
        //  - if the relay switch is activated - change our toggle button to be set to "on",
        //  - so that when the app is first opened, the toggle buttons are correctly set
        val toggleBtn1Status: (StringRequest?) = buildStrRequests(toggleBtn1StatusURL, toggleBtn1);
        val toggleBtn2Status: (StringRequest?) = buildStrRequests(toggleBtn2StatusURL, toggleBtn2);

        val toggleBtn3Status: (StringRequest?) = buildStrRequests(toggleBtn3StatusURL, toggleBtn3);
        val toggleBtn4Status: (StringRequest?) = buildStrRequests(toggleBtn4StatusURL, toggleBtn4);

        val toggleBtn5Status: (StringRequest?) = buildStrRequests(toggleBtn5StatusURL, toggleBtn5);
        val toggleBtn6Status: (StringRequest?) = buildStrRequests(toggleBtn6StatusURL, toggleBtn6);

        // Make the HTTP GET requests to all of our smart outlets
        queue.add(toggleBtn1Status);
        queue.add(toggleBtn2Status);
        queue.add(toggleBtn3Status);
        queue.add(toggleBtn4Status);
        queue.add(toggleBtn5Status);
        queue.add(toggleBtn6Status);

        // Set an event listener for when our button is turned on or off to make an HTTP request to turn
        // the relay switch on or off
        setOnCheckedListener(toggleBtn1ActivateURL, toggleBtn1, queue);
        setOnCheckedListener(toggleBtn2ActivateURL, toggleBtn2, queue);
        setOnCheckedListener(toggleBtn3ActivateURL, toggleBtn3, queue);
        setOnCheckedListener(toggleBtn4ActivateURL, toggleBtn4, queue);
        setOnCheckedListener(toggleBtn5ActivateURL, toggleBtn5, queue);
        setOnCheckedListener(toggleBtn6ActivateURL, toggleBtn6, queue);

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}