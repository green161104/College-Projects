import {
  Datagrid,
  TextField,
  EmailField,
  List,
  ImageField,
} from "react-admin";


/**
 * `ListPlant` Component
 *
 * This component displays a list of plants in a tabular format using `react-admin`.
 * Each row represents a plant with its details, such as an image, name, type,
 * luminosity needs, water needs, and the suggested care experience.
 */
export default function ListPlant() {

  return (
    <List>
      <Datagrid>
        <ImageField title="Plant Image" source="plantImage" />
        <TextField title="Name" source="name" />
        <EmailField title="Type" source="type" />
        <TextField title="Luminosity Needed" source="luminosityNeeded" />
        <TextField title="Water Needs" source="waterNeeds" />
        <TextField title="Suggested Experience" source="expSuggested" />
      </Datagrid>
    </List>
  );
}
