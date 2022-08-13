import { Observable } from "rxjs";
import HttpApi from "./httpApi";
import mockApi from "./mockApi";

export type JavaScript = string;
export type Iban = string;
export type IsoDate = string;
export type Currency = string;
export type TransactionHash = string;
export type Tag = string;
/** Ex. `income.gehalt.xyz` */
export type TagString = string;
export type Cents = number;

export interface Api {
    loadScript(): Promise<LoadScriptResponse>;
    saveScript(script: SaveScriptRequest): Promise<void>
    transactions(): Observable<TransactionsResponse>
    scriptErrors(): Observable<ScriptErrors>
}

export interface LoadScriptResponse {
    script: JavaScript;
}

export interface SaveScriptRequest {
    script: JavaScript;
}

export interface TransactionsResponse {
    transactions: Transaction[];
}

export interface ScriptErrors {
    errors: ScriptError[]
}

export type ScriptError = MissingTagError;

export interface MissingTagError {
    __type: "missingTagError"
    transaction: Transaction
}

export interface Transaction {
    hash: TransactionHash,
    tags: TagString;
    tagParts: Tag[];
    iban: Iban;
    description: string;
    paymentDate: Date;
    transactionDate: Date;
    amount: Cents;
    currency: Currency;
}


const api = new HttpApi();

export default api;