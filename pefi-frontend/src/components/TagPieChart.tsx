import { allPass, always, filter, map, pipe, reduceBy } from "ramda";
import { Pie, PieChart, ResponsiveContainer, Tooltip } from "recharts";
import { TagString, Transaction } from "../api/api";
import { usePefiStore } from "../store";
import { amountReducer, depth, hasDate, matchesTagString } from "../util/txUtil";
import { Predicate, toArray } from "../util/util";

export interface TagPieChartProps {
    year?: number;
    month?: number;
    day?: number;
    tags?: TagString;
    additionalPredicate?: Predicate<Transaction>
}

export default function TagPieChart({ year, month, day, tags, additionalPredicate }: TagPieChartProps) {
    const transactions = usePefiStore(state => state.transactions);

    const dt = depth(tags);

    const data = pipe(
        filter(allPass([
            additionalPredicate ? additionalPredicate : always(true),
            hasDate(year, month, day),
            matchesTagString(tags)
        ])),
        reduceBy<Transaction, number>(amountReducer, 0, tx => tx.tagParts[dt] ?? "<unknown>"),
        toArray,
        map(x => ({...x, value: Math.abs(x.value / 100)}))
    )(transactions);

    return (
        <ResponsiveContainer width="50%" height={250}>
            <PieChart height={250}>
                <Pie data={data} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={100} fill="#8884d8" />
                <Tooltip />
            </PieChart>
        </ResponsiveContainer>
    );
}