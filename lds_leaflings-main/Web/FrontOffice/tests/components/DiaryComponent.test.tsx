import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi, describe, it, expect } from "vitest";
import '@testing-library/jest-dom';
import MyDiaryDisplayComponent from "../../src/components/Diary/DiaryComponent";
import React from "react";
import { Diary } from "../../src/models/diary-model";
import { Log } from "../../src/models/log-model";
import * as plantService from "../../src/services/http/plant/plant-service";
import { Task } from "../../src/models/task-model";

// Mock the entire module
vi.mock("../../src/services/http/plant/plant-service", () => ({
  getTasksByPlant: vi.fn()
}));



describe("MyDiaryDisplayComponent", () => {
  // Mock data
  const mockDiary: Diary = {
      id: 1,
      title: "My Plant Diary",
      UserPlantId: 1,
      Datetime: new Date()
  };

  const mockLogs:Log[] = [
    {
        id: 1,
        logDate: new Date(),
        logDescription: "First log entry with some detailed description",
        diaryId: 1
    },
    {
        id: 2,
        logDate: new Date(),
        logDescription: "Second log entry with another description",
        diaryId: 1
    }
  ];

  const mockTasks: Task[] = [
    {
      id: 1,
      adminId: 1,
      plantId: 1,
      taskName: "Task 1",
      taskDescription: "This is a task"
    },
    {
      id: 2,
      adminId: 1,
      plantId: 1,
      taskName: "Task 2",
      taskDescription: "This is a task"
    }
  ]

  it("renders diary title correctly", () => {
    render(<MyDiaryDisplayComponent 
      diary={mockDiary} 
      logs={mockLogs} 
      plantId={1} 
    />);

    expect(screen.getByText("My Plant Diary")).toBeInTheDocument();
  });

  it("displays logs when available", () => {
    render(<MyDiaryDisplayComponent 
      diary={mockDiary} 
      logs={mockLogs} 
      plantId={1} 
    />);

    expect(screen.getByText(/First log entry/)).toBeInTheDocument();
    expect(screen.getByText(/Second log entry/)).toBeInTheDocument();
  });

  it("shows 'No logs available' when no logs exist", () => {
    render(<MyDiaryDisplayComponent 
      diary={mockDiary} 
      logs={[]} 
      plantId={1} 
    />);

    expect(screen.getByText("No logs available. Start by adding one!")).toBeInTheDocument();
  });

  it("opens add log modal when 'Add Log' button is clicked", async () => {
    render(<MyDiaryDisplayComponent 
      diary={mockDiary} 
      logs={mockLogs} 
      plantId={1} 
    />);

    const addLogButton = screen.getByText("Add Log");
    await userEvent.click(addLogButton);

    expect(screen.getByText("Add New Log")).toBeInTheDocument();
  });

  it("opens task modal and fetches tasks when 'View Plant Tasks' is clicked", async () => {
    // Use vi.spyOn or cast to vi.Mock
    const mockGetTasksByPlant = vi.spyOn(plantService, 'getTasksByPlant')
      .mockResolvedValue(mockTasks);

    render(<MyDiaryDisplayComponent 
      diary={mockDiary} 
      logs={mockLogs} 
      plantId={1} 
    />);

    const viewTasksButton = screen.getByText("View Plant Tasks");
    await userEvent.click(viewTasksButton);

    // Wait for tasks to be fetched and modal to open
    await waitFor(() => {
      expect(mockGetTasksByPlant).toHaveBeenCalledWith(1);
    });

    // Optional: restore the mock
    mockGetTasksByPlant.mockRestore();
  });
});


