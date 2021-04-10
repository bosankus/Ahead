package tech.androidplay.sonali.todo.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tech.androidplay.sonali.todo.R

class PrioritySelectionDialog : BottomSheetDialogFragment() {

    private val args: PrioritySelectionDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.layout_priority_selection_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (args.priorityId > 0)
//            Then a selection has been made with any of the priority
    }

    companion object {
        const val TOP_PRIORITY = 1
        const val MEDIUM_PRIORITY = 2
        const val LOW_PRIORITY = 3
    }
}