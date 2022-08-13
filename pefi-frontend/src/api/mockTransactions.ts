import { toTags } from "../util/txUtil";
import { parseDate } from "../util/util";
import { TagString, Transaction } from "./api";
import Random from "./random";

const rng = new Random();

export default function mockTransactions(): Transaction[] {
    return [
        ...mockGehalt(),
        ...mockBilla()
    ]
}

export const mockSimpleTransaction = (() => {
    let hash = 0;
    return () => ({
        hash: "" + (hash++),
        ...mockTags("online.amazon"),
        iban: "AT321234567890123456",
        amount: 123,
        currency: "EUR",
        description: "POS           6,98 EUR K2   01.01. 00STO Amazon.co.jp Ama|zon.co.jp 12345                   SPESEN: 1,14  KURS:|1,0  VOM:                                   VD/000001234|BAWAATWWXXX AT321234567890123456 VS-Verrechnungskonto",
        paymentDate: parseDate("2022-01-01"),
        transactionDate: parseDate("2022-01-01")
    })
})();

function mockTags(tags: TagString) {
    return {
        tags,
        tagParts: toTags(tags)
    };
}

function mockGehalt(): Transaction[] {
    return rangeInclusive(2017, 2022).flatMap(year => 
        rangeInclusive(1, 12).flatMap(month => 
            tx("income.gehalt.xyz", euro(2000), date(27, month, year))
        ));
}

function mockBilla(): Transaction[] {
    return rangeInclusive(2017, 2022).flatMap(year => 
        rangeInclusive(1, 12).flatMap(month => 
        rangeInclusive(0, 4).map(day => 
            tx("essen.geschäft.billa", rng.nextEuros(-50, -10), date(27, month, year))
        )));
}

const tx = (() => {
    var hash = 0;
    return (tags: TagString, amount: number, date: Date) => ({
        hash: "" + (hash++),
        ...mockTags(tags),
        iban: "AT321234567890123456",
        amount,
        currency: "EUR",
        description: "POS           6,98 EUR K2   01.01. 00STO Amazon.co.jp Ama|zon.co.jp 12345                   SPESEN: 1,14  KURS:|1,0  VOM:                                   VD/000001234|BAWAATWWXXX AT321234567890123456 VS-Verrechnungskonto",
        paymentDate: date,
        transactionDate: date
    })
})();

function euro(amount: number): number {
    return amount * 100;
}

function date(day: number, month: number, year: number): Date {
    return parseDate(`${year}-${month}-${day}`)
}

function rangeInclusive(from: number, to: number): number[] {
    const res = [];
    for (let i = from; i <= to; i++) {
        res.push(i);
    }
    return res;
}
