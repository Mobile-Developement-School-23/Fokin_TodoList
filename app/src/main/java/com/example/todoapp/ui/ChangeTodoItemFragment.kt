package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddTodoItemBinding
import com.example.todoapp.Importance
import com.example.todoapp.utils.dateToUnix
import com.example.todoapp.utils.formatDate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class ChangeTodoItemFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentAddTodoItemBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ChangeTodoItemFragmentArgs>()
    private val viewModel: ChangeTodoItemViewModel by viewModels()

    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.findTodoItem(args)

        setUiEventsListener()
        showPopUpMenu()
        setupDatePickerAndSwitch()
        setDataCollectors()

        binding.textOfTodoItem.addTextChangedListener { text -> saveButtonState(text) }
        saveButtonState(binding.textOfTodoItem.text)

        binding.closeButton.setOnClickListener { backToTodoList() }
        binding.saveButton.setOnClickListener {
            viewModel.updateText(binding.textOfTodoItem.text.toString())
            viewModel.saveTodoItem()
        }
        binding.deleteButton.setOnClickListener { viewModel.removeTodoItem() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUiEventsListener() {
        lifecycleScope.launch {
            viewModel.uiEvent.collectLatest {
                when (it) {
                    ChangeTodoItemNavigations.NavigateUp -> backToTodoList()
                }
            }
        }
    }

    private fun setDataCollectors() {
        lifecycleScope.launch {
            viewModel.text.collect {
                binding.textOfTodoItem.setText(it)
            }
        }
        lifecycleScope.launch {
            viewModel.importance.collect {
                binding.importanceValue.text = it.getLocalizedName(requireContext())
            }
        }
        lifecycleScope.launch {
            viewModel.deadline.collect {
                binding.deadlineDate.text = formatDate(it)
            }
        }
        lifecycleScope.launch {
            viewModel.isDeadlineSet.collect {
                binding.switchDeadline.isChecked = it
                binding.deadlineDate.text = if (it) formatDate(viewModel.deadline.value) else ""
            }
        }
    }

    private fun showPopUpMenu() {
        binding.importance.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.inflate(R.menu.importance_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val importance = when (menuItem.itemId) {
                    R.id.menu_item_high -> Importance.IMPORTANT
                    R.id.menu_item_medium -> Importance.BASIC
                    R.id.menu_item_low -> Importance.LOW
                    else -> viewModel.importance.value
                }
                viewModel.updateImportance(importance)
                true
            }
            popupMenu.show()
        }
    }

    private fun setupDatePickerAndSwitch() {
        calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnCancelListener { binding.switchDeadline.isChecked = false }

        binding.switchDeadline.setOnClickListener {
            if (binding.switchDeadline.isChecked) {
                datePickerDialog.show()
            } else {
                viewModel.updateIsDeadlineSet(false)
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        calendar.set(p1, p2, p3)
        viewModel.updateDeadline(dateToUnix(calendar.time))
        viewModel.updateIsDeadlineSet(true)
    }

    private fun saveButtonState(text: Editable?) {
        binding.saveButton.isEnabled = !text.isNullOrBlank()
    }

    private fun backToTodoList() {
        findNavController().navigateUp()
    }
}