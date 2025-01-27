using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Data;
using WebAPI.Entity;
using WebAPI.Entity.enums;
using WebAPI.Utils;

namespace WebAPITests
{
    internal class WebApiContext
    {

        private readonly IConfiguration _configuration;
        private readonly Cryptography _cryptography;

        public WebApiContext()
        {
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
        }

        public DataContext getWebApiContext()
        {
            var options = new DbContextOptionsBuilder<DataContext>()
                .UseInMemoryDatabase(databaseName: Guid.NewGuid().ToString())
                .Options;
            var databaseContext = new DataContext(options, _cryptography);
            databaseContext.Database.EnsureCreated();
            //if(await databaseContext)
            if (databaseContext == null)
            {
                throw new Exception("Database context is null.");
            }

            // populate temporary database
            populateAdmins(databaseContext);
            populatePlants(databaseContext);
            populateUsers(databaseContext);
            populateAds(databaseContext);
            populateTasks(databaseContext);
            populateUserPlants(databaseContext);
            populatePayments(databaseContext);
            populateWarnings(databaseContext);
            populateDiaries(databaseContext);
            populateLogs (databaseContext);

            return databaseContext;
        }

        private async void populateAdmins(DataContext context)
        {
            context.Admin.Add(new WebAPI.Entity.Admin
            {
                Id = 2,
                Username = "Admin",
                Email = _cryptography.HashString("admin@admin.com"),
                Password = _cryptography.HashString("adminpassword"),
                Contact = "937929222",
                Role = 1
            });
            context.Admin.Add(new WebAPI.Entity.Admin
            {
                Id = 3,
                Username = "Admin2",
                Email = _cryptography.HashString("admin2@admin2.com"),
                Password = _cryptography.HashString("admin2password"),
                Contact = "937923322",
                Role = 1
            });


            await context.SaveChangesAsync();
        }


        private async void populateUsers(DataContext context)
        {
            context.User.Add(new WebAPI.Entity.User
            {
                Id = 4,
                Username = "User",
                Email = _cryptography.HashString("user@user.com"),
                Password = _cryptography.HashString("userpassword"),
                Contact = "912548966",
                RolePaid = false,
                Location = "Guimarães",
                CareExperience = ExperienceLevels.intermediate,
                WaterAvailability = WaterLevels.High,
                LuminosityAvailability = LightLevel.Medium,
                UserAvatar = ""
            });
            context.User.Add(new WebAPI.Entity.User
            {
                Id = 5,
                Username = "User2",
                Email = _cryptography.HashString("user2@user2.com"),
                Password = _cryptography.HashString("user2password"),
                Contact = "912548966",
                RolePaid = true,
                Location = "Guimarães",
                CareExperience = ExperienceLevels.Beginner,
                WaterAvailability = WaterLevels.Low,
                LuminosityAvailability =LightLevel.Medium,
                UserAvatar = ""
            });
            context.User.Add(new WebAPI.Entity.User
            {
                Id = 6,
                Username = "User3",
                Email = _cryptography.HashString("user23@user.com"),
                Password = _cryptography.HashString("userpassword23"),
                Contact = "912542366",
                RolePaid = false,
                Location = "Viseu",
                CareExperience = ExperienceLevels.intermediate,
                WaterAvailability = WaterLevels.High,
                LuminosityAvailability = LightLevel.Medium,
                UserAvatar = ""
            });

            context.User.Add(new WebAPI.Entity.User
            {
                Id = 7,
                Username = "User4",
                Email = _cryptography.HashString("user3@user3.com"),
                Password = _cryptography.HashString("user3password"),
                Contact = "912548966",
                RolePaid = true,
                Location = "Guimarães",
                CareExperience = ExperienceLevels.Expert,
                WaterAvailability = WaterLevels.High,
                LuminosityAvailability = LightLevel.High,
                UserAvatar = ""
            });

            context.User.Add(new WebAPI.Entity.User
            {
                Id = 8,
                Username = "User5",
                Email = _cryptography.HashString("user4@user4.com"),
                Password = _cryptography.HashString("user4password"),
                Contact = "912548966",
                RolePaid = true,
                Location = "Guimarães",
                CareExperience = ExperienceLevels.Beginner,
                WaterAvailability = WaterLevels.Medium,
                LuminosityAvailability = LightLevel.High,
                UserAvatar = ""
            });

            await context.SaveChangesAsync();
        }

