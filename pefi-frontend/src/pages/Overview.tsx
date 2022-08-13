import { Typography } from "@mui/material";
import { CSSProperties } from "react";
import PefiBarChart from "../components/PefiBarChart";
import TagPieChart from "../components/TagPieChart";
import { isExpense, isIncome } from "../util/txUtil";

export default function Overview() {
    const pieLabelStyle: CSSProperties = {
        justifySelf: "center"
    }
    return (
        <>
            <div style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr",
                width: "100%"
            }}>
                <div style={pieLabelStyle}><Typography variant="h4">Income</Typography></div>
                <div style={pieLabelStyle}><Typography variant="h4">Expense</Typography></div>
            </div>
            <div style={{
                display: "flex",
                marginBottom: 25
            }}>
                <TagPieChart year={2020} additionalPredicate={isIncome} />
                <TagPieChart year={2020} additionalPredicate={isExpense} />
            </div>
            <PefiBarChart year={2020} />
        </>
    )
}