package com.example.lightswitch.ui.gallery

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lightswitch.R
import com.example.lightswitch.databinding.FragmentGalleryBinding
import org.json.JSONObject
import java.util.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    fun buildStrRequests(
        url: String?
    ): StringRequest? {
        Log.d("STATE1", "In buildStrRequests");
        val jsonBody: JSONObject = JSONObject();
        jsonBody.put("admin", "5f24a5ecc7522504fbebca19");
        return StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.d("STATE1", response)
            }, { error ->
                Log.d("STATE1", "Response Error Caught: ${error}");

            }
        )
    }
    fun buildJSONRequests(
        url: String?,
        jsonBody: JSONObject
    ): JsonObjectRequest? {
        Log.d("STATE1", "In buildStrRequests");
        return JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response -> println(response) },
            { error -> error.printStackTrace() })
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textGallery
//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        val textDate: TextView = root.findViewById(R.id.text_date)
        val textTime: TextView = root.findViewById(R.id.text_time)
        val gpioSpinner: Spinner = root.findViewById(R.id.gpios_spinner)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val pickDateBtn: Button = root.findViewById(R.id.pickDateBtn);
        val pickTimeBtn: Button = root.findViewById(R.id.pickTimeBtn);
        val submitBtn: Button = root.findViewById(R.id.submitBtn);
        val queue = Volley.newRequestQueue(context);
        val globalSec: Number = -1;
        var globalMin: Number = -1;
        var globalHour: Number = -1;

        var globalYear: Number = -1;
        var globalMonth: Number = -1;
        var globalDayOfMonth: Number = -1;

        Log.d("STATE1", "DONE building request")
        pickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                textDate.setText("" + dayOfMonth + " " + month + ", " + year)
                globalDayOfMonth = dayOfMonth;
                globalMonth = month;
                globalYear = year;
                Log.d("STATE1", textDate.getText().toString());
            }, year, month, day)
            dpd.show()
        }
        pickTimeBtn.setOnClickListener{
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val second = c.get(Calendar.SECOND)
            val dtd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener{ view, hour, minute ->
                textTime.setText("" + hour + ":" + minute);
                globalHour = hour;
                globalMin = minute;
                Log.d("STATE1", textTime.getText().toString());
            }, hour, minute, true)
            dtd.show();
        }
        submitBtn.setOnClickListener{

            Log.d("STATE1", "Global Hour = ${globalHour.toString()}")
            Log.d("STATE1", "Global Minute = ${globalMin}")

            Log.d("STATE1", "Global Year = ${globalYear}")
            Log.d("STATE1", "Global Month = ${globalMonth}")
            Log.d("STATE1", "Global Day of Month = ${globalDayOfMonth}")
            val postURL: (String) = "http://192.168.254.201:5000/schedule";
            val jsonBody: JSONObject = JSONObject();
            jsonBody.put("admin_id", "5f24a5ecc7522504fbebca19");
            val postStringRequest: (JsonObjectRequest?) = buildJSONRequests(postURL, jsonBody);
            queue.add(postStringRequest);
        }
//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.gpios_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            gpioSpinner.adapter = adapter
//        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}