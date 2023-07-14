package com.example.todoapp.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.NotificationReceiver
import com.example.todoapp.ui.compose.AppTheme
import com.example.todoapp.ui.compose.ChangeTodoItemActions
import com.example.todoapp.ui.compose.ChangeTodoItemScreen
import com.example.todoapp.utils.INTENT_ID_IMPORTANCE_KEY
import com.example.todoapp.utils.INTENT_ID_KEY
import com.example.todoapp.utils.INTENT_ID_TITLE_KEY
import com.example.todoapp.utils.MS_IN_S
import com.example.todoapp.utils.dateToUnix
import kotlinx.coroutines.launch

@FragmentScope
class ChangeTodoItemFragment : Fragment() {
    private val args by navArgs<ChangeTodoItemFragmentArgs>()
    @Inject
    lateinit var viewModel: ChangeTodoItemViewModel
    @Inject
    lateinit var alarmManager: AlarmManager

    private var _binding: FragmentAddTodoItemBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App)
            .appComponent
            .changeTodoItemFragmentComponentFactory()
            .create(requireContext())
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
        lifecycleScope.launch {
            var idToInt = 0
            if (args.id.length  >= 9) {
                idToInt = args.id.substring(8, 10).toInt()
            }
            val alarmIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
                intent
                    .putExtra(INTENT_ID_KEY, idToInt)
                    .putExtra(INTENT_ID_TITLE_KEY, viewModel.uiState.value.text)
                    .putExtra(INTENT_ID_IMPORTANCE_KEY, viewModel.uiState.value.importance.toStringResource())
                PendingIntent.getBroadcast(context, idToInt, intent, PendingIntent.FLAG_IMMUTABLE)
            }
            if (viewModel.uiState.value.isDeadlineSet) {
                val time = dateToUnix(viewModel.uiState.value.deadline) * MS_IN_S
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    alarmIntent
                )
            }
        }

        super.onDestroyView()
        _binding = null
    }


}