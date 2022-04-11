package com.tnote.tnoteapp.ui.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.databinding.FragmentTtelementBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.models.TTElement
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager
import java.util.*

class TTElementFragment : Fragment(), TimePickerDialog.OnTimeSetListener {
    private var _binding: FragmentTtelementBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var sessionManager: SessionManager

    val args: TTElementFragmentArgs by navArgs()

    lateinit var spAdapter: ArrayAdapter<String>
    lateinit var ttElement: TTElement
    private var selectedElementId: Int = 0
    private var timetableId: Int = 0

    var timeSetterFlag = ""
    val startTimeFlag = "start_time"
    val endTimeFlag = "end_time"
    var hour: Int = 0
    var minute: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTtelementBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        sessionManager = SessionManager(requireContext())
        setupSpAdapter()

        selectedElementId = args.ttElementId
        timetableId = args.timetableId

        getData()

        binding.btnSelectStart.setOnClickListener {
            timeSetterFlag = startTimeFlag
            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        }

        binding.btnSelectEnd.setOnClickListener {
            timeSetterFlag = endTimeFlag
            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        }

        binding.fabSaveTTElement.setOnClickListener {
            saveTTElementChanges(ttElement)
            Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show()
        }


        viewModel.ttElementFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()
                    fillData(it.data!!)
                    ttElement = it.data
                    Log.e("onTTElementFragmentStateResponse", "Success Called", )
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        "Unexpected Error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> showProgressBar()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        getDefaultTime()

        this.hour = hour
        this.minute = minute

        if (timeSetterFlag == startTimeFlag) {
            ttElement.start = "$hour:$minute"

            binding.tvStart.text = "Start at $hour:$minute"
            binding.tvEnd.text = "End at $hour:${minute}"
        } else {
            ttElement.end = "$hour:$minute"

            if(minute < 10) {
                binding.tvEnd.text = "End at $hour:0$minute"
            } else {
                binding.tvEnd.text = "End at $hour:$minute"
            }
        }
    }

    private fun getData() {
        if (selectedElementId == 0) {
            val tempTTE = newTTElement()
            fillData(tempTTE)
            ttElement = tempTTE
            hideProgressBar()
        } else {
            viewModel.getSelectedTTElement(
                selectedElementId,
                sessionManager.getAuthToken()
            )
        }
    }

    private fun getDefaultTime() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun hideProgressBar() {
        binding.ttElementFragmentProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.ttElementFragmentProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpAdapter() {
        spAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spitem_day,
            resources.getStringArray(R.array.days_array))
    }

    private fun saveTTElementChanges(ttElement: TTElement) {
        ttElement.title = binding.etTTETitle.text.toString()
        ttElement.description = binding.etTTEDescription.text.toString()
        ttElement.day = binding.spDays.selectedItem.toString()
        ttElement.repeating = binding.cbRepeating.isChecked

        if (selectedElementId == 0) {
            viewModel.createTTElement(
                sessionManager.getAuthToken(),
                ttElement
            )
            Log.e("OnSaveTTEChanges", "Create called with $ttElement")
        } else {
            viewModel.updateTTElement(
                selectedElementId,
                sessionManager.getAuthToken(),
                ttElement
            )
            Log.e("OnSaveTTEChanges", "Update called with $ttElement")
        }
    }

    private fun fillData(it: TTElement) {
        it.apply {
            binding.spDays.setSelection(spAdapter.getPosition(day))
            binding.tvStart.text = start
            binding.tvEnd.text = end
            binding.etTTETitle.setText(title)
            binding.etTTEDescription.setText(description)
            binding.cbRepeating.isChecked = repeating
        }
    }

    private fun newTTElement(): TTElement {
        return TTElement(
            id = null,
            ttid = timetableId,
            day = "Monday",
            title = "",
            description = "",
            start = "00:00",
            end = "00:00",
            repeating = false,
            created_at = null,
            updated_at = null
        )
    }
}