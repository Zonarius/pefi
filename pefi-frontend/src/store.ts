import create from 'zustand'
import { Transaction } from './api/api'
import { ScriptsState } from './pages/Script'

export interface AppState {
    transactions: Transaction[]
    scriptState: ScriptsState
    setScriptState(scriptState: ScriptsState): void;
    setTransactions(transactions: Transaction[]): void;
}

export const usePefiStore = create<AppState>((set) => ({
    transactions: [],
    scriptState: "SAVED",
    setScriptState(scriptState) {
        set({ scriptState })
    },
    setTransactions(transactions: Transaction[]) {
        set({ transactions })
    }
}))
