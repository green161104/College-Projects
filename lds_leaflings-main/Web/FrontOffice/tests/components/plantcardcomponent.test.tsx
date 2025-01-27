import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';
import { PlantType } from '../../src/models/enums/PlantType';
import { User } from '../../src/models/user-model';
import { Ad } from '../../src/models/Ad';
import { Plant } from '../../src/models/plant-model';
import { ExperienceLevels } from '../../src/models/enums/ExperienceLevels';
import { WaterLevels } from '../../src/models/enums/WaterLevels';
import { LightLevel } from '../../src/models/enums/LightLevel';
import PlantCardComponent from "../../src/components/Reusables/PlantCardComponent"
import React from 'react';

// Mock the services and utilities
vi.mock('../../services/http/plant/plant-service', () => ({
  getFullImageUrl: (image: string) => `https://example.com/${image}`
}));

// Sample test data
const mockPlant: Plant = {
  id: 1,
  name: 'Test Plant',
  type: PlantType.Succulent,
  description: 'A beautiful test plant',
  plantImage: 'test-plant.jpg',
  expSuggested: ExperienceLevels.Beginner,
  waterNeeds: WaterLevels.High,
  luminosityNeeded: LightLevel.High
};

const mockUser: User = {
  id: 1,
  username: 'testuser',
  rolePaid: true,
  email: '',
  contact: '',
  location: '',
  careExperience: 'Beginner',
  waterAvailability: 'Low',
  luminosityAvailability: 'Low',
  userAvatar: ''
};

const mockAd: Ad = {
  id: 1,
  adminId: 0,
  isActive: false,
  startDate: new Date(),
  endDate: new Date(),
  adFile: ''
};

describe('PlantCardComponent', () => {
  it('renders plant card with correct information', () => {
    render(<PlantCardComponent plant={mockPlant} userData={mockUser} />);
    
    // Check plant name
    const plantName = screen.getByText('Test Plant');
    expect(plantName).toBeInTheDocument();
    
    // Check plant type
    const plantType = screen.getByText('Succulent');
    expect(plantType).toBeInTheDocument();
    
    // Check image
    const plantImage = screen.getByAltText('Test Plant');
    expect(plantImage).toHaveAttribute('src', 'undefined/test-plant.jpg');
  });

  it('opens plant display popup when card is clicked', async () => {
    const user = userEvent.setup();
    
    render(<PlantCardComponent plant={mockPlant} userData={mockUser} ad={mockAd} />);
    
    // Click the plant card
    const plantCard = screen.getByTestId('plant-card');
    await user.click(plantCard);
    
    // Check if plant display popup is opened
    const plantDisplayPopup = screen.getByTestId('plant-display-popup');
    expect(plantDisplayPopup).toBeInTheDocument();
    
    
    const popupPlantDescription = screen.getByText('A beautiful test plant');
    expect(popupPlantDescription).toBeInTheDocument();
  });

  it('closes plant display popup by clicking outside', async () => {
    const user = userEvent.setup();
    
    render(<PlantCardComponent plant={mockPlant} userData={mockUser} ad={mockAd} />);
    
    // Open the popup
    const plantCard = screen.getByTestId('plant-card');
    await user.click(plantCard);
    
    // Verify popup is open
    const plantDisplayPopup = screen.getByTestId('plant-display-popup');
    expect(plantDisplayPopup).toBeInTheDocument();
    
    // Click outside the popup (on the background overlay)
    await user.click(document.body);
    
    // Check that popup is no longer in the document
    const closedPopup = screen.queryByTestId('plant-display-popup');
    expect(closedPopup).not.toBeInTheDocument();
  });

  it('renders different plant types correctly', () => {
    const testCases = [
      { type: PlantType.Succulent, expectedText: 'Succulent' },
      { type: PlantType.Flower, expectedText: 'Flower' },
      { type: PlantType.Fruit, expectedText: 'Fruit' }
    ];

    testCases.forEach(({ type, expectedText }) => {
      const testPlant = { ...mockPlant, type };
      render(<PlantCardComponent plant={testPlant} userData={mockUser} />);
      
      const plantType = screen.getByText(expectedText);
      expect(plantType).toBeInTheDocument();
      
      // Clear the previous render
      vi.clearAllMocks();
    });
  });

  it('handles plant type conversion correctly', () => {
    const testCases = [
      { input: 'succulent', expected: 'Succulent' },
      { input: 'VEGETABLE', expected: 'Vegetable' },
      { input: 0, expected: 'Decorative' },
    ];

    testCases.forEach(({ input, expected }) => {
      const testPlant = { ...mockPlant, type: input as any };
      render(<PlantCardComponent plant={testPlant} userData={mockUser} />);
      
      const plantType = screen.getByText(expected);
      expect(plantType).toBeInTheDocument();
      
      // Clear the previous render
      vi.clearAllMocks();
    });
  });
});