import { MonoTypeOperatorFunction, tap } from "rxjs";

export function onSubscribe<T>(subscribe: () => any): MonoTypeOperatorFunction<T> {
    return tap({ subscribe })
}