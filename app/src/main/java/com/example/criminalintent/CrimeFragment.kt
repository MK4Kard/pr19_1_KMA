package com.example.criminalintent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.MessageFormat
import java.util.UUID

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: ExtendedFloatingActionButton
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var sendButton: ExtendedFloatingActionButton
    private lateinit var reportButton: ExtendedFloatingActionButton

    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onCrimeUpdated(crime: Crime)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        titleField = view.findViewById(R.id.crime_title)
        dateButton = view.findViewById(R.id.crime_date)
        solvedCheckBox = view.findViewById(R.id.crime_solved)
        sendButton = view.findViewById(R.id.send)
        reportButton = view.findViewById(R.id.choose)

        dateButton.text = DateFormat.getDateFormat(context).format(crime.date)
        dateButton.isEnabled = false

        solvedCheckBox.isEnabled = false

        return view
    }

    override fun onStart() {
        super.onStart()

        titleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
                solvedCheckBox.isEnabled = crime.title.isNotEmpty()
                callbacks?.onCrimeUpdated(crime)
            }
        })

        solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
            dateButton.isEnabled = isChecked
            callbacks?.onCrimeUpdated(crime)
        }

        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.also { intent ->
                startActivity(
                    Intent.createChooser(intent, getString(R.string.send_report))
                )
            }
        }

        sendButton.setOnClickListener {
            val pickContactIntent = Intent(
                Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI
            )
            startActivityForResult(pickContactIntent, REQUEST_CONTACT)
        }
    }

    private fun getCrimeReport(): String {
        val solvedString =
            if (crime.isSolved) getString(R.string.crime_report_solved)
            else getString(R.string.crime_report_unsolved)

        val suspectString =
            if (crime.suspect.isBlank()) getString(R.string.crime_report_no_suspect)
            else getString(R.string.crime_report_suspect, crime.suspect)

        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()

        return MessageFormat.format(
            getString(R.string.crime_report),
            crime.title,
            dateString,
            solvedString,
            suspectString
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {

            REQUEST_CONTACT -> {
                val contactUri: Uri = data?.data ?: return

                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

                val cursor = requireActivity().contentResolver.query(
                    contactUri, queryFields, null, null, null
                )

                cursor?.use {
                    if (it.moveToFirst()) {
                        val suspect = it.getString(0)
                        crime.suspect = suspect
                        sendButton.text = suspect
                        callbacks?.onCrimeUpdated(crime)
                    }
                }
            }
        }
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val REQUEST_CONTACT = 1
        private const val DATE_FORMAT = "EEE, MMM dd"

        fun newInstance(crimeId: UUID): CrimeFragment {
            return CrimeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CRIME_ID, crimeId)
                }
            }
        }
    }
}
