import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi, describe, it, expect } from "vitest";
import '@testing-library/jest-dom';
import LogDetailPopup from "../../src/components/Diary/LogDetailComponent";
import { Log } from "../../src/models/log-model";
import React from "react";

describe("LogDetailPopup", () => {
  // Mock log data
  const mockLog: Log = {
    id: 1,
    logDate: new Date('2023-05-15'),
    logDescription: "This is a detailed log entry about my plant's growth.",
    diaryId: 1
  };

  // 1. Rendering Tests


  it("does not render when log is not provided", () => {
    const { container } = render(
      <LogDetailPopup 
        onClose={() => {}} 
        log={undefined as any} 
      />
    );

    expect(container.firstChild).toBeNull();
  });

  it("renders log details when open", () => {
    render(
      <LogDetailPopup 
        onClose={() => {}} 
        log={mockLog} 
      />
    );

    // Check log date
    expect(screen.getByText("Mon May 15 2023")).toBeInTheDocument();

    // Check log description
    expect(screen.getByText("This is a detailed log entry about my plant's growth.")).toBeInTheDocument();

    // Check close button exists
    expect(screen.getByText("Close")).toBeInTheDocument();
  });

  // 2. Interaction Tests
  it("calls onClose when close button is clicked", async () => {
    const mockOnClose = vi.fn();

    render(
      <LogDetailPopup 
        onClose={mockOnClose} 
        log={mockLog} 
      />
    );

    const closeButton = screen.getByText("Close");
    await userEvent.click(closeButton);

    expect(mockOnClose).toHaveBeenCalledTimes(1);
  });

  // 3. Accessibility and UI Tests
  it("has correct aria attributes", () => {
    render(
      <LogDetailPopup 
        onClose={() => {}} 
        log={mockLog} 
      />
    );

    const popup = screen.getByText(mockLog.logDescription).closest('div');
    expect(popup).toHaveClass("bg-white");
    expect(popup).toHaveClass("rounded-lg");
  });

  // 4. Date Formatting Test
  it("formats date correctly", () => {
    render(
      <LogDetailPopup 
        onClose={() => {}} 
        log={mockLog} 
      />
    );

    // Check if date is formatted as expected
    expect(screen.getByText("Mon May 15 2023")).toBeInTheDocument();
  });

  // 5. Snapshot Test
  it("matches snapshot", () => {
    const { asFragment } = render(
      <LogDetailPopup 
        onClose={() => {}} 
        log={mockLog} 
      />
    );

    expect(asFragment()).toMatchSnapshot();
  });
});