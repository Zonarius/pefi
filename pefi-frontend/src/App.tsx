import { Javascript, Merge, PieChart, Upload } from '@mui/icons-material';
import { Chip, CircularProgress } from '@mui/material';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import createTheme from '@mui/material/styles/createTheme';
import ThemeProvider from '@mui/material/styles/ThemeProvider';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { useEffect } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import api from './api/api';
import ErrorMenuItem from './ErrorMenuItem';
import ListItemLink from './ListItemLink';
import Errors from './pages/Errors';
import Overview from './pages/Overview';
import QuickMerge from './pages/QuickMerge';
import Script from './pages/Script';
import UploadCsv from './pages/UploadCsv';
import { usePefiStore } from './store';

const drawerWidth = 210;
const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

function App() {
  const setTransactions = usePefiStore(state => state.setTransactions);
  useEffect(() => {
    const sub = api.transactions().subscribe(txs => setTransactions(txs.transactions));
    return () => sub.unsubscribe();
  }, [])
  
  return (
    <BrowserRouter>
      <ThemeProvider theme={darkTheme}>
        <CssBaseline />
        <Box sx={{ display: 'flex' }}>
          <PefiTitle />
          <PefiDrawer />
          <MainContent />
        </Box>
      </ThemeProvider>
    </BrowserRouter>
  );
}

const routes = [
  {
    path: "/",
    element: <Overview />,
    title: "Overview"
  },
  {
    path: "script",
    element: <Script />,
    title: <ScriptTitle />
  },
  {
    path: "errors",
    element: <Errors />,
    title: "Errors"
  },
  {
    path: "upload",
    element: <UploadCsv />,
    title: "Upload CSV"
  },
  {
    path: "merge",
    element: <QuickMerge />,
    title: "Quick Merge"
  }
]

function ScriptTitle() {
  const state = usePefiStore(state => state.scriptState);
  const commonProps = {
    size: "small",
    style: {
      marginLeft: 5
    }
  };
  return (
    <>
      Scripts
      {
        state === "SAVED" ?
          <Chip label="Saved" color="success" size="small" style={{marginLeft: 5}} />
        : state === "CHANGED" ?
          <Chip label="Changed" color="warning" size="small" style={{marginLeft: 5}} />
        : <CircularProgress />
      }
    </>
  );
}

function PefiTitle() {
  return <AppBar
    position="fixed"
    sx={{ width: `calc(100% - ${drawerWidth}px)`, ml: `${drawerWidth}px` }}
  >
    <Toolbar>
      <Typography variant="h6" noWrap component="div">
        <Routes>
          {routes.map(r => (
            <Route key={r.path} path={r.path} element={r.title} />
          ))}
        </Routes>
      </Typography>
    </Toolbar>
  </AppBar>;
}

function PefiDrawer() {
  return <Drawer
    sx={{
      width: drawerWidth,
      flexShrink: 0,
      '& .MuiDrawer-paper': {
        width: drawerWidth,
        boxSizing: 'border-box',
      },
    }}
    variant="permanent"
    anchor="left"
  >
    <Toolbar />
    <Divider />
    <List>
      <ListItemLink icon={<PieChart />} primary="Overview" to="/" />
    </List>
    <Divider />
    <List>
      <ListItemLink icon={<Javascript />} primary="Script" to="/script" />
      <ErrorMenuItem />
    </List>
    <Divider />
    <List>
      <ListItemLink icon={<Upload />} primary="Upload CSV" to="/upload" />
      <ListItemLink icon={<Merge />} primary="Quick Merge" to="/merge" />
    </List>
  </Drawer>;
}

function MainContent() {
  return <Box
    component="main"
    sx={{ flexGrow: 1, bgcolor: 'background.default', p: 3 }}
  >
    <Toolbar />
    <Routes>
      {routes.map(r => (
        <Route key={r.path} {...r} />
      ))}
    </Routes>
  </Box>;
}


export default App;
