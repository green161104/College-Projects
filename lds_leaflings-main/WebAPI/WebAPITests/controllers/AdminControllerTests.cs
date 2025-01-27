using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
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
    public class AdminControllerTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly IConfiguration _configuration;
        private readonly Cryptography _cryptography;
        private readonly AdminController _controller;
        private readonly WebApiContext _webApiContext;
        private readonly AdminService _adminService;
        public AdminControllerTests() {

            var webApiContext = new WebApiContext();
            _context = webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _adminService = new AdminService(_context, _cryptography);
            _controller = new AdminController(_adminService);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }



        /// <summary>
        /// Tests wether get all method corretly returns all admins
        /// </summary>
        [Fact]
        public async void Admin_GetAllAdmins_ReturnsList()
        {
            //Arrange
            var result = await _controller.GetAllAdmins();

            //Act
            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            var data = okResult.Value as dynamic;

            //Assert
            Assert.NotNull(data);
            Assert.Equal(3, data.total); 
        }


        /// <summary>
        /// Tests whether getting an admin by ID worlks as expected when used correctly
        /// </summary>
        [Fact]
        public async void Admin_GetAdminByID_ReturnsCorrectAdmin()
        {

            // Arrange
            var id = 2; 

            // Act
            var actionResult = await _controller.GetAdminById(id);

            // Assert
            Assert.NotNull(actionResult);

            // Verify the response type is OkObjectResult
            var okResult = Assert.IsType<OkObjectResult>(actionResult.Result);

            // Ensure the returned data is of the expected type
            var adminDto = Assert.IsType<AdminResponseDto>(okResult.Value);

            // Additional assertions to verify properties
            Assert.Equal(id, adminDto.Id);
            Assert.Equal("Admin", adminDto.Username); 
            Assert.Equal("937929222", adminDto.Contact);   
        }


        /// <summary>
        /// tests whether getting an admin by an id that does not exists will return Not Found
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task Admin_GetAdminByID_ReturnsNotFoundForNonExistingAdmin()
        {
            // Act
            var result = await _controller.GetAdminById(999); // Id that doesn't exist

            // Assert
            Assert.IsType<NotFoundObjectResult>(result.Result);
        }


        /// <summary>
        /// tests whether getting an admin by an invalid id will return Not Found
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task Admin_GetAdminByID_ReturnsNotFoundForInvalidId()
        {
            // Act
            var result = await _controller.GetAdminById(-1); 

            // Assert
            Assert.IsType<NotFoundObjectResult>(result.Result);
        }


        /// <summary>
        /// Tests whether adding an admin will work correctly when used with a valid DTO
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddAdmin_ValidAdmin_ReturnsCreatedAdmin()
        {
            // Arrange
            var adminRequest = new AdminRequestDto
            {
                Username = "newadmin",
                Email = "newadmin@gail.com",
                Password = "passwordadmin",
                Contact = "newadmin@example.com"
            };

            // Act
            var result = await _controller.AddAdmin(adminRequest);

            // Assert
            var createdAtResult = Assert.IsType<CreatedAtActionResult>(result.Result);
            var adminResponse = Assert.IsType<AdminResponseDto>(createdAtResult.Value);
            Assert.Equal(adminRequest.Username, adminResponse.Username);
            Assert.Equal(adminRequest.Contact, adminResponse.Contact);
        }


        /// <summary>
        /// Tests whether using a null admin request will generate a bad request response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddAdmin_NullRequest_ReturnsBadRequest()
        {
            // Act
            var result = await _controller.AddAdmin(null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid data.", errorResponse.error);
        }



        /// <summary>
        /// Tests whether sending a DTO containing an invalid password and email will generate an internal server error
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddAdmin_InvalidEmailAndPassword_ReturnsInternalServerError()
        {
            // Arrange
            var adminRequest = new AdminRequestDto
            {
                Username = "newadmin",
                Email = "newadmin",
                Password = "pasn",
                Contact = "newadmin@example.com"
            };

            // Act
            var result = await _controller.AddAdmin(adminRequest);

            // Assert
            var statusResult = Assert.IsType<ObjectResult>(result.Result);
            Assert.Equal((int)HttpStatusCode.InternalServerError, statusResult.StatusCode);
            Assert.IsType<ErrorResponseDto>(statusResult.Value);
        }


        /// <summary>
        /// Tests whether sending a DTO containing an email already in use will generate an internal server error
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task AddAdmin_EmailInUse_ReturnsInternalServerError()
        {
            // Arrange
            var adminRequest = new AdminRequestDto
            {
                Username = "newadmin",
                Email = "admin@admin.com",
                Password = "pas3244223n",
                Contact = "276161723"
            };

            // Act
            var result = await _controller.AddAdmin(adminRequest);

            // Assert
            var statusResult = Assert.IsType<ObjectResult>(result.Result);
            Assert.Equal((int)HttpStatusCode.InternalServerError, statusResult.StatusCode);
            Assert.IsType<ErrorResponseDto>(statusResult.Value);
        }


        /// <summary>
        /// Tests whether updates are successful under the ideal conditions
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task UpdateAdmin_WhenUpdateIsSuccessful_ReturnsOk()
        {
            // Arrange
            var existingId = 1;  // ID of an existing admin in the fake database
            var adminDto = new UpdateAdminDto { Username = "UpdatedAdmin", Contact = "UpdatedContact" };

            // Act
            var result = await _controller.UpdateAdmin(existingId, adminDto);

            // Assert
            var updatedUserResult = await _controller.GetAdminById(existingId); // Assuming this method retrieves user by ID
            var okUpdate = Assert.IsType<OkObjectResult>(updatedUserResult.Result);
            var adminDtoUpdate = Assert.IsType<AdminResponseDto>(okUpdate.Value);

            Assert.Equal(adminDto.Username, adminDtoUpdate.Username);
            Assert.Equal(adminDto.Contact, adminDtoUpdate.Contact);

            var okResult = Assert.IsType<OkObjectResult>(result.Result);
            Assert.NotNull(okResult.Value);
        }


        /// <summary>
        /// Tests wheter using a null DTO to update an admin will generate a bad request response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task UpdateAdmin_WhenAdminDtoIsNull_ReturnsBadRequest()
        {
            // Act
            var result = await _controller.UpdateAdmin(1, null);

            // Assert
            var badRequestResult = Assert.IsType<BadRequestObjectResult>(result.Result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(badRequestResult.Value);
            Assert.Equal("Invalid data.", errorResponse.error);
        }


        /// <summary>
        /// Tests wheter attempting to update an admin that does not exist generates a not found resposnse
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task UpdateAdmin_WhenAdminDoesNotExist_ReturnsNotFound()
        {
            // Arrange
            var nonExistentId = 999;
            var adminDto = new UpdateAdminDto { Username = "TestAdmin", Contact = "TestContact" };

            // Act
            var result = await _controller.UpdateAdmin(nonExistentId, adminDto);

            // Assert
            Assert.IsType<NotFoundResult>(result.Result);
        }


        /// <summary>
        /// testing wheter deleting an admin will result in success under correct conditions
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task DeleteAdmin_WhenDeletionIsSuccessful_ReturnsNoContent()
        {
            // Arrange
            var existingId = 2; 

            // Act
            var result = await _controller.DeleteAdmin(existingId);

            // Assert
            Assert.IsType<NoContentResult>(result);

            var deletedAdmin = await _controller.GetAdminById(existingId);
            Assert.IsType<NotFoundObjectResult>(deletedAdmin.Result);

        }

        /// <summary>
        /// tests whether attempting to delete a non-existing admin will generate a not found response
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task DeleteAdmin_ReturnsNotFound_WhenAdminDoesNotExist()
        {
            // Arrange
            var nonExistentId = 999;

            // Act
            var result = await _controller.DeleteAdmin(nonExistentId);

            // Assert
            var notFoundResult = Assert.IsType<NotFoundObjectResult>(result);
            var errorResponse = Assert.IsType<ErrorResponseDto>(notFoundResult.Value);
            Assert.Equal("Admin not found", errorResponse.error);
        }

    }
}
