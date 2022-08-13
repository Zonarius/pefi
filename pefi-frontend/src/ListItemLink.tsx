import { ListItem, ListItemIcon, ListItemText } from "@mui/material";
import React from "react";
import { Link, LinkProps } from "react-router-dom";

export interface ListItemLinkProps {
    icon?: React.ReactElement;
    primary: string;
    to: string;
}

export default function ListItemLink(props: ListItemLinkProps) {
    const { icon, primary, to } = props;

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
                {icon ? <ListItemIcon>{icon}</ListItemIcon> : null}
                <ListItemText primary={primary} />
            </ListItem>
        </li>
    );
}