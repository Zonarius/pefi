import { parse } from "date-fns";
import { map, pipe, toPairs } from "ramda";
import { useEffect, useState } from "react";
import { Observable, Subject } from "rxjs";

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

export type Predicate<T> = (elem: T) => boolean;

export function useEventObservable<T>(): [Observable<T>, (changedValue: T) => any] {
    const subject = new Subject<T>();
    return [
        subject,
        subject.next.bind(subject)
    ];
}

export function isString(x: any): x is string {
    return typeof x === "string";
}

export function isDefined<T>(x: T | undefined | null): x is T {
    return x != null;
}

export const NO_ELEMENT = Symbol();

export function useObservableWithDefault<T>(defaultValue: T, obs: () => Observable<T>): T {
    const value = useObservable(obs);
    return value === NO_ELEMENT
        ? defaultValue
        : value;
}

export function useObservable<T>(obs: () => Observable<T>): T | typeof NO_ELEMENT {
    const [value, setValue] = useState<T | typeof NO_ELEMENT>(NO_ELEMENT);
    useEffect(() => {
        const subscription = obs().subscribe({
            next: setValue,
            error: console.error
        })

        return () => {
            subscription.unsubscribe();
        }
    }, [])

    return value;
}

type MapValuesToKeysIfAllowed<T> = {
    [K in keyof T]: T[K] extends PropertyKey ? K : never;
};
type Filter<T> = MapValuesToKeysIfAllowed<T>[keyof T];

export function groupBy<T extends Record<PropertyKey, any>, Key extends Filter<T>>(
    arr: T[],
    key: Key
): Record<T[Key], T[]> {
    return arr.reduce((accumulator, val) => {
        const groupedKey = val[key];
        if (!accumulator[groupedKey]) {
            accumulator[groupedKey] = [];
        }
        accumulator[groupedKey].push(val);
        return accumulator;
    }, {} as Record<T[Key], T[]>);
}

export function parseDate(date: string): Date {
    return parse(date, "yyyy-MM-dd", new Date())
}

export interface ToArrayResult<K, V> {
    name: K;
    value: V;
}

export function toArray<K extends string, V>(obj: Record<K, V>): ToArrayResult<K, V>[] {
    return pipe(
        toPairs,
        map<any[], ToArrayResult<K, V>>(x => ({name: x[0], value: x[1]}))
    )(obj);
}