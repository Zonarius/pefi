import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Accordion, AccordionDetails, AccordionSummary, Chip, Typography } from "@mui/material";
import { DataGrid, GridColDef, GridRenderCellParams } from "@mui/x-data-grid";
import api, { Transaction } from "../api/api";
import { groupBy, NO_ELEMENT, useObservable } from "../util/util";

export default function Errors() {
    const errors = useObservable(() => api.scriptErrors());

    if (errors === NO_ELEMENT) {
        return null;
    }

    const errs = groupBy(errors.errors, "__type");
    const missingTagTxs = (errs.missingTagError ?? [])
        .map(err => ({
            ...err.transaction,
            amount: err.transaction.amount / 100            
        }));

    return (
        <div>
            <Accordion defaultExpanded={true}>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>
                        Tag missing
                    </Typography>
                    <Chip
                        style={{marginLeft: "5px"}}
                        label={errs.missingTagError?.length ?? 0}
                        color="error"
                        size="small"
                    />
                </AccordionSummary>
                <AccordionDetails>
                    <TransactionTable transactions={missingTagTxs} />
                </AccordionDetails>
            </Accordion>
        </div>
    )
}

interface TransactionTableProps {
    transactions: Transaction[]
}

const columns: GridColDef[] = [
    { field: "iban", headerName: "IBAN", hide: true },
    { field: "description", headerName: "Description", minWidth: 2000, renderCell: TableCell },
    { field: "paymentDate", headerName: "Payment Date", hide: true },
    { field: "transactionDate", headerName: "Transaction Date", hide: true },
    { field: "amount", headerName: "Amount", hide: false },
    { field: "currency", headerName: "Currency", hide: true },
]

function TransactionTable({ transactions }: TransactionTableProps) {
    return (
        <div>
            <DataGrid
                autoHeight
                getRowId={tx => tx.hash}
                rows={transactions}
                columns={columns}
                pageSize={20}
                rowsPerPageOptions={[5]}
                disableSelectionOnClick
            />
        </div>
    );
}

function TableCell(props: GridRenderCellParams) {
    return (
        <div style={{
            whiteSpace: "pre",
            fontFamily: "monospace"
        }}>
            {props.value}
        </div>
    )
}