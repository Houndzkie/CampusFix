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

    private var tempProofUri: Uri? = null
    private var capturedProofUri: Uri? = null
    private lateinit var imageviewRepairProof: ImageView
    private lateinit var textviewProofLabel: TextView

    private val requestPermissionLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Re-trigger the camera logic if needed, but for simplicity we'll just toast
            android.widget.Toast.makeText(requireContext(), "Permission granted. Please try taking the photo again.", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            android.widget.Toast.makeText(requireContext(), "Camera permission is required to take proof photos.", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private val takeProofPicture = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            capturedProofUri = tempProofUri
            imageviewRepairProof.setImageURI(capturedProofUri)
            imageviewRepairProof.visibility = View.VISIBLE
            textviewProofLabel.visibility = View.VISIBLE
        }
    }

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
        val buttonTakePhoto = view.findViewById<Button>(R.id.buttonTakePhoto)
        
        imageviewRepairProof = view.findViewById(R.id.imageviewRepairProof)
        textviewProofLabel = view.findViewById(R.id.textviewProofLabel)

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
            buttonTakePhoto.visibility = View.GONE
        } else if (request.status == "In Progress") {
            buttonAccept.visibility = View.GONE
            buttonFinish.visibility = View.VISIBLE
            buttonTakePhoto.visibility = View.VISIBLE
            
            if (request.proofPhotoUri != null) {
                imageviewRepairProof.setImageURI(Uri.parse(request.proofPhotoUri))
                imageviewRepairProof.visibility = View.VISIBLE
                textviewProofLabel.visibility = View.VISIBLE
                capturedProofUri = Uri.parse(request.proofPhotoUri)
            }
        } else {
            buttonAccept.visibility = View.GONE
            buttonFinish.visibility = View.GONE
            buttonTakePhoto.visibility = View.GONE
            
            if (request.proofPhotoUri != null) {
                imageviewRepairProof.setImageURI(Uri.parse(request.proofPhotoUri))
                imageviewRepairProof.visibility = View.VISIBLE
                textviewProofLabel.visibility = View.VISIBLE
            }
        }

        buttonAccept.setOnClickListener {
            DataManager.updateRequestStatus(request.id, "In Progress")
            dismiss()
        }

        buttonTakePhoto.setOnClickListener {
            if (androidx.core.content.ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                try {
                    val photoFile = java.io.File(requireContext().cacheDir, "proof_${System.currentTimeMillis()}.jpg")
                    tempProofUri = androidx.core.content.FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.fileprovider",
                        photoFile
                    )
                    takeProofPicture.launch(tempProofUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    android.widget.Toast.makeText(requireContext(), "Camera could not be opened.", android.widget.Toast.LENGTH_SHORT).show()
                }
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

        buttonFinish.setOnClickListener {
            if (capturedProofUri == null) {
                android.widget.Toast.makeText(requireContext(), "Proof photo is required to finish the repair.", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val updatedRequest = request.copy(status = "Completed", proofPhotoUri = capturedProofUri.toString())
            DataManager.updateRequest(updatedRequest)
            
            dismiss()
        }
    }
}
