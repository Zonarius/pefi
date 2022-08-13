import { Error } from "@mui/icons-material";
import { Badge, ListItem, ListItemIcon, ListItemText } from "@mui/material";
import React from "react";
import { Link, LinkProps } from "react-router-dom";
import { map, startWith } from "rxjs";
import api from "./api/api";
import { useObservable, useObservableWithDefault } from "./util/util";

export default function ErrorMenuItem() {
    const to = "/errors";
    const errorCount = useObservableWithDefault(0, () => api.scriptErrors().pipe(
        map(errs => errs.errors.length)
    ));

    const renderLink = React.useMemo(
        () =>
            React.forwardRef<HTMLAnchorElement, Omit<LinkProps, 'to'>>(function MyLink(
                itemProps,
                ref,
            ) {
                return <Link to={to} ref={ref} {...itemProps} role={undefined} />;
            }),
        [to],
    );

    return (
        <li>
            <ListItem button component={renderLink}>
                <ListItemIcon>
                    <Badge badgeContent={errorCount} color="error">
                        <Error />
                    </Badge>
                </ListItemIcon>
                <ListItemText primary="Errors" />
            </ListItem>
        </li>
    );
  }