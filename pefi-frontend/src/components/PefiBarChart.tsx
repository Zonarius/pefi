import { useTheme } from "@mui/system";
import { filter, reduceBy, pipe, toPairs, map } from "ramda";
import { Bar, BarChart, CartesianGrid, Legend, ReferenceLine, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { Transaction } from "../api/api";
import { usePefiStore } from "../store";
import { amountReducer } from "../util/txUtil";

interface PefiBarChartProps {
    year?: number;
    month?: number;
}

export default function PefiBarChart({ year }: PefiBarChartProps) {
    const transactions = usePefiStore(state => state.transactions);
    const theme = useTheme();

    const values = pipe(
        filter<Transaction>(tx => tx.paymentDate.getFullYear() === year),
        reduceBy<Transaction, number>(amountReducer, 0, tx => String(tx.paymentDate.getMonth() + 1)),
        toPairs<number>,
        map(x => ({ name: x[0], EUR: x[1] / 100 }))
    )(transactions)

    return (
        <ResponsiveContainer height={490} width="100%">
            <BarChart data={values}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                <ReferenceLine y={0} stroke="#000" />
                <Bar dataKey="EUR" fill={theme.palette.success.light} />
            </BarChart>
        </ResponsiveContainer>
    )
}