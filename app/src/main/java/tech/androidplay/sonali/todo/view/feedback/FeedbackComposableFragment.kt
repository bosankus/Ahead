package tech.androidplay.sonali.todo.view.feedback

import android.os.*
import android.view.*
import androidx.compose.ui.platform.*
import androidx.fragment.app.*
import kotlinx.coroutines.*
import tech.androidplay.sonali.todo.view.common.*
import tech.androidplay.sonali.todo.viewmodel.*

@ExperimentalCoroutinesApi
class FeedbackComposableFragment() : Fragment() {

    private val viewModel: FeedbackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AheadTheme {
                    FeedbackComposeScreen()
                }
            }
        }
    }
}

