import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import '@testing-library/jest-dom';
import AddLogComponent from "../../src/components/Diary/AddLogComponent";
import { beforeEach, describe, expect, it, vi } from "vitest";
import React from "react";
import { createNewLog } from "../../src/services/http/diarylogs/diarylogs-service";


// Mock the createNewLog function
// Mock the entire module
vi.mock("../../src/services/http/diarylogs/diarylogs-service", () => ({
  createNewLog: vi.fn()
}));

describe("AddLogComponent", () => {
  const mockOnClose = vi.fn();
  const mockOnUpdate = vi.fn();

  beforeEach(() => {
    // Reset mocks before each test
    vi.resetAllMocks();
    
    vi.mocked(createNewLog).mockResolvedValue({ success: true });
  });


  it("renders the modal when isOpen is true", () => {
    render(<AddLogComponent onClose={mockOnClose} diaryId={1} onUpdate={mockOnUpdate}/>);
    const text = screen.getByText("Add New Log")
    expect(text).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Enter log description...")).toBeInTheDocument();
  });

  it("shows an error if the description is empty", async () => {
    render(<AddLogComponent onClose={mockOnClose} diaryId={1} onUpdate={mockOnUpdate}/>);
    const submitButton = screen.getByText("Submit Log");

    await userEvent.click(submitButton);

    expect(await screen.findByText("Log description cannot be empty.")).toBeInTheDocument();
  });


  it("calls createNewLog with correct data and closes on success", async () => {
    render(<AddLogComponent onClose={mockOnClose} diaryId={1} onUpdate={mockOnUpdate}/>);

    const textarea = screen.getByPlaceholderText("Enter log description...");
    const submitButton = screen.getByText("Submit Log");

    await userEvent.type(textarea, "Test log description");
    
    expect(submitButton).toBeEnabled();
    
    await userEvent.click(submitButton);

    await waitFor(() => {
      expect(createNewLog).toHaveBeenCalledWith(1, "Test log description");
    });

    expect(mockOnClose).toHaveBeenCalled();
  });

  it("calls onClose when the cancel button is clicked", async () => {
    render(<AddLogComponent onClose={mockOnClose} diaryId={1} onUpdate={mockOnUpdate}/>);
    const cancelButton = screen.getByText("Cancel");

    await userEvent.click(cancelButton);

    expect(mockOnClose).toHaveBeenCalled();
  });

  it("displays error message when API call fails", async () => {
    // Mock the createNewLog to throw an error
    vi.mocked(createNewLog).mockRejectedValue(new Error("Network error"));
  
    render(<AddLogComponent onClose={mockOnClose} diaryId={1} onUpdate={mockOnUpdate}/>);
  
    const textarea = screen.getByPlaceholderText("Enter log description...");
    const submitButton = screen.getByText("Submit Log");
  
    await userEvent.type(textarea, "Test log description");
    await userEvent.click(submitButton);
  
    expect(await screen.findByText("An error occurred while creating the log.")).toBeInTheDocument();
  });

  it("disables submit button while loading", async () => {
    // Mock a slow API call
    vi.mocked(createNewLog).mockImplementation(() => 
      new Promise(resolve => setTimeout(() => resolve({ success: true }), 1000))
    );
  
    render(<AddLogComponent onClose={mockOnClose} diaryId={1} onUpdate={mockOnUpdate}/>);
  
    const textarea = screen.getByPlaceholderText("Enter log description...");
    const submitButton = screen.getByText("Submit Log");
  
    await userEvent.type(textarea, "Test log description");
    await userEvent.click(submitButton);
  
    expect(submitButton).toBeDisabled();
    expect(submitButton).toHaveTextContent("Submitting...");
  });

});
  
