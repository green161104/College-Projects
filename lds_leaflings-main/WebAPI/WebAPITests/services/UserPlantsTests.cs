using Microsoft.AspNetCore.Components.Sections;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPITests.services
{
    public class UserPlantsTests : IDisposable
    {

        private readonly DataContext _context;
        private readonly UserPlantsService _userPlantsService;
        private readonly UserService _userService;
        private readonly Cryptography _cryptography;
        private readonly IConfiguration _configuration;


        /** TO TEST 
         *  GetAllUserPlantsAsync - does it return valid objects (?) - done
         * GetPlantsByUserIdAsync - does it return valid objects for valid data (?) (yes), does it return empty lists when it should (?) ( yes )  , what happens when I give it an ID that is not valid(?) ( it returns null ) - done 
         * GetUsersByPlantIdAsync - does it return users associated with the plantID given(?) ( yes ) , what about with invalid IDS (?) ( returns an empty list ) , what about when no one owns a particular plant(?) ( in this case, a userplant object can't exist so it doesn't make sense ) - done  
         * AddUserPlantAsync - does it add correctly(yes!), when I add a 4th plant to a free user what happens( null ), what about for a paid user(success), what if I try to add a plant that doesn't exist(can't do it cause there's no such plant object to instantiate a userplant.) - done
         * DeleteUserPlantAsync - valid/invalid IDs - done 
         * CountUserPlantsAsync - valid result, returns zero for users with zero plants, throws exception for invalid IDS ( see user tests and services ) - done
         */
        public UserPlantsTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _userPlantsService = new UserPlantsService(_context);
            _userService = new UserService(_context, _cryptography);
        }
        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }



        [Fact]
        public async void GetAllUserPlantsAsyncShouldNotBeEmpty() // isn't empty
        {
            var userplants = await _userPlantsService.GetAllUserPlantsAsync();

            Assert.NotEmpty(userplants);
        }



        [Fact]
        public async void GetPlantsByUserIdAsyncShouldBeNullForInvalidID()
        {
            var expectedUserPlantID = 100; // this is is invalid, so it should 
            var userPlant = await _userPlantsService.GetPlantsByUserIdAsync(expectedUserPlantID);

            Assert.Null(userPlant); // return null
        }

        [Fact]
        public async void GetPlantsByUserIdAsyncShouldNotBeEmptyForValidID()
        {
            var expectedUserPlantID = 4; // this is is valid, so it  shouldn't be null
            var userPlant = await _userPlantsService.GetPlantsByUserIdAsync(expectedUserPlantID);

            Assert.NotEmpty(userPlant); // return the list of plants
        }

        [Fact]
        public async void GetPlantsByUserIdAsyncShouldBeEmptyForUserWithNoPlants()
        {
            var expectedUserPlantID = 6; // this is is valid, so it  shouldn't be null
            var userPlant = await _userPlantsService.GetPlantsByUserIdAsync(expectedUserPlantID);

            Assert.Empty(userPlant); // return the list of plants
        }

        [Fact]
        public async void GetUsersByPlantIdAsyncShouldNotBeNull()
        {
            var expectedUserPlantID = 1;
            var userPlant = await _userPlantsService.GetUsersByPlantIdAsync(expectedUserPlantID);

            Assert.NotNull(userPlant);
        }


        [Fact]
        public async void GetUsersByPlantIdAsyncShoulBeEmptyForInvalidID()  // for invalid userplant ID
        {
            var expectedUserPlantID = 25677;
            var userPlant = await _userPlantsService.GetUsersByPlantIdAsync(expectedUserPlantID);
             
            Assert.Empty(userPlant); // a task devolve um vazio, cause no one owns it
        }


        [Fact]
        public async void AddUserPlantShouldBeSuccessfullForValidInfo()
        {
            var UserPlantToAdd = new UserPlantRequestDto
            {
                PlantId = 3,
                UserId = 6,
            };
                
            var AddedUP = await _userPlantsService.AddUserPlantAsync(UserPlantToAdd);

            Assert.Equal(UserPlantToAdd.PlantId, AddedUP.PlantID);
            Assert.Equal(UserPlantToAdd.UserId, AddedUP.PersonID);
        }

        [Fact]
        public async void AddUserPlantShouldBeUnuccessfullForInvalidInfo()
        {
            var UserPlantToAdd = new UserPlantRequestDto
            {
                PlantId = 9999,
                UserId = -5,
            };

            var AddedUP = await _userPlantsService.AddUserPlantAsync(UserPlantToAdd);

            Assert.Null(AddedUP); 

        }

        [Fact]
        public async void AddUserPlantShouldBeUnsuccessfullForFreeUserWith3Plants() // working
        {
            var UserPlantToAdd = new UserPlantRequestDto
            {
                PlantId = 1,
                UserId = 3,
            };

            var AddedUP = await _userPlantsService.AddUserPlantAsync(UserPlantToAdd);

            Assert.Null(AddedUP);

        }

        [Fact]
        public async void AddUserPlantShouldBeSuccessfullForPaidUserWith3_OrMorePlants() // working
        {
            var UserPlantToAdd = new UserPlantRequestDto
            {
                PlantId = 2,
                UserId = 6
            };

            var AddedUP = await _userPlantsService.AddUserPlantAsync(UserPlantToAdd);

            Assert.NotNull(AddedUP);
        }


        [Fact]

        public async void CountUserPLantsShouldBeCorrect() // working
        {
            int userId = 5;

            var user = await _userService.GetUserByIdAsync(userId);
            var result  = await _userPlantsService.CountUserPlantsAsync(user);
            
            Assert.Equal(4, result);

        }

        [Fact]

        public async void CountUserPLantsShouldBeZeroForUserWithNoPlants() // working
        {
            int userId = 6;

            var user = await _userService.GetUserByIdAsync(userId);
            var result = await _userPlantsService.CountUserPlantsAsync(user);

            Assert.Equal(0,result);
        }


        [Fact]
        public async void DeleteUserPlantShouldWorkForValidData()
        {
            int Userid = 4;
            int plantId = 2; 
            var user = await _userService.GetUserByIdAsync(Userid);
            
            var result = _userPlantsService.DeleteUserPlantAsync(Userid, plantId);
          
            Assert.True(await result);
        }

        [Fact]
        public async void DeleteUserPlantShouldNotWorkForInValidData()
        {
            int Userid = 4;
            int plantId = 200;
            var user = await _userService.GetUserByIdAsync(Userid);

            var result = _userPlantsService.DeleteUserPlantAsync(Userid, plantId);

            Assert.False(await result);
        }

    }
}
