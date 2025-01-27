import {
  BooleanInput,
  DateInput,
  Edit,
  ImageField,
  ImageInput,
  minValue,
  required,
  SimpleForm,
} from "react-admin";

/**
 * EditAd component for editing an existing advertisement.
 *
 * This component renders a form for updating the details of an existing ad.
 * It includes fields for activation status, start and end dates, and an image.
 * The form is built using `react-admin`'s `Edit` and `SimpleForm` components.
 */
export default function EditAd() {
  return (
    <Edit title="Edit Ad">
      <SimpleForm>
        <BooleanInput source="isActive" label="Active" />

        <DateInput
          label="Start Date"
          source="startDate"
          validate={
            (required("Start Date is required"),
            minValue(new Date(), "Start date must be in the future"))
          }
        />

        <DateInput
          label="End Date"
          source="endDate"
          validate={
            (required("End Date is required"),
            minValue(new Date(), "End date must be in the future"))
          }
        />

        <ImageInput label="Image" source="adFile">
          <ImageField source="url" title="title" />
        </ImageInput>
      </SimpleForm>
    </Edit>
  );
}