        private async void populatePlants(DataContext context)
        {
            context.Plant.Add(new Plant
            {
                Id = 1,
                AdminID = 2,
                Name = "Plant",
                Description = "Description",
                Type = TypesPlants.Vegetable,
                ExpSuggested = ExperienceLevels.Beginner,
                WaterNeeds = WaterLevels.Low,
                LuminosityNeeded = LightLevel.Medium,
                PlantImage = ""
            });
            context.Plant.Add(new Plant
            {
                Id = 2,
                AdminID = 2,
                Name = "Plant2",
                Description = "Description",
                Type = TypesPlants.Fruit,
                ExpSuggested = ExperienceLevels.Beginner,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Low,
                PlantImage = ""
            });
            context.Plant.Add(new Plant
            {
                Id = 3,
                AdminID = 2,
                Name = "Plant3",
                Description = "Description",
                Type = TypesPlants.Flower,
                ExpSuggested = ExperienceLevels.Expert,
                WaterNeeds = WaterLevels.Low,
                LuminosityNeeded = LightLevel.Medium,
                PlantImage = ""
            });
            context.Plant.Add(new Plant
            {
                Id = 4,
                AdminID = 2,
                Name = "Plant4",
                Description = "Description",
                Type = TypesPlants.Vegetable,
                ExpSuggested = ExperienceLevels.Beginner,
                WaterNeeds = WaterLevels.Low,
                LuminosityNeeded = LightLevel.Medium,
                PlantImage = ""
            });
            context.Plant.Add(new Plant
            {
                Id = 5,
                AdminID = 2,
                Name = "Plant5",
                Description = "Description",
                Type = TypesPlants.Fruit,
                ExpSuggested = ExperienceLevels.Beginner,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Low,
                PlantImage = ""
            });
            context.Plant.Add(new Plant
            {
                Id = 6,
                AdminID = 2,
                Name = "Plant6",
                Description = "Description",
                Type = TypesPlants.Flower,
                ExpSuggested = ExperienceLevels.Expert,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Medium,
                PlantImage = ""
            });

            context.Plant.Add(new Plant
            {
                Id = 7,
                AdminID = 2,
                Name = "Plant7",
                Description = "Description",
                Type = TypesPlants.Succulent,
                ExpSuggested = ExperienceLevels.Beginner,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Medium,
                PlantImage = ""
            });
            context.Plant.Add(new Plant
            {
                Id = 8,
                AdminID = 2,
                Name = "Plant8",
                Description = "Description",
                Type = TypesPlants.Flower,
                ExpSuggested = ExperienceLevels.Expert,
                WaterNeeds = WaterLevels.Low,
                LuminosityNeeded = LightLevel.Medium,
                PlantImage = ""
            });
            context.Plant.Add(new Plant
            {
                Id = 9,
                AdminID = 2,
                Name = "Plant8",
                Description = "Description",
                Type = TypesPlants.Flower,
                ExpSuggested = ExperienceLevels.Expert,
                WaterNeeds = WaterLevels.High,
                LuminosityNeeded = LightLevel.Medium,
                PlantImage = ""
            });

            await context.SaveChangesAsync();
        }

        private async void populateAds(DataContext context)
        {
            context.Ad.Add(new Ad
            {
                Id = 1,
                AdminID = 2,
                isActive = true,
                StartDate = DateTime.Now,
                EndDate = DateTime.Now.AddDays(1),
                AdFile = ""
            });
            context.Ad.Add(new Ad
            {
                Id = 2,
                AdminID = 1,
                isActive = true,
                StartDate = DateTime.Now,
                EndDate = DateTime.Now.AddDays(1),
                AdFile = ""
            });
            context.Ad.Add(new Ad
            {
                Id = 3,
                AdminID = 2,
                isActive = true,
                StartDate = DateTime.Now,
                EndDate = DateTime.Now.AddDays(1),
                AdFile = ""
            });

            await context.SaveChangesAsync();
        }

