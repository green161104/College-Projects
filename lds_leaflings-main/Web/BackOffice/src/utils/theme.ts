import { defaultTheme } from 'react-admin'
import { deepmerge } from "@mui/utils";
import lightGreen from '@mui/material/colors/lightGreen';
import red from '@mui/material/colors/red';

export const myTheme = deepmerge(defaultTheme, {
    palette: {
        primary: {
            main: lightGreen[500],
            light: lightGreen[300],
            dark: lightGreen[700],
        },
        secondary: {
            main: lightGreen[500],
        },
        error: {
            main: red[500],
        },
        contrastThreshold: 3,
        tonalOffset: 0.2,
    },
    typography: {
        // Use the system font instead of the default Roboto font.
        fontFamily: [
            "-apple-system",
            "BlinkMacSystemFont",
            '"Segoe UI"',
            "Arial",
            "sans-serif",
        ].join(","),
    }
});
