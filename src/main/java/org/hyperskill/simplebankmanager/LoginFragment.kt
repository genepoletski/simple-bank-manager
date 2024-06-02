package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val DEFAULT_USERNAME = "Lara"
private const val DEFAULT_PASSWORD = 1234

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var storedUsername = DEFAULT_USERNAME
    private var storedPassword = DEFAULT_PASSWORD

    private lateinit var loginUsernameEditView: EditText
    private lateinit var loginPasswordEditView: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* TODO: This is a workaround. It's not great that we add AppCompatActivity dependency
            to the Fragment. Consider using Fragment Result API and Bundle to pass data
            between Activity and Fragment */
        val intent = (view.context as AppCompatActivity).intent
        intent.extras?.let {
            storedUsername = it.getString("username") ?: storedUsername
            storedPassword = it.getString("password")?.toInt() ?: storedPassword
        }

        loginUsernameEditView = view.findViewById(R.id.loginUsername)
        loginPasswordEditView = view.findViewById(R.id.loginPassword)
        loginButton = view.findViewById(R.id.loginButton)

        loginButton.setOnClickListener { handleLoginButtonClicked() }
    }

    private val loginUsername: String
        get() = loginUsernameEditView.text.toString()

    private val loginPassword: String
        get() = loginPasswordEditView.text.toString()

    private fun handleLoginButtonClicked() {
        if (isValidLoginUsername() && isValidLoginPassword()) {
            return onLoginSucces()
        }
        onLoginError()
    }

    private fun isValidLoginUsername() = loginUsername == storedUsername

    private fun isValidLoginPassword(): Boolean {
        if (loginPassword.isBlank()) {
            return false
        }

        if (loginPassword.toInt() != storedPassword) {
            return false
        }

        return true
    }

    private fun onLoginSucces() {
        Toast.makeText(requireContext(), "logged in", Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            R.id.action_loginFragment_to_userMenuFragment,
            bundleOf("username" to loginUsername)
        )
    }

    private fun onLoginError() {
        Toast.makeText(requireContext(), "invalid credentials", Toast.LENGTH_SHORT).show()
    }

    companion object {
        // TODO: Research why this needed
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}