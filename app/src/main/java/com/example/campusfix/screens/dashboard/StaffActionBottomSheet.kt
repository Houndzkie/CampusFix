package com.example.campusfix.screens.dashboard

import android.os.Bundle
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.campusfix.R
import com.example.campusfix.data.DataManager
import com.example.campusfix.data.RepairRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StaffActionBottomSheet(
    private val request: RepairRequest
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_staff_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textviewIssueTitle = view.findViewById<TextView>(R.id.textviewIssueTitle)
        val textviewTimestamp = view.findViewById<TextView>(R.id.textviewTimestamp)
        val imageviewRequestPhoto = view.findViewById<ImageView>(R.id.imageviewRequestPhoto)
        val textviewLocation = view.findViewById<TextView>(R.id.textviewLocation)
        val textviewDescription = view.findViewById<TextView>(R.id.textviewDescription)
        val buttonAccept = view.findViewById<Button>(R.id.buttonAccept)
        val buttonFinish = view.findViewById<Button>(R.id.buttonFinish)

        textviewIssueTitle.text = request.issue

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = sdf.format(Date(request.timestamp))
        textviewTimestamp.text = "Reported on: $formattedDate"

        if (request.photoUrl.isNotBlank()) {
            imageviewRequestPhoto.setImageURI(Uri.parse(request.photoUrl))
            imageviewRequestPhoto.visibility = View.VISIBLE
        } else {
            imageviewRequestPhoto.visibility = View.GONE
        }

        textviewLocation.text = request.location
        textviewDescription.text = request.description

        if (request.status == "Pending") {
            buttonAccept.visibility = View.VISIBLE
            buttonFinish.visibility = View.GONE
        } else if (request.status == "In Progress") {
            buttonAccept.visibility = View.GONE
            buttonFinish.visibility = View.VISIBLE
        } else {
            buttonAccept.visibility = View.GONE
            buttonFinish.visibility = View.GONE
        }

        buttonAccept.setOnClickListener {
            DataManager.updateRequestStatus(request.id, "In Progress")
            dismiss()
        }

        buttonFinish.setOnClickListener {
            DataManager.updateRequestStatus(request.id, "Completed")
            dismiss()
        }
    }
}
