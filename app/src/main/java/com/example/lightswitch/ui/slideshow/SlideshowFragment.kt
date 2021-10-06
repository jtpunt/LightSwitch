package com.example.lightswitch.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lightswitch.R
import com.example.lightswitch.databinding.FragmentSlideshowBinding
import org.json.JSONException
import org.json.JSONObject

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null
    fun buildStrRequests(
        url: String?,
        root: View
    ): StringRequest? {
        Log.d("STATE1", "In buildStrRequests");
        val jsonBody: JSONObject = JSONObject();
        jsonBody.put("admin", "5f24a5ecc7522504fbebca19");
        return StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                // Display the first 500 characters of the response string.
                val jsonResponse: JSONObject = JSONObject(response);
                val linear_layout: LinearLayout = root.findViewById(R.id.linear_layout)
                Log.d("STATE1", jsonResponse.toString() )
                val iter: Iterator<String> = jsonResponse.keys()
                while (iter.hasNext()) {
                    val schedule_id_key = iter.next()
                    try {
//                            val value: Any = jsonResponse.get(schedule_id_key)
//                            Log.d("STATE1", value.toString());
                        val jsonEntry: JSONObject = jsonResponse[schedule_id_key] as JSONObject
                        val textView = TextView(requireContext())
                        textView.text = jsonEntry.toString()
                        linear_layout.addView(textView)
                    } catch (e: JSONException) {
                        e.printStackTrace();
                        // Something went wrong!
                    }
                }
            }, Response.ErrorListener { error ->
                Log.d("STATE1", "Response Error Caught: ${error}");

            }
        )
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textSlideshow
//        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val queue = Volley.newRequestQueue(context);
        val getURL: (String) = "http://192.168.254.201:5000/schedule";
        val getStringRequest: (StringRequest?) = buildStrRequests(getURL, root);
        queue.add(getStringRequest);

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}