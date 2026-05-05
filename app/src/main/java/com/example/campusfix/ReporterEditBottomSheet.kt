package com.example.campusfix

import android.os.Bundle
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReporterEditBottomSheet(
    private val request: RepairRequest
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_reporter_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val textviewIssueTitle = view.findViewById<TextView>(R.id.textviewIssueTitle)
        val textviewTimestamp = view.findViewById<TextView>(R.id.textviewTimestamp)
        val imageviewRequestPhoto = view.findViewById<ImageView>(R.id.imageviewRequestPhoto)
        val edittextLocation = view.findViewById<EditText>(R.id.edittextLocation)
        val edittextDescription = view.findViewById<EditText>(R.id.edittextDescription)
        val buttonSaveChanges = view.findViewById<Button>(R.id.buttonSaveChanges)

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

        edittextLocation.setText(request.location)
        edittextDescription.setText(request.description)

        if (request.status != "Pending") {
            edittextLocation.isEnabled = false
            edittextDescription.isEnabled = false
            buttonSaveChanges.visibility = View.GONE
            Toast.makeText(context, "Cannot edit requests that are already processing.", Toast.LENGTH_SHORT).show()
        }

        buttonSaveChanges.setOnClickListener {
            val updatedLocation = edittextLocation.text.toString().trim()
            val updatedDesc = edittextDescription.text.toString().trim()
            
            if (updatedLocation.isNotEmpty() && updatedDesc.isNotEmpty()) {
                DataManager.updateRequestDetails(request.id, updatedLocation, updatedDesc)
                dismiss()
            }
        }
    }
}
