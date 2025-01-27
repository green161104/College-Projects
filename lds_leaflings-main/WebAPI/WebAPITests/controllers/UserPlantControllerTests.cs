using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
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
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPITests.controllers
{
    public class UserPlantControllerTests : IDisposable
    {

        private readonly DataContext _context;
        private readonly IConfiguration _configuration;
        private readonly Cryptography _cryptography;
        private readonly AuthController _controller;
        private readonly UserPlantsController _userPlantController;
        private readonly WebApiContext _webApiContext;

        public UserPlantControllerTests()
        {
            _webApiContext = new WebApiContext();
            _context = _webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _controller = new AuthController(_context, _configuration, _cryptography);
            _userPlantController = new UserPlantsController(_context, _cryptography);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        [Fact]
        public async void GetAllPlantsUsers_ReturnsOkResult()
        {
            // Act
            var result = await _userPlantController.GetAllPlantsUsers();

            // Assert
            Assert.IsType<ActionResult<List<UserPlant>>>(result);
        }

        [Fact]
        public async void GetPlantsByUser_ReturnsOkResult()
        {
            // Act
            var id = 4;
            var result = await _userPlantController.GetPlantsByUser(id);

            // Assert
            Assert.IsType<ActionResult<List<UserPlantResponseDto>>>(result);
        }

        [Fact]
        public async void GetPlantsByUser_ReturnsNotFound()
        {
            var id = 9999;
            var result = await _userPlantController.GetPlantsByUser(id);


            // Assert
            var notFoundResult = Assert.IsType<NoContentResult>(result.Result); // result is a notfoundobjectresult
        }

        [Fact]
        public async void GetUsersByPlant_ReturnsOk_WhenUsersFound()
        {
            // Arrange
            int plantId = 1; // Ensure this ID has users associated in your populated database

            // Act
            var result = await _userPlantController.GetUsersByPlant(plantId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var returnValue = Assert.IsAssignableFrom<List<UserPlantRequestDto>>(okResult.Value);

            // Verify that the number of users returned matches the expected count
            Assert.Equal(2, returnValue.Count); 
           // Assert.All(returnValue, up => Assert.Equal(plantId, up.PlantId));
        }


        /**
         *  public int PlantId { get; set; }
         * public int UserId { get; set; }
         * //public DateTime DateAcquired { get; set; }  
        */
       


        [Fact]
        public async void AddUserPlant_ReturnsOk_WhenAdded()
        {
            //Arrange
            UserPlantRequestDto userPlant = new UserPlantRequestDto() { PlantId = 3, UserId = 6 };

            //Act 
            var result = await _userPlantController.AddUserPlant(userPlant);

            //Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var returnValue = Assert.IsAssignableFrom<UserPlant>(okResult.Value);
        }

        [Fact]
        public async void AddUserPlant_ReturnsBadRequest_WhenAdded()
        {
            //Arrange
            UserPlantRequestDto userPlant = new UserPlantRequestDto() { PlantId = 97678, UserId = -2342 };

            //Act 
            var result = await _userPlantController.AddUserPlant(userPlant);

            //Assert
            var badResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badResult.Value);
            Assert.Equal("Error while adding plant to user", errorResponse.error);
        }

        [Fact]
        public async void DeleteUserPlant_ReturnsNoContent_WhenDeleted()
        {
            int userId = 4;
            int plantId = 3;

            //Act 
            var result = await _userPlantController.DeleteUserPlant(userId,plantId);

            //Assert
            var okResult = Assert.IsType<NoContentResult>(result);
            var returnValue = Assert.IsAssignableFrom<NoContentResult>(okResult);
        }

        [Fact]
        public async void DeleteUserPlant_ReturnsNotFound_WhenDeletedInfoInvalid()
        {
            //Act 
            var result = await _userPlantController.DeleteUserPlant(23423, 3);

            //Assert
            var okResult = Assert.IsType<NotFoundResult>(result);
        }

      

    }
}

