package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val DEFAULT_BALANCE = 0.00

/**
 * A simple [Fragment] subclass.
 * Use the [TransferFundsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferFundsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var balance = DEFAULT_BALANCE

    private lateinit var accountNumberInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var transferButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(arguments)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            balance = it.getDouble("balance")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer_funds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountNumberInput = view.findViewById(R.id.transferFundsAccountEditText)
        amountInput = view.findViewById(R.id.transferFundsAmountEditText)
        transferButton = view.findViewById(R.id.transferFundsButton)

        transferButton.setOnClickListener { handleTransferButtonClick() }
    }

    private val accountNumber: String
        get() = accountNumberInput.text.toString()

    private val amount: String
        get() = amountInput.text.toString()

    private fun isValidAccountNumber() : Boolean {
        val regex = Regex("^(sa|ca)\\d{4}$")
        return accountNumber.matches(regex)
    }

    private fun isValidAmount() : Boolean {
        if (amount.isBlank()) {
            return false
        }

        return amount.toDouble() > 0
    }

    private fun hasSufficientBalance() : Boolean {
        return balance >= amount.toDouble()
    }

    private fun onInvalidAccountNumber() {
        accountNumberInput.error = "Invalid account number"
    }

    private fun onInvalidAmount() {
        amountInput.error = "Invalid amount"
    }

    private fun onInsufficientBalance() {
        val text = String.format("%.2f", amount.toDouble()).let { "Not enough funds to transfer $$it" }
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun onSufficientBalance() {
        val text = String.format("%.2f", amount.toDouble())
                .let { "Transferred $$it to account $accountNumber" }
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        val result = Bundle()
        result.putDouble("balance", balance.toDouble() - amount.toDouble())
        parentFragmentManager.setFragmentResult("transferFund", result)
        findNavController().navigateUp()
    }

    private fun handleTransferButtonClick() {
        var isInvalid = false

        if (!isValidAccountNumber()) {
            isInvalid = true
            onInvalidAccountNumber()
        }

        if (!isValidAmount()) {
            isInvalid = true
            onInvalidAmount()
        }

        if (isInvalid) {
            return
        }

        if (!hasSufficientBalance()) {
            return onInsufficientBalance()
        }

        onSufficientBalance()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransferFundsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransferFundsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}