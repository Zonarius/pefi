import { Observable, Subject } from "rxjs";
import { onSubscribe } from "../util/rxUtil";
import { toTags } from "../util/txUtil";
import { parseDate } from "../util/util";
import { Api, LoadScriptResponse, SaveScriptRequest, ScriptErrors, Transaction, TransactionsResponse } from "./api";

export default class HttpApi implements Api {

    private transactionSubject = new Subject<TransactionsResponse>();
    private scriptErrorSubject = new Subject<ScriptErrors>();

    public transactions(): Observable<TransactionsResponse> {
        return this.transactionSubject.pipe(
            onSubscribe(() => this.loadTransactions())
        );
    }

    public scriptErrors(): Observable<ScriptErrors> {
        return this.scriptErrorSubject.pipe(
            onSubscribe(() => this.loadScriptErrors())
        );
    }

    public loadScript(): Promise<LoadScriptResponse> {
        return this.getJson("/api/script");
    }

    public async saveScript(script: SaveScriptRequest): Promise<void> {
        await this.postJson("/api/script", script);
        this.loadTransactions();
        this.loadScriptErrors();
    }

    private async loadTransactions() {
        const txs: ServerTransactions = await this.getJson("/api/transactions");
        this.transactionSubject.next({
            transactions: txs.transactions.map(toTransaction)
        })
    }

    private async loadScriptErrors() {
        const errs: ErrorsResponse = await this.getJson("/api/script/errors");
        this.scriptErrorSubject.next({
            errors: errs.errors.map(err => ({
                __type: err.type,
                transaction: toTransaction(err.transaction)
            }))
        })
    }

    private getJson(path: string): Promise<any> {
        return fetch(path).then(val => val.json());
    }

    private postJson(path: string, body: any): Promise<any> {
        return fetch(path, {
            method: "POST",
            body: JSON.stringify(body),
            headers: {
                "Content-Type": "application/json"
            }
        })
    }
}

function toTransaction(tx: ServerTransaction): Transaction {
    return {
        hash: tx.hash,
        amount: tx.amount,
        currency: tx.currency,
        description: tx.description,
        iban: tx.iban,
        tags: tx.tag,
        tagParts: toTags(tx.tag),
        paymentDate: parseDate(tx.paymentDate),
        transactionDate: parseDate(tx.transactionDate)
    };
}

interface ErrorsResponse {
    errors: Array<{
        type: "missingTagError",
        transaction: ServerTransaction
    }>
}

interface ServerTransactions {
    transactions: ServerTransaction[]
}

interface ServerTransaction {
    hash: string;
    tag: string;
    iban: string;
    description: string;
    paymentDate: string;
    transactionDate: string;
    amount: number;
    currency: string;
}