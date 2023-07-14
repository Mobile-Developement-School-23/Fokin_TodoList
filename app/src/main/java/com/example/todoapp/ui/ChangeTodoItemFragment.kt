package com.example.todoapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.todoapp.App
import com.example.todoapp.databinding.FragmentAddTodoItemBinding
import com.example.todoapp.di.FragmentScope
import com.example.todoapp.ui.ChangeTodoItemFragmentArgs
import com.example.todoapp.ui.ChangeTodoItemViewModel
import javax.inject.Inject
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ui.compose.AppTheme
import com.example.todoapp.ui.compose.ChangeTodoItemActions
import com.example.todoapp.ui.compose.ChangeTodoItemScreen

@FragmentScope
class ChangeTodoItemFragment : Fragment() {
    private val args by navArgs<ChangeTodoItemFragmentArgs>()
    @Inject
    lateinit var viewModel: ChangeTodoItemViewModel

    private var _binding: FragmentAddTodoItemBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App)
            .appComponent
            .addTodoItemFragmentComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init(args)
        binding.composeChangeTodoItem.setContent {
            AppTheme {
                ChangeTodoItemScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    uiEvent = viewModel.uiEvent,
                    uiAction = viewModel::onUiAction,
                    onNavigateUp = { findNavController().navigateUp() },
                    onSave = { findNavController().navigateUp() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}