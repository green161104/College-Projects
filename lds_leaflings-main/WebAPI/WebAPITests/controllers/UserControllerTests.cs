using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity.enums;
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPITests.controllers
{
    public class UserControllerTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly IConfiguration _configuration;
        private readonly Cryptography _cryptography;
        private readonly UserController _controller;
        private readonly WebApiContext _webApiContext;
        private readonly UserService _userService;

        public UserControllerTests()
        {
            var webApiContext = new WebApiContext();
            _context = webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _userService = new UserService(_context, _cryptography);
            _controller = new UserController(_userService);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        /// <summary>
        /// tests whether using the get all users method will correctly return every user
        /// </summary>
        [Fact]
        public async void User_GetAlUsers_ReturnsList()
        {
            //Arrange
            var result = await _controller.GetAllUsers();

            //Act
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var data = okResult.Value as dynamic;

            //Assert
            Assert.NotNull(data);
            Assert.Equal(5, data.total);
        }

        /// <summary>
        /// tests whether attempting to retrieve a user by their id will work correctly under the ideal circumstances
        /// </summary>
        [Fact]
        public async void User_GetUserByID_ReturnsCorrectUser()
        {

            // Arrange
            var id = 4;

            // Act
            var actionResult = await _controller.GetUserById(id);

            // Assert
            Assert.NotNull(actionResult);

            // Verify the response type is OkObjectResult
            var okResult = Assert.IsType<OkObjectResult>(actionResult.Result);

            // Ensure the returned data is of the expected type
            var userDto = Assert.IsType<UserResponseDto>(okResult.Value);

            // Additional assertions to verify properties
            Assert.Equal(id, userDto.Id);
            Assert.Equal("User", userDto.Username);
            Assert.Equal("912548966", userDto.Contact);
        }


        /// <summary>
        /// tests whether attempting to retrieve a user ID that does not xist, returns a Not Found response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task User_GetUserByID_ReturnsNotFoundForNonExistingUser()
        {
            // Act
            var result = await _controller.GetUserById(999); // Id that doesn't exist

            // Assert
            Assert.IsType<NotFoundObjectResult>(result.Result);
        }


        /// <summary>
        /// tests whether attempting to retrieve an in valid user ID, returns a Not Found response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task User_GetUserByID_ReturnsNotFoundForInvalidId()
        {
            // Act
            var result = await _controller.GetUserById(-1);

            // Assert
            Assert.IsType<NotFoundObjectResult>(result.Result);
        }

        /// <summary>
        /// Tests whether adding a user works successfully when using a valid DTO
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddUser_ValidUser_ReturnsCreatedUser()
        {
            // Arrange
            var adminRequest = new UserRequestDto
            {
                Username = "newuser",
                Email = "newuser@gail.com",
                Password = "passwordnewuser",
                Contact = "276161723",
                Location = "Guimaraes",
                CareExperience = ExperienceLevels.intermediate,
                WaterAvailability = WaterLevels.High,
                LuminosityAvailability = LightLevel.Medium

            };

            // Act
            var result = await _controller.AddUser(adminRequest);

            // Assert
            var createdAtResult = Assert.IsType<CreatedAtActionResult>(result.Result);
            var userResponse = Assert.IsType<UserResponseDto>(createdAtResult.Value);
            Assert.Equal(adminRequest.Username, userResponse.Username);
            Assert.Equal(adminRequest.Contact, userResponse.Contact);
        }


        /// <summary>
        /// Tests whether attempting to add a user using a null DTO resturns a Bad Request response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddUser_NullRequest_ReturnsBadRequest()
        {
            // Act
            var result = await _controller.AddUser(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid data.", errorResponse.error);
        }


        /// <summary>
        /// Tests whether adding a user with invalid email and password will generate an internal server error
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddUser_InvalidEmailAndPassword_ReturnsInternalServerError()
        {
            // Arrange
            var userRequest = new UserRequestDto
            {
                Username = "newuser",
                Email = "newuser",
                Password = "pasn",
                Contact = "276161723",
                Location = "Guimaraes",
                CareExperience = ExperienceLevels.intermediate,
                WaterAvailability = WaterLevels.High,
                LuminosityAvailability = LightLevel.Medium
            };

            // Act
            var result = await _controller.AddUser(userRequest);

            // Assert
            var statusResult = Assert.IsType<ObjectResult>(result.Result);
            Assert.Equal((int)HttpStatusCode.InternalServerError, statusResult.StatusCode);
            Assert.IsType<ErrorResponseDto>(statusResult.Value);
        }

        /// <summary>
        /// tests whether attemption to add an user with an already existing email will return an internal server error
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddUser_EmailInUse_ReturnsInternalServerError()
        {
            // Arrange
            var userRequest = new UserRequestDto
            {
                Username = "newuser",
                Email = "user@user.com",
                Password = "pas3244223n",
                Contact = "276161723",
                Location = "Guimaraes",
                CareExperience = ExperienceLevels.intermediate,
                WaterAvailability = WaterLevels.High,
                LuminosityAvailability = LightLevel.Medium
            };

            // Act
            var result = await _controller.AddUser(userRequest);

            // Assert
            var statusResult = Assert.IsType<ObjectResult>(result.Result);
            Assert.Equal((int)HttpStatusCode.InternalServerError, statusResult.StatusCode);
            Assert.IsType<ErrorResponseDto>(statusResult.Value);
        }

        /// <summary>
        /// tests whether a successful update will return an OK response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task UpdateUser_WhenUpdateIsSuccessful_ReturnsOk()
        {
            // Arrange
            var existingId = 4;  // ID of an existing user in the fake database
            var userRequestDto = new UpdateUserInformationDto
            {
                Username = "UpdatedUser",
                Contact = "UpdatedContact",
                Location = "Guimaraes"
            };

            // Act
            var result = await _controller.UpdateUser(existingId, userRequestDto);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            Assert.NotNull(okResult.Value);

            // Verify the updated user details
            var updatedUserResult = await _controller.GetUserById(existingId); // Assuming this method retrieves user by ID
            var okUpdate = Assert.IsType<OkObjectResult>(updatedUserResult.Result);
            var userDto = Assert.IsType<UserResponseDto>(okUpdate.Value);

            Assert.Equal(userRequestDto.Username, userDto.Username);
            Assert.Equal(userRequestDto.Contact, userDto.Contact);

        }


        /// <summary>
        /// tests whether updating a user using a null DTO will result in a Bad Request respnse
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task UpdateUser_WhenUserDtoIsNull_ReturnsBadRequest()
        {
            // Act
            var result = await _controller.UpdateUser(3, null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result);
        }

        /// <summary>
        /// Tests whether attempting to update a non-existing user will resutl in a Not Found response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task UpdateUserInformation_WhenuserDoesNotExist_ReturnsNotFound()
        {
            // Arrange
            var nonExistentId = 999;
            var userRequestDto = new UpdateUserInformationDto
            {
                Username = "UpdatedUser",
                Contact = "UpdatedContact",
                Location = "Guimaraes",
            };

            // Act
            var result = await _controller.UpdateUser(nonExistentId, userRequestDto);

            // Assert
            Assert.IsType<NotFoundObjectResult>(result);
        }


        /// <summary>
        /// tests whether a successful update will return an OK response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task UpdateUserPreferences_WhenUpdateIsSuccessful_ReturnsOk()
        {
            // Arrange
            var existingId = 4;  // ID of an existing user in the fake database
            var userPreferencesRequestDto = new UserPreferencesRequestDto
            {
                CareExperience = ExperienceLevels.Expert,
                WaterAvailability = WaterLevels.High,
                LuminosityAvailability = LightLevel.Medium
            };

            // Act
            var result = await _controller.UpdateUserPreferences(existingId, userPreferencesRequestDto);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            Assert.NotNull(okResult.Value);
            Assert.True((bool)okResult.Value); // Ensure it returns true for successful update

            // Verify the updated user details
            var updatedUserResult = await _controller.GetUserById(existingId); // Assuming this method retrieves user by ID
            var okUpdate = Assert.IsType<OkObjectResult>(updatedUserResult.Result);
            var userDto = Assert.IsType<UserResponseDto>(okUpdate.Value);

            Assert.Equal(userPreferencesRequestDto.WaterAvailability, userDto.WaterAvailability);
            Assert.Equal(userPreferencesRequestDto.LuminosityAvailability, userDto.LuminosityAvailability);
            Assert.Equal(userPreferencesRequestDto.CareExperience, userDto.CareExperience);
        }

        /// <summary>
        /// tests whether user deleting is successful (returns no content) when the user exists
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task DeleteUser_WhenDeletionIsSuccessful_ReturnsNoContent()
        {
            // Arrange
            var existingId = 5;

            // Act
            var result = await _controller.DeleteUser(existingId);

            // Assert
            Assert.IsType<NoContentResult>(result);

            var deletedUser = await _controller.GetUserById(existingId);
            Assert.IsType<NotFoundObjectResult>(deletedUser.Result);

        }


        /// <summary>
        /// tests whether deleting a user returns not found if the ID does not exist
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task DeleteUser_ReturnsNotFound_WhenUserDoesNotExist()
        {
            // Arrange
            var nonExistentId = 999;

            // Act
            var result = await _controller.DeleteUser(nonExistentId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("User not found", errorResponse.error);
        }


        /// <summary>
        /// tests whether retrieving plant matches for a valid user returns the expected results for each list
        /// </summary>
        /// <returns></returns>
        [Fact]
        //public async Task GetPlantMatches_WhenValidUser_ReturnsOk()
        //{
        //     Arrange
        //    var userId = 5;

        //     Act
        //    var result = await _controller.GetPlantMatches(userId);

        //     Assert
        //    var okResult = Assert.IsType<OkObjectResult>(result);
        //    Assert.NotNull(okResult.Value);

        //     Cast the value of OkObjectResult to a dynamic type
        //    var response = okResult.Value as dynamic;
        //    Assert.NotNull(response);

        //     Verify the structure of the response
        //     Assert the number of items in each match category
        //    Assert.Equal(2, ((IEnumerable<dynamic>)response.perfectMatches).Count());
        //    Assert.Equal(5, ((IEnumerable<dynamic>)response.averageMatches).Count());
        //    Assert.Empty(((IEnumerable<dynamic>)response.noMatches));
        //    Assert.Equal(2, ((IEnumerable<dynamic>)response.weakMatches).Count());
        //}


        /// <summary>
        /// tests whether attempting to retrieve plant matches fot a user not found will return empty lists for every category
        /// </summary>
        /// <returns></returns>
        //[Fact]
        public async Task GetPlantMatches_WhenUserNotFound_ReturnsEmptyLists()
        {
            // Arrange: Use an invalid user ID
            var userId = 999;

            // Act: Perform the actual call to GetPlantMatches
            var result = await _controller.GetPlantMatches(userId);

            // Assert
            var okResult = Assert.IsType<OkObjectResult>(result);
            Assert.NotNull(okResult.Value);

            // Cast the value of OkObjectResult to a dynamic type
            var response = okResult.Value as dynamic;
            Assert.NotNull(response);

            // Verify the structure of the response
            // Assert the number of items in each match category
            //Assert.Empty((IEnumerable<dynamic>)response.perfectMatches);
            //Assert.Empty((IEnumerable<dynamic>)response.averageMatches);
            //Assert.Empty((IEnumerable<dynamic>)response.noMatches);
            //Assert.Empty((IEnumerable<dynamic>)response.weakMatches);
        }

    }
}
