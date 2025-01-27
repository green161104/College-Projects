import {
  Create,
  PasswordInput,
  SimpleForm,
  TextInput,
  regex,
  required,
} from "react-admin";

/**
 * CreateAdmin component for creating a new admin user.
 *
 * This component renders a form with fields for username, email, password, and contact number.
 * It uses `react-admin`'s `Create` and `SimpleForm` components to handle the creation of a new admin.
 * Form validation is applied to ensure the correct format and required fields.
 */
export default function CreateAdmin() {
  // Validation rules for each input field
  // Contact validation ensures the phone number has exactly 9 digits
  const contactValidation = [
    regex(/^\d{9}/, "Phone number must have 9 digits"),
    required("Phone number is required"),
  ];

  // Email validation ensures a valid email format
  const emailValidation = [
    regex(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
      "Invalid email"
    ),
    required("Email is required"),
  ];

  // Password validation ensures the password has at least 6 characters
  const passwordValidation = [
    regex(/^.{6,}$/, "Password must contain at least 6 characters"),
    required("Password is required"),
  ];

  return (
    <Create title="Create Admin">
      <SimpleForm>
        {/* TextInput for the username field with required validation */}
        <TextInput label="Username" source="username" validate={required()} />

        {/* TextInput for the email field with email validation */}
        <TextInput label="Email" source="email" validate={emailValidation} />

        {/* PasswordInput for the password field with password validation */}
        <PasswordInput
          label="Password"
          source="password"
          validate={passwordValidation}
        />

        {/* TextInput for the contact field with contact validation */}
        <TextInput label="Contact" source="contact" validate={contactValidation} />
      </SimpleForm>
    </Create>
  );
}
