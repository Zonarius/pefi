import { parseISO } from "date-fns";
import { merge, NEVER, of } from "rxjs";
import stripIndent from "strip-indent";
import { parseDate } from "../util/util";
import { Api, ScriptErrors, TransactionsResponse } from "./api";
import mockTransactions, { mockSimpleTransaction } from "./mockTransactions";



const mockApi: Api = {
    transactions: mock("transactions", of<TransactionsResponse>({
        transactions: mockTransactions()
    })),
    scriptErrors: mock("scriptErrors", merge(
        NEVER,
        of<ScriptErrors>({
            errors: [
                {
                    __type: "missingTagError",
                    transaction: mockSimpleTransaction()
                },
                {
                    __type: "missingTagError",
                    transaction: mockSimpleTransaction()
                }
            ]
        })
    )),
    saveScript: mockPromise("saveScript", undefined),
    loadScript: mockPromise("loadScript", {
        displayName: "Billa",
        slug: "billa",
        script: stripIndent(`
        /**
         * This function is called for every transaction in the export.
         * The return value will be assigned to the transaction.
         * 
         * @param   {Object} tx                 The transaction
         * @param   {string} tx.iban            The IBAN of the transaction
         * @param   {string} tx.description     The description of the transaction, lines are split by "|"
         * @param   {string} tx.paymentDate     The date of the payment, ISO-formatted (ex. "2022-12-31")
         * @param   {string} tx.transactionDate The date of the transaction, ISO-formatted (ex. "2022-12-31")
         * @param   {number} tx.amount          The amount of the transaction, positive for income, negative for costs
         * @param   {string} tx.currency        The currency of the transaction (ex. "EUR")
         * @returns {string|undefined}          Tag of the transaction, separated by dots (ex. "essen.geschäft.billa"). Undefined or empty string if this script does not assign a tag to the transaction.
         */
        function getTag(tx) {
          return "essen.geschäft.billa"
        }
        `)
    })
}

function mock<T>(name: string, value: T): () => T {
    return () => {
        console.log(name);
        return value;
    }
}

function mockPromise<T>(name: string, value: T): () => Promise<T> {
    return mock(name, Promise.resolve(value));
}

export default mockApi;