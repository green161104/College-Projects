import { AdminContext, defaultDataProvider, GetListResult, GetOneResult, Resource } from "react-admin";
import { render, screen } from "@testing-library/react";
import ListAdmin from "./listAdmin";

const customDataProvider = {
  ...defaultDataProvider,
  getList: () =>
    Promise.resolve<GetListResult>({
      data: [
        { id: 1, username: "foo", contact: "937895666" },
        { id: 2, username: "bar", contact: "123456789" },
      ],
      total: 2,
    }),
    getOne: () =>
      Promise.resolve<GetOneResult>({ data: { id: 1, username: "foo", contact: "937895666" } }),
};


describe("<ListAdmin/>", () => {
  it("Should render correctly", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="Admin" list={ListAdmin} />
      </AdminContext>
    );

    const items = await screen.findAllByText(/foo|bar/);
    expect(items).toHaveLength(2);
  });

  it("Should display the correct usernames", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="Admin" list={ListAdmin} />
      </AdminContext>
    );
    expect(await screen.findByText("foo")).toBeInTheDocument();
    expect(await screen.findByText("bar")).toBeInTheDocument();
  });

  it("Should display the correct contact information", async () => {
    render(
      <AdminContext dataProvider={customDataProvider}>
        <Resource name="Admin" list={ListAdmin} />
      </AdminContext>
    );
    expect(await screen.findByText("937895666")).toBeInTheDocument();
    expect(await screen.findByText("123456789")).toBeInTheDocument();
  });
});
