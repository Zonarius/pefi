import seedrandom from "seedrandom";

export default class Random {
    private rng = seedrandom("27")

    public nextEuros(from: number, to: number): number {
        return Math.floor(100 * ((this.rng() * (to - from)) + from));
    }
}
