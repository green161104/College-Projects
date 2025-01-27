import {
  Datagrid,
  TextField,
  List,
  BooleanField,
} from "react-admin";


/**
 * `ListUser` Component
 *
 * This component displays a list of users in a tabular format using `react-admin`.
 * Each row in the table represents a user and their attributes, including username, contact,
 * paid status, location, care experience, water availability, and luminosity availability.
 */
export default function ListUser() {

  return (
    <List>
      <Datagrid>
        <TextField title="Username" source="username"></TextField>
        <TextField title="Contact" source="contact"></TextField>
        <BooleanField title="Is Paid User" source="rolePaid"></BooleanField>
        <TextField title="Location" source="location"></TextField>
        <TextField title="Care Experience" source="careExperience"></TextField>
        <TextField title="Water Availability" source="waterAvailability"></TextField>
        <TextField title="Luminosity Availability" source="luminosityAvailability"></TextField>
      </Datagrid>
    </List>
  );
}
