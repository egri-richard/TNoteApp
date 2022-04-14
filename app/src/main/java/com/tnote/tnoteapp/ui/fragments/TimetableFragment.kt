package com.tnote.tnoteapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.adapters.TTElementsAdapter
import com.tnote.tnoteapp.databinding.FragmentTimetableBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.models.TTElement
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class TimetableFragment: Fragment(R.layout.fragment_timetable) {
    private var _binding: FragmentTimetableBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var ttElementsAdapter: TTElementsAdapter

    lateinit var sessionManager: SessionManager

    val args: TimetableFragmentArgs by navArgs()
    private var timetableId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        setupRV()
        sessionManager = SessionManager(requireContext())

        timetableId = args.timetableId
        getData()


        ttElementsAdapter.setOnItemClickListener {
            Log.e("OnNavigateToTTEFrag", "id: ${it.id}", )
            val ttElementId = Bundle().apply {
                putInt("ttElementId", it.id!!)
                putInt("timetableId", timetableId)
            }

            Log.e("OnNavigateToTTEFrag_NavArgsBundle", "Value: $ttElementId", )
            findNavController().navigate(
                R.id.action_timetableFragment_to_TTElementFragment,
                ttElementId
            )
        }

        ttElementsAdapter.setOnItemLongClickListener {
            //Log.e("OnTTEDelBtnClick", "id: ${it.id!!}", )
            createDeleteDialog(it.id!!).show()
        }

        binding.fabNewTTElement.setOnClickListener {
            val ttElementId = Bundle().apply {
                putInt("ttElementId", 0)
                putInt("timetableId", timetableId)
            }
            findNavController().navigate(
                R.id.action_timetableFragment_to_TTElementFragment,
                ttElementId
            )
        }

        viewModel.timetableFragmentState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    Log.e("OnTimetableFragmentStateResponse", "response data: ${response.data}")

                    response.data?.let { list ->
                        ttElementsAdapter.differ.submitList(sortTTList(list))
                        Log.e("OnTimetableFragmentStateResponse", "RetList: $list")
                    }

                    binding.rvTTElements.smoothScrollToPosition(0)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        "Unexpected Error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun getData() {
        viewModel.getSelectedTimetable(
            timetableId,
            sessionManager.getAuthToken()
        )

        Log.e("TimetableFragment getData called", "TTID: $timetableId ", )
    }

    private fun setupRV() {
        ttElementsAdapter = TTElementsAdapter()
        binding.rvTTElements.apply {
            adapter = ttElementsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.fragmentTimetableProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.fragmentTimetableProgressBar.visibility = View.VISIBLE
    }

    private fun delete(id: Int) {
        viewModel.deleteTTElement(
            id,
            sessionManager.getAuthToken()
        )

        getData()
    }

    private fun createDeleteDialog(id: Int): AlertDialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage("Do you want to delete this event?")
            .setPositiveButton("Yes") { _, _ ->
                delete(id)
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { _, _ -> }
            .create()

        return dialog
    }

    private fun sortTTList(list: List<TTElement>): List<TTElement> {
        val mappedList = list.map {
            when(it.day) {
                "Monday" -> it.day = "1"
                "Tuesday" -> it.day = "2"
                "Wednesday" -> it.day = "3"
                "Thursday" -> it.day = "4"
                "Friday" -> it.day = "5"
                "Saturday" -> it.day = "6"
                "Sunday" -> it.day = "7"
            }
        }

        Log.e("OnTimetableListMapped", "list: $mappedList", )

        val sortedList = list.sortedWith(
            compareBy(
                { it.day },
                { it.start }
            )
        )

        val finalList = sortedList.map {
            when(it.day) {
                "1" -> it.day = "Monday"
                "2" -> it.day = "Tuesday"
                "3" -> it.day = "Wednesday"
                "4" -> it.day = "Thursday"
                "5" -> it.day = "Friday"
                "6" -> it.day = "Saturday"
                "7" -> it.day = "Sunday"
            }
            it
        }

        return finalList
    }
}