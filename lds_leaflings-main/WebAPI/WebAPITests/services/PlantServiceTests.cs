using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity.enums;
using WebAPI.Services;

namespace WebAPITests.services
{
    public class PlantServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly PlantService _plantService;
        //private readonly IConfiguration _configuration;

        /// <summary>
        /// Assigns the context and configuration to the PlantServiceTests class
        /// </summary>
        public PlantServiceTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            //_configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _plantService = new PlantService(_context);
        }

        /// <summary>
        /// Dispose method to delete the database after the tests are run
        /// </summary>
        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        /// <summary>
        /// Test case to verify that the GetPlantByIdAsync method returns a non-null plant when a valid plant ID is provided
        /// </summary>
        [Fact]
        public async void GetPlantByIdAsyncShouldNotBeNull()
        {
            var expectedPlantId = 1;
            var plant = await _plantService.GetPlantByIdAsync(expectedPlantId);

            Assert.NotNull(plant);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByIdAsync method returns null when an invalid plant ID is provided
        /// </summary>
        [Fact]
        public async void GetPlantByIdAsyncShouldBeNull()
        {
            var expectedPlantId = 100;
            var plant = await _plantService.GetPlantByIdAsync(expectedPlantId);

            Assert.Null(plant);
        }

        /// <summary>
        /// Test case to verify that the GetAllPlantsAsync method returns a non-empty list of plants
        /// </summary>
        [Fact]
        public async void GetAllPlantsAsyncShouldNotBeEmpty()
        {
            var totalPlants = 9;
            var plants = await _plantService.GetAllPlantsAsync();

            Assert.NotEmpty(plants);
            Assert.Equal(totalPlants, plants.Count);
        }

        /// <summary>
        /// Test case to verify that the SearchPlantsByNameAsync method returns a non-empty list of plants when a valid name is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByNameShouldNotBeEmpty()
        {
            var result = await _plantService.SearchPlantsByNameAsync("Plant");
            Assert.NotEmpty(result);
        }

        /// <summary>
        /// Test case to verify that the SearchPlantsByNameAsync method returns an empty list of plants when an invalid name is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByNameShouldBeEmpty()
        {
            var result = await _plantService.SearchPlantsByNameAsync("Plant123");
            Assert.Empty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByExpAsync method returns a non-empty list of plants when a valid experience level is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByExpShouldNotBeEmpty()
        {
            var result = await _plantService.GetPlantByExpAsync(ExperienceLevels.Beginner);
           
            Assert.NotEmpty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByExpAsync method returns an empty list of plants when an invalid experience level is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByExpShouldBeEmpty()
        {
            var result = await _plantService.GetPlantByExpAsync(ExperienceLevels.intermediate);

            Assert.Empty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByWaterNeeds method returns a non-empty list of plants when a valid water need level is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByWaterNeedsShouldNotBeEmpty()
        {
            var result = await _plantService.GetPlantByWaterNeeds(WaterLevels.Low);

            Assert.NotEmpty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByWaterNeeds method returns an empty list of plants when an invalid water need level is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByWaterNeedsShouldBeEmpty()
        {
            var result = await _plantService.GetPlantByWaterNeeds(WaterLevels.Medium);

            Assert.Empty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByLuminosityNeeded method returns a non-empty list of plants when a valid luminosity level is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByLuminosityShouldNotBeEmpty()
        {
            var result = await _plantService.GetPlantByLuminosityNeeded(LightLevel.Medium);

            Assert.NotEmpty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByLuminosityNeeded method returns an empty list of plants when an invalid luminosity level is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByLuminosityShouldBeEmpty()
        {
            var result = await _plantService.GetPlantByLuminosityNeeded(LightLevel.High);

            Assert.Empty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByTypePlant method returns a non-empty list of plants when a valid plant type is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByTypeShouldNotBeEmpty()
        {
            var result = await _plantService.GetPlantByTypePlant(TypesPlants.Vegetable);

            Assert.NotEmpty(result);
        }

        /// <summary>
        /// Test case to verify that the GetPlantByTypePlant method returns an empty list of plants when an invalid plant type is provided
        /// </summary>
        [Fact]
        public async void GetPlantsByTypeShouldBeEmpty()
        {
            var result = await _plantService.GetPlantByTypePlant(TypesPlants.Medicinal);

            Assert.Empty(result);
        }

        /// <summary>
        /// Test case to verify that the AddPlantAsync method returns a non-null plant when a valid plant is added
        /// </summary>
        [Fact]
        public async void addPlantShouldNotBeNull()
        {
            var plant = new PlantRequestDto
            {
                AdminID = 2,
                Name = "Plant7",
                Type = TypesPlants.Fruit,
                Description = "Description",
                ExpSuggested = ExperienceLevels.intermediate,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Low,
                PlantImage = null
            };

            var result = await _plantService.AddPlantAsync(plant);

            Assert.NotNull(result);
        }

        /// <summary>
        /// Test case to verify that the UpdatePlantAsync method updates the plant successfully when a valid plant ID and updated plant data are provided
        /// </summary>
        [Fact]
        public async void updatePlantShouldNotBeNull()
        {
            var plantIdToUpdate = 1;

            var plantDTO = new PlantRequestDto
            {
                Name = "UpdatedPlant7",
                Type = TypesPlants.Fruit,
                ExpSuggested = ExperienceLevels.intermediate,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Low,
                PlantImage = null
            };

            var isUpdated = await _plantService.UpdatePlantAsync(plantIdToUpdate, plantDTO);
            var updatePlant = await _plantService.GetPlantByIdAsync(plantIdToUpdate);


            Assert.True(isUpdated);
            Assert.Equal("UpdatedPlant7", updatePlant.Name);
        }

        /// <summary>
        /// Test case to verify that the UpdatePlantAsync method returns false when an invalid plant ID is provided
        /// </summary>
        [Fact]
        public async void updatePlantShouldBeNull()
        {
            var plantIdToUpdate = 100;

            var plantDTO = new PlantRequestDto
            {
                Name = "UpdatedPlant7",
                Type = TypesPlants.Fruit,
                ExpSuggested = ExperienceLevels.intermediate,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Low,
                PlantImage = null
            };

            var isUpdated = await _plantService.UpdatePlantAsync(plantIdToUpdate, plantDTO);

            Assert.False(isUpdated);
        }

        /// <summary>
        /// Test case to verify that the DeletePlantAsync method deletes the plant successfully when a valid plant ID is provided
        /// </summary>
        [Fact]
        public async void deletePlantShouldBeTrue()
        {
            var plantIdToDelete = 6;
            var isDeleted = await _plantService.DeletePlantAsync(plantIdToDelete);

            Assert.True(isDeleted);
        }

        /// <summary>
        /// Test case to verify that the DeletePlantAsync method returns false when an invalid plant ID is provided
        /// </summary>
        [Fact]
        public async void deletePlantShouldBeFalse()
        {
            var plantIdToDelete = 100;
            var isDeleted = await _plantService.DeletePlantAsync(plantIdToDelete);

            Assert.False(isDeleted); // Não existe planta com esse id
        }


    }
}
