import { Admin, Resource } from "react-admin";
import { myTheme } from "./utils/theme";
import MyLoginPage from "./components/MyLoginPage";
import { authProvider } from "./providers/authProvider";
import { dataProvider } from "./providers/dataProvider";
import ListAdmin from "./components/admins/listAdmin";
import ListUser from "./components/users/listUser";
import ListAd from "./components/ads/listAd";
import ListPlant from "./components/plants/listPlant";
import CreateAdmin from "./components/admins/createAdmin";
import CreatePlant from "./components/plants/createPlant";
import EditAd from "./components/ads/editAd";
import EditAdmin from "./components/admins/editAdmin";
import EditPlant from "./components/plants/editPlant";
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import PersonIcon from '@mui/icons-material/Person';
import YardIcon from '@mui/icons-material/Yard';
import CreateAd from "./components/ads/createAd";
import CampaignIcon from '@mui/icons-material/Campaign';

function App() {
  return (
    <Admin
      theme={myTheme}
      loginPage={MyLoginPage}
      authProvider={authProvider}
      dataProvider={dataProvider}
    >
      <Resource name="Admin" list={ListAdmin} create={CreateAdmin} edit={EditAdmin} icon={AdminPanelSettingsIcon}/>
      <Resource name="User" list={ListUser} icon={PersonIcon}/>
      <Resource name="Plant" list={ListPlant} create={CreatePlant} edit={EditPlant} icon={YardIcon}/>
      <Resource name="Ad" list={ListAd} create={CreateAd} edit={EditAd} icon={CampaignIcon}/>
    </Admin>
  );
}

export default App;
