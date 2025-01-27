import {
  ArrayInput,
  Edit,
  ImageField,
  ImageInput,
  required,
  SelectInput,
  SimpleForm,
  SimpleFormIterator,
  TextInput,
} from "react-admin";

import { CareExperience } from "../../models/enums/CareExperience";
import { WaterAvailability } from "../../models/enums/WaterAvailability";
import { LuminosityAvailability } from "../../models/enums/LuminosityAvailability";

/**
 * EditPlant component for editing an existing plant's details.
 *
 * This form allows the user to edit various details of an existing plant, including its name, 
 * care experience level, water and luminosity needs, image, and related tasks. It uses `react-admin`'s
 * `Edit` and `SimpleForm` components to manage the input fields and their validations.
 */
export default function EditPlant() {
  return (
    <Edit title="Edit Plant">
      <SimpleForm>
        <TextInput label="Name" source="name" validate={required("Plant name is required")} />

        <SelectInput
          label="Experience"
          source="expSuggested"
          choices={[
            { id: CareExperience.Beginner, name: "Beginner" },
            { id: CareExperience.Intermidiate, name: "Intermidiate" },
            { id: CareExperience.Expert, name: "Expert" },
          ]}
          validate={required("Experience is required")}
          optionText="name"
          optionValue="id"
        />

        <SelectInput
          label="Water Need"
          source="waterNeeds"
          choices={[
            { id: WaterAvailability.Low, name: "Low" },
            { id: WaterAvailability.Medium, name: "Medium" },
            { id: WaterAvailability.High, name: "High" },
          ]}
          validate={required("Water Need is required")}
        />

        <SelectInput
          label="Luminosity Needed"
          source="luminosityNeeded"
          choices={[
            { id: LuminosityAvailability.Low, name: "Low" },
            { id: LuminosityAvailability.Medium, name: "Medium" },
            { id: LuminosityAvailability.High, name: "High" },
          ]}
          validate={required("Luminosity Needed is required")}
        />

        <ImageInput source="plantImage" label="Image">
          <ImageField source="url" title="title" />
        </ImageInput>

        <ArrayInput source="tasks">
          <SimpleFormIterator disableReordering inline>
            <TextInput label="Task Name" source="taskName" validate={required("Task name is required")} />
            <TextInput label="Task Description" source="taskDescription" validate={required("Task Description is required")} />
          </SimpleFormIterator>
        </ArrayInput>
        
      </SimpleForm>
    </Edit>
  );
}
