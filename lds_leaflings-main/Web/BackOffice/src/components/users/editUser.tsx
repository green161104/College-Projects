import {
  SimpleForm,
  Edit,
  TextInput,
  NumberInput,
  BooleanInput,
  ImageInput,
  ImageField,
} from "react-admin";

/**
 * `EditUser` Component
 *
 * This component is used for editing user details within a `react-admin` interface.
 * The user can view and update attributes like username, contact, location, care experience,
 * water availability, luminosity needed, image, and whether the user is a paid user.
 */
export default function EditUser() {
  // const postFilters = [
  //   <SearchInput source="q" alwaysOn />,
  //   <TextInput label="UserName" source="username" />,
  // ];

  return (
    // <List filters={postFilters}>
    <Edit title="Edit User">
      <SimpleForm>
        <TextInput source="username" label="Username" disabled />
        <NumberInput source="contact" label="Contact" disabled />
        <TextInput source="location" label="Location" disabled />
        <TextInput source="careExperience" label="Care Experience" disabled />
        
        <TextInput
          source="waterAvailability"
          label="Water Availability"
          disabled
        />

        <TextInput
          source="luminosityAvailability"
          label="Luminosity Needed"
          disabled
        />

        <ImageInput source="plantImage" label="Image">
          <ImageField source="plantImage" title="title" />
        </ImageInput>
        
        <BooleanInput source="rolePaid" label="Is Paid User?" />
      </SimpleForm>
    </Edit>
  );
}
