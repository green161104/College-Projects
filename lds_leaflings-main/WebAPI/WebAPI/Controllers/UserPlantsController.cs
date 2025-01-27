using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.exceptions;
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserPlantsController : ControllerBase
    {
        private readonly UserPlantsService _userPlantsService;
        private readonly UserService _userService;
        private readonly Cryptography _crypt;

        public UserPlantsController(DataContext context, Cryptography cryptography)
        {
            _userPlantsService = new UserPlantsService(context);
            _userService = new UserService(context, cryptography);
        }

        /// <summary>
        /// Retrieves all associations between users and plants.
        /// </summary>
        /// <returns>A list of UserPlant objects.</returns>
        [HttpGet]
        [Authorize] // Protege este endpoint
        public async Task<ActionResult<List<UserPlant>>> GetAllPlantsUsers()
        {
            try
            {
                List<UserPlant> allUserPlants = await _userPlantsService.GetAllUserPlantsAsync();

                if (allUserPlants == null || allUserPlants.Count == 0)
                {
                    return NoContent();
                }

                return Ok(new
                {
                    data = allUserPlants,
                    total = allUserPlants.Count
                });
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        /// <summary>
        /// Retrieves plants associated with a specific user.
        /// </summary>
        /// <param name="userId">The ID of the user whose plants are to be retrieved.</param>
        /// <returns>A list of UserPlantDto objects.</returns>
        [HttpGet("{userId}")]
        [Authorize]
        public async Task<ActionResult<List<UserPlantResponseDto>>> GetPlantsByUser(int userId)
        {

            try
            {
                List<UserPlant> userPlants = await _userPlantsService.GetPlantsByUserIdAsync(userId);

                if (userPlants == null || !userPlants.Any())
                {
                    return NoContent();
                }

                var result = userPlants.Select(up => new UserPlantResponseDto
                {

                    Id = up.Id,
                    UserId = up.PersonID,
                    Plant = new PlantResponseDto
                    {
                        Id = up.Plant.Id,
                        Name = up.Plant.Name,
                        Type = up.Plant.Type,
                        ExpSuggested = up.Plant.ExpSuggested,
                        WaterNeeds = up.Plant.WaterNeeds,
                        Description = up.Plant.Description,
                        LuminosityNeeded = up.Plant.LuminosityNeeded,
                        PlantImage = up.Plant.PlantImage
                    }
                }).ToList();

                return Ok(result);
            } catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }


        /// <summary>
        /// Retrieves plants associated with a specific user.
        /// </summary>
        /// <param name="userId">The ID of the user whose plants are to be retrieved.</param>
        /// <returns>A list of UserPlantDto objects.</returns>
        [HttpGet("plant/{plantId}")]
        [Authorize] // Protege este endpoint
        public async Task<ActionResult<List<UserPlant>>> GetUsersByPlant(int plantId)
        {
            try
            {
                List<UserPlant> userPlants = await _userPlantsService.GetUsersByPlantIdAsync(plantId);

                if (userPlants == null || !userPlants.Any())
                {
                    return NotFound();
                }

                var result = userPlants.Select(up => new UserPlantRequestDto
                {
                    PlantId = up.PlantID,
                    UserId = up.PersonID,
                }).ToList();

                return Ok(result);
            } catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        /// <summary>
        /// Adds a new association between a user and a plant.
        /// </summary>
        /// <param name="userPlant">The UserPlantDto object containing user and plant IDs.</param>
        /// <returns>The created UserPlant object.</returns>
        [HttpPost]
        [Authorize] // Protege este endpoint
        public async Task<ActionResult<UserPlant>> AddUserPlant(UserPlantRequestDto userPlant)
        {
            try
            {
                if (userPlant == null)
                {
                    return BadRequest(new ErrorResponseDto { error = "Invalid Data" });
                }

                UserPlant createdUserPlant = await _userPlantsService.AddUserPlantAsync(userPlant);

                if (createdUserPlant == null) // se o método adduserplant retornar null
                {
                    return BadRequest(new ErrorResponseDto { error = "Error while adding plant to user" });
                }

                return Ok(createdUserPlant);  // Retorna a associação criada
            } 
            catch(NonPaidUserException e)
            {
                return StatusCode((int)HttpStatusCode.BadRequest, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        /// <summary>
        /// Deletes the association between a user and a plant.
        /// </summary>
        /// <param name="userId">The ID of the user.</param>
        /// <param name="plantId">The ID of the plant.</param>
        /// <returns>No content if the deletion was successful.</returns>
        [HttpDelete("{userId}/{plantId}")]
        [Authorize] // Protege este endpoint
        public async Task<IActionResult> DeleteUserPlant(int userId, int plantId)
        {
            try
            {
                bool deleted = await _userPlantsService.DeleteUserPlantAsync(userId, plantId);

                if (!deleted)
                    return NotFound();

                return NoContent();
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }


    }
}
