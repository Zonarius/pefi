import { TagTwoTone } from "@mui/icons-material";
import { allPass } from "ramda";
import { Tag, TagString, Transaction } from "../api/api";
import { isDefined, Predicate, ToArrayResult } from "./util";


export function hasYear(year?: number): Predicate<Transaction> {
    if (!isDefined(year)) {
        return () => true;
    }
    return tx => tx.paymentDate.getFullYear() === year;
}

export function hasMonth(month?: number): Predicate<Transaction> {
    if (!isDefined(month)) {
        return () => true;
    }
    return tx => tx.paymentDate.getMonth() === month;
}

export function hasDay(day?: number): Predicate<Transaction> {
    if (!isDefined(day)) {
        return () => true;
    }
    return tx => tx.paymentDate.getDate() === day;
}

export function hasDate(year?: number, month?: number, day?: number): Predicate<Transaction> {
    return allPass([hasYear(year), hasMonth(month), hasDay(day)]);
}

export function isIncome(tx: Transaction): boolean {
    return tx.amount > 0;
}

export function isExpense(tx: Transaction): boolean {
    return tx.amount < 0;
}

export function matchesTagString(tagString?: TagString): Predicate<Transaction> {
    if (!isDefined(tagString)) {
        return () => true;
    }
    return tx => tx.tags.startsWith(tagString);
}

export function depth(tagString?: TagString): number {
    return toTags(tagString).length;
}

export function toTags(tagString?: TagString): Tag[] {
    if (!isDefined(tagString)) {
        return [];
    }
    return tagString.split(".");
}

export function amountReducer(acc: number, tx: Transaction): number {
    return acc + tx.amount;
}