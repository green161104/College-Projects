import { AdminContext, defaultDataProvider, GetListResult, Resource } from "react-admin";
import { render, screen } from "@testing-library/react";
import ListUser from "./listUser";

const customDataProvider = {
    ...defaultDataProvider,
    getList: () =>
      Promise.resolve<GetListResult>({
        data: [
          { id: 1, username: 'foo', contact: '937895666' , rolePaid: true, location: 'Porto', careExperience: 'Beginner', waterAvailability: 'Low', luminosityAvailability: 'Low' },
          { id: 2, username: 'bar', contact: '912546555', rolePaid: true, location: 'Lisbon', careExperience: 'Expert', waterAvailability: 'High', luminosityAvailability: 'High' },
        ],
        total: 2,
      }),
  };


describe("<ListUser/>", () => {
  it("Should render correctly", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="User" list={ListUser} />
      </AdminContext>
    );

    const items = await screen.findAllByText(/foo|bar/);
    expect(items).toHaveLength(2);
  });

  it("Should display the correct usernames", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="User" list={ListUser} />
      </AdminContext>
    );
    expect(await screen.findByText("foo")).toBeInTheDocument();
    expect(await screen.findByText("bar")).toBeInTheDocument();
  });

  it("Should display the correct contact information", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="User" list={ListUser} />
      </AdminContext>
    );
    expect(await screen.findByText("937895666")).toBeInTheDocument();
    expect(await screen.findByText("912546555")).toBeInTheDocument();
  });
});