        private async void populateTasks(DataContext context)
        {
            // Ensure tasks are associated with correct PlantId and AdminId
            context.Task.Add(new WebAPI.Entity.PlantTask
            {
                Id = 1,
                AdminId = 2,
                PlantId = 1,
                TaskName = "TaskName1",
                TaskDescription = "TaskDescription1"
            });
            context.Task.Add(new WebAPI.Entity.PlantTask
            {
                Id = 2,
                AdminId = 2,
                PlantId = 1,
                TaskName = "TaskName2",
                TaskDescription = "TaskDescription2"
            });
            context.Task.Add(new WebAPI.Entity.PlantTask
            {
                Id = 3,
                AdminId = 2,
                PlantId = 2,
                TaskName = "TaskName3",
                TaskDescription = "TaskDescription3"
            });

            await context.SaveChangesAsync();
        }

        /**
         *  //Chave primária composta
        [Key]
        public int Id { get; set; }

        // Chaves estrangeiras para a planta e para o utilizador
        [ForeignKey("Plant")]
        public int PlantID { get; set; } // Identificador da planta

        [ForeignKey("User")]
        public int PersonID { get; set; } // Identificador do Utilizador

       

      Tenho Users com id 3 e 4, onde 4 é paid e 3 é free
        */
        private async void populateUserPlants(DataContext context)
        {
            context.UserPlant.Add(new UserPlant
            {
                Id = 1,
                PlantID = 1,
                PersonID = 5,
            });
            context.UserPlant.Add(new UserPlant
            {
                Id = 2,
                PlantID = 2,
                PersonID = 5,
            });
            context.UserPlant.Add(new UserPlant
            {
                Id = 3,
                PlantID = 3,
                PersonID = 5,
            });
            context.UserPlant.Add(new UserPlant
            {
                Id = 4,
                PlantID = 4,
                PersonID = 5,
            }); // I have 4 plants associated to a paid user
            context.UserPlant.Add(new UserPlant
            {
                Id = 5,
                PlantID = 1,
                PersonID = 4,
            });
            context.UserPlant.Add(new UserPlant
            {
                Id = 6,
                PlantID = 2,
                PersonID = 4,
            });
            context.UserPlant.Add(new UserPlant
            {
                Id = 7,
                PlantID = 3,
                PersonID = 4,
            }); // and 3 plants associated to a free user.
            await context.SaveChangesAsync();
        }

        private async void populatePayments(DataContext context)
        {
            context.Payment.Add(new Payment
            {
                Id = 1,
                UserId = 5,
                Title = "halloPayment",
                CreationDate = DateTime.Now,
            });
            await context.SaveChangesAsync();
        }

        private async void populateWarnings(DataContext context)
        {
            context.Warning.Add(new Warning
            {
                Id = 1,
                UserId = 4,
                Location = "Guimarães",
                Message = "Neve",
                ReminderDate = DateTime.Now
            });
            context.Warning.Add(new Warning
            {
                Id = 2,
                UserId = 4,
                Location = "Guimarães",
                Message = "Muita Chuva",
                ReminderDate = DateTime.Now
            });
            context.Warning.Add(new Warning
            {
                Id = 3,
                UserId = 5,
                Location = "Guimarães",
                Message = "Muita Neve",
                ReminderDate = DateTime.Now
            });
            context.Warning.Add(new Warning
            {
                Id = 4,
                UserId = 5,
                Location = "Guimarães",
                Message = "Vendaval",
                ReminderDate = DateTime.Now
            });

            await context.SaveChangesAsync();
        }
        
        private async void populateDiaries(DataContext context)
        {
            context.Diary.Add(new Diary
            {
                Id = 1,
                UserPlantId = 1,
                Title = "Diary 1",
                CreationDate = DateTime.Now,
            });

            context.Diary.Add(new Diary
            {
                Id = 2,
                UserPlantId = 2,
                Title = "Diary 2",
                CreationDate = DateTime.Now,
            });

            await context.SaveChangesAsync();

        }
        private async void populateLogs(DataContext context)
        {
            context.Log.Add(new Log
            {
                Id = 1,
                DiaryId = 1,
                LogDate = DateTime.Now,
                LogDescription = "LogMessage1"
            });

            context.Log.Add(new Log
            {
                Id = 2,
                DiaryId = 2,
                LogDate = DateTime.Now,
                LogDescription = "LogMessage2"
            });

            context.Log.Add(new Log
            {
                Id = 3,
                DiaryId = 3,
                LogDate = DateTime.Now,
                LogDescription = "LogMessage3"
            });

            await context.SaveChangesAsync();
        }
    }
}
