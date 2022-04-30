package ru.gb.kotlinapp.view.phone

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.FragmentPhoneBinding

class PhoneFragment : Fragment() {
    private var _binding: FragmentPhoneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissionContactPhone()
    }

    private fun checkPermissionContactPhone() {
        val resultRead =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        val resultCall =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
        if (resultRead == PERMISSION_GRANTED && resultCall == PERMISSION_GRANTED) {
            getContacts()
        } else {
            myRequestPermission()
        }
    }

    private fun myRequestPermission() {
        permissionLoad.launch(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
            )
        )
    }

    private val permissionLoad = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permitted ->
        val read = permitted.getValue(Manifest.permission.READ_CONTACTS)
        val call = permitted.getValue(Manifest.permission.CALL_PHONE)
        when {
            read && call -> {
                getContacts()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                showSettings()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                showSettings()
            }
            else -> {
                showRationale()
            }
        }
    }

    private fun showRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.need_permission_tittle))
            .setMessage(getString(R.string.need_permission_message))
            .setPositiveButton(getString(R.string.need_permission_ok)) { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton(getString(R.string.need_permission_no)) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .create()
            .show()
    }

    private fun showSettings() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.need_permission_tittle))
            .setMessage(
                "${getString(R.string.need_permission_message)} \n" +
                        getString(R.string.need_permission_message_again)
            )
            .setPositiveButton(getString(R.string.need_permission_ok)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.need_permission_no)) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .create()
            .show()
    }

    private fun openAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireActivity().packageName}")
        )
        settingsLoad.launch(appSettingsIntent)
    }

    private val settingsLoad = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { checkPermissionContactPhone() }

    private fun getContacts() {
        context?.let { context ->
            val contResolver = context.contentResolver
            val cursor = contResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursor?.let { cursor ->
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val view = TextView(context).apply {
                        text = name
                        textSize = 25f
                    }
                    binding.phoneContactContainer.addView(view)
                    view.setOnClickListener {
                        getNumberPhoneOnID(contResolver, contactId)
                    }
                }
                cursor.close()
            }
        }
    }

    private fun getNumberPhoneOnID(contResolver: ContentResolver, contactId: String) {
        val phones = contResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
        )
        var number: String
        var home: String? = null
        var mobile: String? = null
        var work: String? = null
        var other: String? = null
        phones?.let { cursor ->
            while (cursor.moveToNext()) {
                number =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                when (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> home = number
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> mobile = number
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> work = number
                    else -> other = number
                }
            }
            phones.close()
        }
        viewPhone(home, mobile, work, other)
    }

    private fun viewPhone(home: String?, mobile: String?, work: String?, other: String?) {
        val lineView = LinearLayout(requireContext())
        lineView.orientation = VERTICAL
        home?.let { number ->
            lineView.addView(createButton(number, getString(R.string.home)))
        }
        mobile?.let { number ->
            lineView.addView(createButton(number, getString(R.string.mobile)))
        }
        work?.let { number ->
            lineView.addView(createButton(number, getString(R.string.work)))
        }
        other?.let { number ->
            lineView.addView(createButton(number, getString(R.string.other)))
        }
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.call))
            .setView(lineView)
            .create()
            .show()
    }

    private fun createButton(number: String, text: String): View {
        val button = Button(requireContext())
        button.text = text
        button.setOnClickListener {
            goCall(number)
        }
        return button
    }

    private fun goCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhoneFragment()
    }
}