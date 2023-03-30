package com.cursosandroidant.snapshots.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cursosandroidant.snapshots.R
import com.cursosandroidant.snapshots.SnapshotsApplication
import com.cursosandroidant.snapshots.databinding.FragmentProfileBinding
import com.cursosandroidant.snapshots.utils.FragmentAux
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/****
 * Project: Snapshots
 * From: com.cursosandroidant.snapshots.ui.fragments
 * Created by Alain Nicolás Tello on 01/02/23 at 11:12
 * All rights reserved 2023.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 ***/
class ProfileFragment : Fragment(), FragmentAux {

    private lateinit var mBinding: FragmentProfileBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh()
        setupButton()
    }

    private fun setupButton() {
        mBinding.btnLogout.setOnClickListener {
            context?.let {
                MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.dialog_logout_title)
                        .setPositiveButton(R.string.dialog_logout_confirm) { _, _ ->
                            singOut()
                        }

                        .setNegativeButton(R.string.dialog_logout_cancel, null)
                        .show()
            }
        }
    }

    private fun singOut() {
        context?.let {
            AuthUI.getInstance().signOut(it)
                    .addOnCompleteListener {
                        Toast.makeText(context, R.string.profile_logout_success, Toast.LENGTH_SHORT).show()
                        mBinding.tvName.text = ""
                        mBinding.tvEmail.text = ""

                        (activity?.findViewById(R.id.bottomNav) as? BottomNavigationView)?.selectedItemId =
                            R.id.action_home
                    }
        }
    }

    /*
    *   FragmentAux
    * */
    override fun refresh() {
        with(mBinding) {
            tvName.text = SnapshotsApplication.currentUser.displayName
            tvEmail.text = SnapshotsApplication.currentUser.email
        }
    }
}