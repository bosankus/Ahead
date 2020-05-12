package tech.androidplay.sonali.todo.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frame_today_todo_header.*
import kotlinx.android.synthetic.main.shimmer_layout.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.Helper
import tech.androidplay.sonali.todo.utils.TimeStampUtil

class MainActivity : AppCompatActivity() {

    // Current timestamp
    private val currentDate: String by lazy { TimeStampUtil().currentLocalDate }
//    private val currentTime: String by lazy { TimeStampUtil().currentTime }

    // Firebase Auth
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firebaseUser: String = firebaseAuth.currentUser?.uid.toString()

    // Firebase Firestore
    private var firestoreDb = FirebaseFirestore.getInstance()
    private var taskListRef: CollectionReference = firestoreDb.collection("Tasks")

    private lateinit var animation: Animation
    private var todo: Todo? = Todo()
    private val taskList: MutableList<Todo> = mutableListOf()
    private val todoListAdapter: TodoListAdapter = TodoListAdapter(taskList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // enable white status bar with black icons
        setScreenUI()

        // load create task button animations
        initiateFABAnimation()

        // turning listeners on
        clickListeners()

        // loading all task list
        initializeRecyclerView()

    }

    override fun onStart() {
        super.onStart()
        shimmerFrameLayout.startShimmer()
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        shimmerFrameLayout.stopShimmer()
    }

    private fun setScreenUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
    }

    private fun initiateFABAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
        efabAddTask.startAnimation(animation)
        tvTodayDate.text = currentDate
    }

    @SuppressLint("InflateParams")
    private fun clickListeners() {
        efabAddTask.setOnClickListener {
            val arguments = Bundle()
            arguments.putString("userId", firebaseAuth.currentUser?.uid.toString())
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.arguments = arguments
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun initializeRecyclerView() {
        rvTodoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        rvTodoList.setHasFixedSize(true)
        initTodoListFirestore()
    }

    // DATA PART
    // TODO: To be shifted in correct way to TaskRepo
    @SuppressLint("SetTextI18n")
    private fun initTodoListFirestore() {
        taskListRef
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    // Continue shimmering
                    clParentLayout.visibility = View.GONE
                    clParentErrorLayout.visibility = View.VISIBLE
                }
                if (querySnapshot != null) {
                    taskList.clear()
                    // fetches the all documents on any change
                    val snapshotList = querySnapshot.documents
                    for (documentSnapshot in snapshotList) {
                        todo = documentSnapshot.toObject(Todo::class.java)
                        todo?.isCompleted = true
                        todo?.let { taskList.add(it) }
                    }
                    // Shimmer and show list
                    shimmerFrameLayout.visibility = View.GONE
                    rvTodoList.visibility = View.VISIBLE
                    rvTodoList.adapter = todoListAdapter
                    // Setting number of list items
                    tvTodayCount.text = todoListAdapter.itemCount.toString() + " items"
                } else Helper().logErrorMessage("Firestore: No value")
            }
    }

}



