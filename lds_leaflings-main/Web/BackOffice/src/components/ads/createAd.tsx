import {
  BooleanInput,
  Create,
  DateInput,
  ImageField,
  ImageInput,
  minValue,
  required,
  SimpleForm,
} from "react-admin";

/**
 * CreateAd component for creating a new advertisement.
 *
 * This component renders a form for creating a new ad, including fields for
 * activation status, start and end dates, and an image. It uses `react-admin`
 * components like `BooleanInput`, `DateInput`, and `ImageInput` to manage the form inputs.
 */
export default function CreateAd() {
  return (
    <Create title="Create Ad">
      <SimpleForm>
        {/* BooleanInput to set whether the ad is active or not */}
        <BooleanInput label="Active" source="isActive" />

        {/* DateInput for selecting the start date of the ad */}
        <DateInput
          label="Start Date"
          source="startDate"
          validate={
            // Validation for the start date field: required and must be in the future
            (required("Start Date is required"),
            minValue(new Date(), "Start date must be in the future"))
          }
        />

        {/* DateInput for selecting the end date of the ad */}
        <DateInput
          label="End Date"
          source="endDate"
          validate={
            // Validation for the end date field: required and must be in the future
            (required("End Date is required"),
            minValue(new Date(), "End date must be in the future"))
          }
        />

        {/* ImageInput for uploading an image for the ad */}
        <ImageInput label="Image" source="adFile">
          {/* ImageField to display the uploaded image */}
          <ImageField source="url" title="title" />
        </ImageInput>
      </SimpleForm>
    </Create>
  );
}
