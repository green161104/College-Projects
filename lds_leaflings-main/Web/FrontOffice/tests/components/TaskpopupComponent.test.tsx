import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';
import TaskDetailPopup from '../../src/components/Diary/TaskpopupComponent';
import { Task } from '../../src/models/task-model';
import React from 'react';

describe('TaskDetailPopup', () => {
  // Mock tasks for testing
  const mockTasks: Task[] = [
    {
        id: 1,
        taskName: 'Water Plant',
        taskDescription: 'Water the plant thoroughly',
        adminId: 1,
        plantId: 1
    },
    {
        id: 2,
        taskName: 'Prune Branches',
        taskDescription: 'Remove dead or overgrown branches',
        adminId: 1,
        plantId: 1
    }
  ];


  it('renders message when tasks is empty', () => {
    render(
      <TaskDetailPopup 
        onClose={() => {}} 
        tasks={[]} 
      />
    );
    
    const noTasksMessage = screen.getByText('No tasks available for this plant.');
    expect(noTasksMessage).toBeInTheDocument();
  });

  it('renders popup with tasks when isOpen is true and tasks exist', () => {
    render(
      <TaskDetailPopup 
        onClose={() => {}} 
        tasks={mockTasks} 
      />
    );
    
    // Check popup title
    expect(screen.getByText('Plant Tasks')).toBeInTheDocument();

    // Check task names
    expect(screen.getByText('Water Plant')).toBeInTheDocument();
    expect(screen.getByText('Prune Branches')).toBeInTheDocument();

    // Check task descriptions
    expect(screen.getByText('Water the plant thoroughly')).toBeInTheDocument();
    expect(screen.getByText('Remove dead or overgrown branches')).toBeInTheDocument();
  });

  it('renders "No tasks available" message when tasks array is empty', () => {
    render(
      <TaskDetailPopup 
        onClose={() => {}} 
        tasks={[]} 
      />
    );
    
    expect(screen.getByText('No tasks available for this plant.')).toBeInTheDocument();
  });

  it('calls onClose when close button is clicked', async () => {
    const mockOnClose = vi.fn();
    const user = userEvent.setup();

    render(
      <TaskDetailPopup 
        onClose={mockOnClose} 
        tasks={mockTasks} 
      />
    );
    
    const closeButton = screen.getByText('Close');
    await user.click(closeButton);

    expect(mockOnClose).toHaveBeenCalledTimes(1);
  });
});