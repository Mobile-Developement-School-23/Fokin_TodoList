package com.example.todoapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.di.FragmentScope
import com.example.todoapp.utils.generateRandomItemId
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@FragmentScope
class TodoListFragment : Fragment() {
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: TodoListViewModel
    @Inject
    lateinit var todoItemsAdapter: TodoItemAdapter
    @Inject
    lateinit var todoItemDecoration: TodoItemDecoration

    private var snackbar : Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App)
            .appComponent
            .todoListFragmentComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            setupUiEventsListener()
            setupRecycler()
            setupErrorHandler()
        }

        binding.swipeToRefresh.setOnRefreshListener {
            dismissSnackbar()
            viewModel.reloadData()
            binding.swipeToRefresh.isRefreshing = false
        }
        binding.floatingActionButton.setOnClickListener { onItemClick(generateRandomItemId(), true) }
        binding.imageSettings.setOnClickListener {showThemeSelectionBottomSheet()}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupErrorHandler() {
        viewModel.errorListLiveData().observe(viewLifecycleOwner) {
            if (it) {
                setupSnackbar(R.string.load_error)
            } else {
                dismissSnackbar()
            }
        }
        viewModel.errorItemLiveData().observe(viewLifecycleOwner) {
            if (it) {
                setupSnackbar(R.string.item_error)
            } else {
                dismissSnackbar()
            }
        }
    }

    private fun setupSnackbar(message: Int) {
        if (snackbar == null) {
            snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
            snackbar?.show()
        }
    }

    private fun setupUiEventsListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    when (it) {
                        is TodoListNavigations.NavigateToEditTodoItem -> {
                            onItemClick(it.id, false)
                        }
                        is TodoListNavigations.NavigateToNewTodoItem -> {
                            onItemClick(generateRandomItemId(), true)
                        }
                    }
                }
        }
    }

    private fun setupRecycler() {
        binding.todoItemsList.adapter = todoItemsAdapter
        binding.todoItemsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.todoItemsList.addItemDecoration(todoItemDecoration)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTodoItems()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    todoItemsAdapter.setData(it)
                }
        }
    }

    private fun dismissSnackbar() {
        snackbar?.dismiss()
        snackbar = null
    }

    private fun onItemClick(id: String, isNewItem: Boolean) {
        dismissSnackbar()
        val action =
            TodoListFragmentDirections.actionTodoListFragmentToAddTodoItemFragment2(id, isNewItem)
        findNavController().navigate(action)
    }

    private fun showThemeSelectionBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_theme)
        bottomSheetDialog.show()
    }
}