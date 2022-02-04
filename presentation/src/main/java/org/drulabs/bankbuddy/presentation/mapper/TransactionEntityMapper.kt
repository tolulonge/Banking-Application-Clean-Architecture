package org.drulabs.bankbuddy.presentation.mapper

import org.drulabs.bankbuddy.domain.entities.TransactionEntity
import org.drulabs.bankbuddy.presentation.model.Transaction
import javax.inject.Inject

class TransactionEntityMapper @Inject constructor() : Mapper<TransactionEntity, Transaction> {

    override fun from(e: Transaction): TransactionEntity {
        return TransactionEntity(
            transactionId = e.transactionId,
            type = e.type,
            amountInCents = e.amountInCents,
            commaSeparatedTags = e.commaSeparatedTags,
            timestamp = e.timestamp,
            flagged = e.flagged,
            remarks = e.remarks
        )
    }

    override fun to(t: TransactionEntity): Transaction {
        return Transaction(
            transactionId = t.transactionId,
            type = t.type,
            amountInCents = t.amountInCents,
            commaSeparatedTags = t.commaSeparatedTags,
            timestamp = t.timestamp,
            flagged = t.flagged,
            remarks = t.remarks,
            isHVT = t.isHVT
        )
    }
}