import {
  Datagrid,
  TextField,
  List,
} from "react-admin";

/**
 * ListAdmin component for displaying a list of admin users.
 *
 * This component renders a list of admins, showing their username and contact information.
 * It uses `react-admin`'s `List` and `Datagrid` components to display the data in a table format.
 */
export default function ListAdmin() {

  return (
    <List>
      <Datagrid>
        <TextField title="Username" source="username"></TextField>
        <TextField title="Contact" source="contact"></TextField>
      </Datagrid>
    </List>
  );
}
