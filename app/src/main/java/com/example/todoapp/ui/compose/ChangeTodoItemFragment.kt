package com.example.todoapp.ui.compose

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

@FragmentScope
class ChangeTodoItemFragment : Fragment() {
    private val args by navArgs<ChangeTodoItemFragmentArgs>()
    @Inject
    lateinit var viewModel: ChangeTodoItemViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App)
            .appComponent
            .addTodoItemFragmentComponent()
            //.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.findTodoItem(args)
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                AppTheme {
                    viewModel.oldTodoItem?.let {
                        ChangeTodoItemScreen(
                            todoItem = it,
                            isEditing = !viewModel.isNewItem,
                            onAction = ::onTodoEditorAction
                        )
                    }
                }
            }
        }

        return view
    }

    private fun onTodoEditorAction(action: ChangeTodoItemActions) {
        when (action) {
            ChangeTodoItemActions.Close -> {
                findNavController().navigateUp()
            }

            ChangeTodoItemActions.Delete -> {
                viewModel.removeTodoItem()
                //findNavController().navigate(R.id.action_todoEditor_to_todoList)
            }

            ChangeTodoItemActions.Save -> {
                viewModel.saveTodoItem()
                //findNavController().navigate(R.id.action_todoEditor_to_todoList)
            }

            is ChangeTodoItemActions.UpdateDeadline -> {
                action.deadline?.let { viewModel.updateDeadline(it) }
            }

            is ChangeTodoItemActions.UpdateImportance -> {
                viewModel.updateImportance(action.importance)
            }

            is ChangeTodoItemActions.UpdateText -> {
                viewModel.updateText(action.text)
            }
        }
    }
}