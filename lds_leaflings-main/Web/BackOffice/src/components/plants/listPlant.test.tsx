import {
  AdminContext,
  defaultDataProvider,
  GetListResult,
  Resource,
} from "react-admin";
import { render, screen } from "@testing-library/react";
import ListPlant from "./listPlant";

const customDataProvider = {
  ...defaultDataProvider,
  getList: () =>
    Promise.resolve<GetListResult>({
      data: [
        {
          id: 1,
          plantImage: "foo",
          name: "foo",
          type: "Fruit",
          luminosityNeeded: "High",
          waterNeeds: "Low",
          expSuggested: "Beginner",
        },
        {
          id: 2,
          plantImage: "bar",
          name: "bar",
          type: "Decorative",
          luminosityNeeded: "Low",
          waterNeeds: "High",
          expSuggested: "Expert",
        },
      ],
      total: 2,
    }),
};

describe("<ListPlant/>", () => {
  it("Should render correctly", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="Plant" list={ListPlant} />
      </AdminContext>
    );

    const items = await screen.findAllByText(/foo|bar/);
    expect(items).toHaveLength(2);
  });

  it("Should display the correct plant names", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="Plant" list={ListPlant} />
      </AdminContext>
    );
    expect(await screen.findByText("Fruit")).toBeInTheDocument();
    expect(await screen.findByText("Decorative")).toBeInTheDocument();
  });

  it("Should display the correct expSuggested", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="Plant" list={ListPlant} />
      </AdminContext>
    );
    expect(await screen.findByText("Beginner")).toBeInTheDocument();
    expect(await screen.findByText("Expert")).toBeInTheDocument();
  });
});
