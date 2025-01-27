import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';
import MyGardenDisplayComponent from '../../src/components/MyGarden/MyGardenDisplayComponent';
import { UserPlant } from '../../src/models/userPlant-model';
import { Plant } from '../../src/models/plant-model';
import { User } from '../../src/models/user-model';
import { Ad } from '../../src/models/Ad';
import { PlantType } from '../../src/models/enums/PlantType';
import { WaterLevels } from '../../src/models/enums/WaterLevels';
import { ExperienceLevels } from '../../src/models/enums/ExperienceLevels';
import { LightLevel } from '../../src/models/enums/LightLevel';
import React from 'react';

describe('MyGardenDisplayComponent', () => {
  // Mock data for testing
  const mockPlants: Plant[] = [
    {
        id: 1,
        name: 'Rose',
        type: PlantType.Decorative,
        description: 'Beautiful flowering plant',
        waterNeeds: WaterLevels.High,
        expSuggested: ExperienceLevels.Beginner,
        luminosityNeeded: LightLevel.Low,
        plantImage: ''
    },
    {
        id: 2,
        name: 'Lily',
        type: PlantType.Decorative,
        description: 'Beautiful flowering plant',
        waterNeeds: WaterLevels.High,
        expSuggested: ExperienceLevels.Beginner,
        luminosityNeeded: LightLevel.Low,
        plantImage: ''
    },
    {
        id: 3,
        name: 'Pumpkin',
        type: PlantType.Vegetable,
        description: 'Edible vegetable plant',
        waterNeeds: WaterLevels.High,
        expSuggested: ExperienceLevels.intermediate,
        luminosityNeeded: LightLevel.High,
        plantImage: ''
    }
  ];

  const mockUserPlants: UserPlant[] = mockPlants.map((plant, index) => ({
    id: Number(index+1),
    plantId: plant.id,
    userId: 1,
    plant: plant
  }));

  const mockUser: User = {
      id: 1,
      username: 'testuser',
      email: 'test@example.com',
      contact: '415415415445',
      rolePaid: false,
      location: 'DHAIUJDIA',
      careExperience: 'Beginner',
      waterAvailability: 'Low',
      luminosityAvailability: 'Low',
      userAvatar: ''
  };

  const mockAd: Ad = {
      id: 1,
      adFile: 'Test Ad',
      adminId: 1,
      isActive: true,
      startDate: new Date(),
      endDate: new Date()
  };

  it('renders no plants message when userplants is empty', () => {
    render(
      <MyGardenDisplayComponent 
        userplants={[]} 
        userData={mockUser} 
        adItem={mockAd} 
      />
    );

    expect(screen.getByText('No plants to display.')).toBeInTheDocument();
  });

  it('renders plants when userplants are provided', () => {
    render(
      <MyGardenDisplayComponent 
        userplants={mockUserPlants} 
        userData={mockUser} 
        adItem={mockAd} 
      />
    );

    expect(screen.getByText('Rose')).toBeInTheDocument();
    expect(screen.getByText('Lily')).toBeInTheDocument();
  });

  it('filters plants by search query', async () => {
    const user = userEvent.setup();

    render(
      <MyGardenDisplayComponent 
        userplants={mockUserPlants} 
        userData={mockUser} 
        adItem={mockAd} 
      />
    );

    const searchInput = screen.getByPlaceholderText('Search plants...');
    
    await user.type(searchInput, 'Rose');

    // Only Rose should be visible
    expect(screen.getByText('Rose')).toBeInTheDocument();
    expect(screen.queryByText('Lily')).not.toBeInTheDocument();
  });
  
 it('filters plants by type', async () => {
    const user = userEvent.setup();

    render(
      <MyGardenDisplayComponent 
        userplants={mockUserPlants} 
        userData={mockUser} 
        adItem={mockAd} 
      />
    );

    // Use role to find the Decorative filter button
    const decorativeButton = screen.getByRole('button', { name: /Decorative/i });
    await user.click(decorativeButton);

    // Wait for and check the filtered results
    await waitFor(() => {
      expect(screen.getByText('Rose')).toBeInTheDocument();
      expect(screen.getByText('Lily')).toBeInTheDocument();
      expect(screen.queryByText('Pumpkin')).not.toBeInTheDocument();
    });
  });
  
  it('handles combination of search and type filter', async () => {
    const user = userEvent.setup();

    render(
      <MyGardenDisplayComponent 
        userplants={mockUserPlants} 
        userData={mockUser} 
        adItem={mockAd} 
      />
    );

    // First, select the Vegetable filter
    const vegetableButton = screen.getByRole('button', { name: /Vegetable/i });
    await user.click(vegetableButton);

    // Then enter a search query
    const searchInput = screen.getByPlaceholderText('Search plants...');
    await user.type(searchInput, 'Pump');

    // Wait for and check the filtered results
    await waitFor(() => {
      expect(screen.getByText('Pumpkin')).toBeInTheDocument();
      expect(screen.queryByText('Rose')).not.toBeInTheDocument();
      expect(screen.queryByText('Lily')).not.toBeInTheDocument();
    });
  });

it('resets to all plants when "All" filter is selected', async () => {
    const user = userEvent.setup();

    render(
      <MyGardenDisplayComponent 
        userplants={mockUserPlants} 
        userData={mockUser} 
        adItem={mockAd} 
      />
    );

    // Filter to Vegetable
    const vegetableFilterButton = screen.getByRole("button", { name: /Vegetable/i });
    await user.click(vegetableFilterButton);

    // Reset to All
    const allFilterButton = screen.getByRole("button", { name: /All/i });
    await user.click(allFilterButton);

    // Expect all plants to be visible again
    await waitFor(() => {
      expect(screen.getByText('Rose')).toBeInTheDocument();
      expect(screen.getByText('Lily')).toBeInTheDocument();
    });
});

});

