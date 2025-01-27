import {
    Datagrid,
    List,
    ImageField,
    BooleanField,
    DateField,
  } from "react-admin";
  

  /**
 * ListAd component for displaying a list of advertisements.
 *
 * This component renders a list of ads in a tabular format, showing details
 * such as the ad image, active status, start date, and end date.
 * It uses `react-admin`'s `List` and `Datagrid` components to structure the data.
 */
  export default function ListAd() {
    return (
      <List>
        <Datagrid>
          <ImageField source="adFile" title="Ad Image" />
          <BooleanField source="isActive" title="Is Active" />
          <DateField source="startDate" title="Start Date"/>
          <DateField source="endDate" title="End Date"/>
        </Datagrid>
      </List>
    );
  }
  