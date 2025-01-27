using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Entity.enums;
using WebAPI.Services;

namespace WebAPITests.controllers
{
    public  class PlantControllerTests :IDisposable
    {
        private readonly WebApiContext _webApiContext;
        private readonly DataContext _context;
        private readonly PlantService _plantService;
        private readonly PlantController _controller;

        /// <summary>
        /// Assigns the context and configuration to the PlantControllerTests class
        /// </summary>
        public PlantControllerTests() 
        {
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _plantService = new PlantService(_context);
            _controller = new PlantController(_plantService);
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
        /// Tests the GetAllPlants method to ensure it returns an OK result with a list of plants
        /// </summary>
        [Fact]
        public async void GetAllPlants_ReturnsOkResult_WithListOfPlants()
        {
            // Act
            var result = await _controller.GetAllPlants();

            // Assert
            Assert.IsType<ActionResult<List<PlantResponseDto>>>(result);
        }

        /// <summary>
        /// Tests the GetPlantById method to ensure it returns an OK result with the expected plant
        /// </summary>
        [Fact]
        public async void GetPlantById_ReturnsOkResult_WithPlant()
        {
            var expectedId = 1;
            // Act
            var result = await _controller.GetPlant(expectedId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var plant = Assert.IsType<PlantResponseDto>(okResult.Value);
            Assert.Equal(expectedId, plant.Id);
        }

        /// <summary>
        /// Tests the GetPlantById method to ensure it returns a NotFound result when the plant is not found
        /// </summary>
        [Fact]
        public async void GetPlantById_ReturnsNotFoundResult_WithPlant()
        {
            var expectedId = 100;
            // Act
            var result = await _controller.GetPlant(expectedId);

            // Assert
            Assert.IsType<NotFoundObjectResult>(result.Result);
        }

        /// <summary>
        /// Tests the CreatePlant method to ensure it returns a Created result with the newly created plant
        /// </summary>
        [Fact]
        public async void CreatePlant_ReturnsOkResult_WithPlant()
        {
            // Arrange
            var plant = new PlantRequestDto
            {
                AdminID = 2,
                Name = "Plant1",
                Description = "Description",
                Type = TypesPlants.Flower,
                ExpSuggested = ExperienceLevels.Expert,
                WaterNeeds = WaterLevels.Medium,
                LuminosityNeeded = LightLevel.High,
                PlantImage = null // Se estiver a testar sem imagem
            };
            
            // Act
            var result = await _controller.AddPlant(plant);

            // Assert
            var createdAtActionResult = Assert.IsType<CreatedAtActionResult>(result.Result);
            var createdPlant = Assert.IsAssignableFrom<PlantResponseDto>(createdAtActionResult.Value);
            Assert.Equal(plant.Name, createdPlant.Name);
            Assert.Equal(plant.Type, createdPlant.Type);
            Assert.Equal(plant.ExpSuggested, createdPlant.ExpSuggested);
            Assert.Equal(plant.WaterNeeds, createdPlant.WaterNeeds);
            Assert.Equal(plant.LuminosityNeeded, createdPlant.LuminosityNeeded);
            //Assert.Equal(plant.PlantImage, createdPlant.PlantImage);
        }

        /// <summary>
        /// Tests the CreatePlant method to ensure it returns a BadRequest result when the input is invalid
        /// </summary>
        [Fact]
        public async void CreatePlant_ReturnsBadRequest_WithMessage()
        {
            // Act
            var result = await _controller.AddPlant(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid data.", errorResponse.error);
        }

        /// <summary>
        /// Tests the UpdatePlant method to ensure it returns an OK result with a successful update
        /// </summary>
        [Fact]
        public async void UpdatePlant_ReturnsOkResult_WithPlant()
        {
            var plantId = 1;
            var updatedPlant = new PlantRequestDto
            {
                AdminID = 2,
                Name = "Plant1",
                Type = TypesPlants.Medicinal,
                Description = "NewDescription",
                ExpSuggested = ExperienceLevels.Expert,
                WaterNeeds = WaterLevels.Medium,
                LuminosityNeeded = LightLevel.High,
                PlantImage = null
            };

            var resultUpdate = await _controller.UpdatePlant(plantId, updatedPlant);

            //// Assert
            //var okResult = Assert.IsType<OkObjectResult>(resultUpdate.Result);
            //var isUpdateSuccess = Assert.IsType<bool>(okResult.Value);
            //Assert.True(isUpdateSuccess);
            // Assert
            var okResult = Assert.IsType<OkObjectResult>(resultUpdate.Result);
            var plantResponse = Assert.IsType<PlantResponseDto>(okResult.Value);

            // Verificações adicionais nos detalhes da planta atualizada
            Assert.Equal(plantId, plantResponse.Id);
            Assert.Equal(updatedPlant.Name, plantResponse.Name);
            Assert.Equal(updatedPlant.Type, plantResponse.Type);
            Assert.Equal(updatedPlant.ExpSuggested, plantResponse.ExpSuggested);
            Assert.Equal(updatedPlant.WaterNeeds, plantResponse.WaterNeeds);
            Assert.Equal(updatedPlant.LuminosityNeeded, plantResponse.LuminosityNeeded);
        }

        /// <summary>
        /// Tests the UpdatePlant method to ensure it returns a BadRequest result when the input is invalid
        /// </summary>
        [Fact]
        public async void UpdatePlant_ReturnsBadRequest_WithMessage()
        {
            // Act
            var result = await _controller.UpdatePlant(1, null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid data.", errorResponse.error);
        }

        /// <summary>
        /// Tests the DeletePlant method to ensure it returns a NoContent result when the plant is successfully deleted
        /// </summary>
        [Fact]
        public async void DeletePlant_ReturnsNoContentResult_WhenPlantDeleted()
        {
            var deletedId = 6;
           
            var resultDelete = await _controller.DeletePlant(deletedId);

            // Assert
            Assert.IsType<NoContentResult>(resultDelete);
        }

        /// <summary>
        /// Tests the DeletePlant method to ensure it returns a NotFound result when the plant is not found
        /// </summary>
        [Fact]
        public async void DeletePlant_ReturnsNotFoundResult_WhenPlantNotFound()
        {
            var deletedId = 100;

            var resultDelete = await _controller.DeletePlant(deletedId);

            // Assert
            Assert.IsType<NotFoundObjectResult>(resultDelete);
        }
    }
}
