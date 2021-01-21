package com.example.lightswitch.ui.gallery

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lightswitch.R
import android.widget.Button
import android.widget.Spinner
import java.util.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)
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

        val globalSec: Number = -1;
        var globalMin: Number = -1;
        var globalHour: Number = -1;

        var globalYear: Number = -1;
        var globalMonth: Number = -1;
        var globalDayOfMonth: Number = -1;
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
        }
        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.gpios_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            gpioSpinner.adapter = adapter
        }

        return root
    }
}