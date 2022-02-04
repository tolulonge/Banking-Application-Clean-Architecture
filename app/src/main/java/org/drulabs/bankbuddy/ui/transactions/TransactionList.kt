package org.drulabs.bankbuddy.ui.transactions

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_transaction_list.*
import org.drulabs.bankbuddy.R
import org.drulabs.bankbuddy.presentation.factory.ViewModelFactory
import org.drulabs.bankbuddy.presentation.model.Status
import org.drulabs.bankbuddy.presentation.model.Transaction
import org.drulabs.bankbuddy.presentation.viewmodels.TransactionsVM
import org.drulabs.bankbuddy.utils.ALPHA_HIDDEN
import org.drulabs.bankbuddy.utils.ALPHA_VISIBLE
import org.drulabs.bankbuddy.utils.setCustomChecked
import javax.inject.Inject

class TransactionList : AppCompatActivity(),
    TransactionsAdapter.TransactionClickListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var transactionsVM: TransactionsVM

    private val transactionListAdapter = TransactionsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)

        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.title_transactions)
        }

        rvTransactionList.layoutManager = LinearLayoutManager(this)
        rvTransactionList.adapter = transactionListAdapter

        chkTransactionCredit.setOnCheckedChangeListener(this)
        chkTransactionDebit.setOnCheckedChangeListener(this)
        chkTransactionFlagged.setOnCheckedChangeListener(this)

        transactionsVM = ViewModelProviders.of(this, viewModelFactory)
            .get(TransactionsVM::class.java)
        transactionsVM.transactionListSource.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    println("Transactions Loading")
                    showLoader()
                }
                Status.ERROR -> {
                    println("Transactions ERROR: ${it.message}")
                    hideLoader()
                }
                Status.SUCCESS -> {
                    hideLoader()
                    it.data?.let {transactions ->
                        transactionListAdapter.populate(transactions)
                        supportActionBar?.let { actionBar ->
                            actionBar.title = "Transactions (${transactions.size})"
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_transaction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                android.R.id.home -> {
                    this@TransactionList.finish()
                }
                R.id.action_reset_filter -> {
                    chkTransactionCredit.setCustomChecked(false, this)
                    chkTransactionDebit.setCustomChecked(false, this)
                    chkTransactionFlagged.setCustomChecked(false, this)
                    transactionsVM.resetFilters()
                }
            }
        }
        return false
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        buttonView?.let {
            transactionsVM.filterTransactions(
                credit = chkTransactionCredit.isChecked,
                debit = chkTransactionDebit.isChecked,
                flagged = chkTransactionFlagged.isChecked
            )
        }
    }

    override fun onTransactionTapped(transaction: Transaction) {
        Toast.makeText(this, "${transaction.amountInCents} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onFlagToggled(transaction: Transaction) {
        transactionsVM.toggleFlaggedStatus(transaction)
    }

    private fun showLoader() {
        pbTransactionLoader.visibility = View.VISIBLE
        rvTransactionList.alpha = ALPHA_HIDDEN
        llTransactionFilterHolder.alpha = ALPHA_HIDDEN
    }

    private fun hideLoader() {
        pbTransactionLoader.visibility = View.GONE
        rvTransactionList.alpha = ALPHA_VISIBLE
        llTransactionFilterHolder.alpha = ALPHA_VISIBLE
    }
}
