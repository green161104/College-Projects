import {
  Edit,
  required,
  SimpleForm,
  regex,
  TextInput,
} from "react-admin";


/**
 * EditAdmin component for editing an admin user's details.
 *
 * This component provides a form for updating the username and contact information
 * of an admin user. It uses `react-admin`'s `Edit` and `SimpleForm` components
 * to handle the editing process.
 */
export default function EditAdmin() {
    const contactValidation = [regex(/^\d{9}/, "Must have 9 numbers"), required()]
    

    return (
        <Edit title="Edit Plant">
            <SimpleForm>
                <TextInput label="username" source="username" validate={required()}/>
                <TextInput label="Contact" source="contact" validate={contactValidation}/>
            </SimpleForm>
        </Edit>
    );
}
