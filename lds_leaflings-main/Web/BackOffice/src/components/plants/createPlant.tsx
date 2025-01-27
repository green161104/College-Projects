import {
  ArrayInput,
  Create,
  ImageField,
  ImageInput,
  SelectInput,
  SimpleForm,
  SimpleFormIterator,
  TextInput,
  required,
} from "react-admin";

import { LuminosityAvailability } from "../../models/enums/LuminosityAvailability";
import { WaterAvailability } from "../../models/enums/WaterAvailability";
import { CareExperience } from "../../models/enums/CareExperience";
import { PlantTypes } from "../../models/enums/PlantTypes";

/**
 * CreatePlant component for creating a new plant.
 *
 * This form allows the user to input various details about a new plant, including its
 * name, description, type, care experience level, water needs, luminosity needs, an image,
 * and related tasks. The form uses `react-admin`'s `Create` and `SimpleForm` components to manage 
 * the input fields and their validations.
 */
export default function CreatePlant() {
  return (
    <Create title="Create Plant">
      <SimpleForm>
        <TextInput label="Name" source="name" validate={required("Plant name is required")} />

        <TextInput label="Description" source="description" validate={required("Plant description is required")}/>


        <SelectInput
          label="Type"
          source="type"
          choices={[
            { id: PlantTypes.Decorative, name: "Decorative" },
            { id: PlantTypes.Medicinal, name: "Medicinal" },
            { id: PlantTypes.Fruit, name: "Fruit" },
            { id: PlantTypes.Vegetable, name: "Vegetable" },
            { id: PlantTypes.Flower, name: "Flower" },
            { id: PlantTypes.Succulent, name: "Succulent" },
          ]}
          defaultValue={PlantTypes.Decorative}
          validate={required("Type is required")}
        />

        <SelectInput
          label="Experience"
          source="expSuggested"
          choices={[
            { id: CareExperience.Beginner, name: "Beginner" },
            { id: CareExperience.Intermidiate, name: "Intermidiate" },
            { id: CareExperience.Expert, name: "Expert" },
          ]}
          defaultValue={CareExperience.Beginner}
          validate={required("Experience is required")}
        />

        <SelectInput
          label="Water Need"
          source="waterNeeds"
          choices={[
            { id: WaterAvailability.Low, name: "Low" },
            { id: WaterAvailability.Medium, name: "Medium" },
            { id: WaterAvailability.High, name: "High" },
          ]}
          defaultValue={WaterAvailability.Low}
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
          defaultValue={LuminosityAvailability.Low}
          validate={required("Luminosity Needed is required")}
        />

        <ImageInput source="plantImage" label="Image" >
          <ImageField source="url" title="title" />
        </ImageInput>

        <ArrayInput source="tasks">
          <SimpleFormIterator disableReordering inline>
            <TextInput label="Task Name" source="taskName" validate={required("Tas name is required")} />
            <TextInput label="Task Description" source="taskDescription" validate={required("Task description is required")} />
          </SimpleFormIterator>
        </ArrayInput>

      </SimpleForm>
    </Create>
  );
}

/**
 * <ReferenceInput> -> pode ser útil, permite-me ir buscar um valor a uma outra tabela
 */
