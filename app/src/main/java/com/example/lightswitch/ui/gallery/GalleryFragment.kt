package com.example.lightswitch.ui.gallery

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lightswitch.R
import android.widget.Button
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
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val mPickDateBtn: Button = root.findViewById(R.id.pickDateBtn);
        val mPickTimeBtn: Button = root.findViewById(R.id.pickTimeBtn);

        mPickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                textDate.setText("" + dayOfMonth + " " + month + ", " + year)
                Log.d("STATE1", textDate.getText().toString());
            }, year, month, day)
            dpd.show()
        }
        mPickTimeBtn.setOnClickListener{
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val second = c.get(Calendar.SECOND)
            val dtd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener{ view, hour, minute ->
                textTime.setText("" + hour + ":" + minute);
                Log.d("STATE1", textTime.getText().toString());
            }, hour, minute, true)
            dtd.show();
        }

        return root
    }
}