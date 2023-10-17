package com.example.crudsops.ui.mainApp.dashboard

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudsops.R
import com.example.crudsops.databinding.ActivityDashBoardBinding
import com.example.crudsops.ui.auth.LoginActivity
import com.example.crudsops.data.Item
import com.example.crudsops.data.viewModels.ItemViewModel
import com.example.crudsops.data.viewModels.ItemViewModelFactory
import com.example.crudsops.ui.mainApp.viewItem.ViewItemActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashBoardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ItemRecyclerAdapter.CallBackListener {

    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing firebase
        auth = Firebase.auth
        db = Firebase.firestore
        // setting up drawer layout
        setDrawerLayout()
        // initializing view model
        itemViewModel = ViewModelProvider(this, ItemViewModelFactory())[ItemViewModel::class.java]
        initRecyclerView()

        // initializing on clicks
        binding.fabAddNewItem.setOnClickListener { onClickAddNewItem() }
    }

    private fun initRecyclerView() {
        recyclerView = binding.rvItems
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        observeData()
    }

    private fun observeData() {
        itemViewModel.lst.observe(this) {
            recyclerView.adapter = ItemRecyclerAdapter(it, this)
        }
    }

    // callback from item recycler adapter
    override fun onItemRemoveClicked(item: Item) {
        // dialog box to take confirmation from user
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete Item?")
        dialog.setMessage("Do you want to delete \"${item.title}\"?")
        dialog.setPositiveButton("Yes") { _, _ ->
            itemViewModel.remove(item)
        }
        dialog.setNegativeButton("Cancel") { _, _ ->
        }
        dialog.setCancelable(false)
        if (!isFinishing)
            dialog.show()
    }

    // callback from item recycler adapter
    override fun onItemClicked(item: Item) {
        startActivity(
            Intent(baseContext, ViewItemActivity::class.java)
                .putExtra("id", item.id)
                .putExtra("title", item.title)
                .putExtra("description", item.description)
        )
    }

    private fun onClickAddNewItem() {
        val createNewItemBottomSheet = CreateNewItemBottomSheet(0, itemViewModel)
        createNewItemBottomSheet.show(
            supportFragmentManager,
            createNewItemBottomSheet.javaClass.simpleName
        )
        createNewItemBottomSheet.isCancelable = false
    }

    private fun setDrawerLayout() {
        setSupportActionBar(binding.toolbar)

        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener(this)
        val navHeaderView: View = navigationView.getHeaderView(0)
        navHeaderView.findViewById<TextView>(R.id.tv_email).text = auth.currentUser!!.email
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                auth.signOut()
                startActivity(Intent(baseContext, LoginActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawers()
        return true
    }
}