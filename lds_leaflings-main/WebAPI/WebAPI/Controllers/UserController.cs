using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Claims;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Entity.enums;
using WebAPI.Services;

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly UserService _userProfileService;

        public UserController(UserService userProfileService)
        {
            _userProfileService = userProfileService;
        }

        [HttpGet]
        [Authorize]
        public async Task<ActionResult<List<UserResponseDto>>> GetAllUsers(
            [FromQuery] int _limit = 5,
            [FromQuery] int _page = 1,
            [FromQuery] string _sort = "Id",
            [FromQuery] string _order = "ASC")
        {
            try
            {
                // Calculate the start and end indices
                int start = (_page - 1) * _limit;
                int end = _limit * _page;

                var users = await _userProfileService.GetAllUsersAsync(start, end, _sort, _order);
                int totalUsers = await _userProfileService.GetTotalUsersCountAsync();

                if (users == null || users.Count == 0)
                    return NoContent();

                var userDtos = users.Select(user => new UserResponseDto
                {
                    Id = user.Id,
                    Username = user.Username,
                    Email = user.Email,
                    Contact = user.Contact,
                    RolePaid = user.RolePaid,
                    Location = user.Location,
                    CareExperience = user.CareExperience,
                    WaterAvailability = user.WaterAvailability,
                    LuminosityAvailability = user.LuminosityAvailability,
                    UserAvatar = user.UserAvatar
                }).ToList();

                return Ok(new
                {
                    data = users,
                    total = totalUsers
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

        // GET: api/user/{id}
        [HttpGet("{id}")]
        [Authorize]
        public async Task<ActionResult<UserResponseDto>> GetUserById(int id)
        {
            try
            {
                var user = await _userProfileService.GetUserByIdAsync(id);
                if (user is null)
                    return NotFound(new ErrorResponseDto { error = "User not found" });

                return Ok(new UserResponseDto
                {
                    Id = user.Id,
                    Email = user.Email,
                    Username = user.Username,
                    Contact = user.Contact,
                    RolePaid = user.RolePaid,
                    Location = user.Location,
                    CareExperience = user.CareExperience,
                    WaterAvailability = user.WaterAvailability,
                    LuminosityAvailability = user.LuminosityAvailability,
                    UserAvatar = user.UserAvatar
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

        [HttpPost]
        public async Task<ActionResult<UserResponseDto>> AddUser([FromForm] UserRequestDto userDto)
        {
            // Verifica se os dados do utilizador são válidos
            if (userDto == null)
                return BadRequest(new ErrorResponseDto{ error = "Invalid data.", details = "User data cannot be null." });

            try
            {
                // Cria o utilizador apenas com os dados do perfil básico
                var createdUser = await _userProfileService.AddUserAsync(userDto);

                if (createdUser == null)
                {
                    return BadRequest(new ErrorResponseDto
                    {
                        error = "Error while creating the User",
                        details = "Ensure all fields are filled correctly"
                    });
                }

                var userResponse = new UserResponseDto
                {
                    Id = createdUser.Id,
                    Username = createdUser.Username,
                    Email = createdUser.Email,
                    Contact = createdUser.Contact,
                    RolePaid = createdUser.RolePaid,
                    Location = createdUser.Location,
                    CareExperience = createdUser.CareExperience,
                    WaterAvailability = createdUser.WaterAvailability,
                    LuminosityAvailability = createdUser.LuminosityAvailability,
                    UserAvatar = createdUser.UserAvatar
                };

                // Retorna o utilizador recém-criado
                return CreatedAtAction(nameof(GetUserById), new { id = createdUser.Id }, userResponse);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }


        [HttpPut("preferences/{id}")]
        [Authorize]
        public async Task<ActionResult<bool>> UpdateUserPreferences(int id, UserPreferencesRequestDto newUserPreferences)
        {
            // Verifica se os dados são válidos
            if (newUserPreferences == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid data." });

            try
            {
                var updateSuccessful = await _userProfileService.UpdateUserPreferencesAsync(id, newUserPreferences);

                if (!updateSuccessful)
                    return NotFound();


                return Ok(updateSuccessful);
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        [HttpPut("password/{id}")]
        [Authorize]
        public async Task<ActionResult> UpdatePassword(int id, [FromBody] PasswordUpdateRequestDto passwordDto)
        {
            if (passwordDto == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid data." });

            try
            {
                var updateSuccessful = await _userProfileService.UpdatePasswordAsync(
                    id,
                    passwordDto.OldPassword,
                    passwordDto.NewPassword
                );

                if (!updateSuccessful)
                    return NotFound(new ErrorResponseDto { error = "User not found" });

                return Ok(new { message = "Password updated successfully" });
            }
            catch (InvalidOperationException e)
            {
                return BadRequest(new ErrorResponseDto { error = e.Message });
            }
            catch (ArgumentException e)
            {
                return BadRequest(new ErrorResponseDto { error = e.Message });
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        [HttpPut("{id}")]
        [Authorize]
        public async Task<ActionResult> UpdateUser(int id, [FromBody] UpdateUserInformationDto updateUserInformation)
        {
            if (updateUserInformation == null ||
                string.IsNullOrEmpty(updateUserInformation.Username) ||
                string.IsNullOrEmpty(updateUserInformation.Location) ||
                string.IsNullOrEmpty(updateUserInformation.Contact))
            {
                return BadRequest(new ErrorResponseDto { error = "Invalid data." });
            }

            try
            {
                var updatedUser = await _userProfileService.UpdateUserInformation(
                    id,
                    updateUserInformation.Username,
                    updateUserInformation.Location,
                    updateUserInformation.Contact
                );

                if (updatedUser == null)
                    return NotFound(new ErrorResponseDto { error = "User not found" });

                UserResponseDto updatedUserDto = new UserResponseDto
                {
                    Id = updatedUser.Id,
                    Username = updatedUser.Username,
                    Email = updatedUser.Email,
                    Contact = updatedUser.Contact,
                    RolePaid = updatedUser.RolePaid,
                    Location = updatedUser.Location,
                    CareExperience = updatedUser.CareExperience,
                    WaterAvailability = updatedUser.WaterAvailability,
                    LuminosityAvailability = updatedUser.LuminosityAvailability,
                    UserAvatar = updatedUser.UserAvatar
                };

                return Ok(updatedUserDto);
            }
            catch (InvalidOperationException e)
            {
                return BadRequest(new ErrorResponseDto { error = e.Message });
            }
            catch (ArgumentException e)
            {
                return BadRequest(new ErrorResponseDto { error = e.Message });
            }
            catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }
    

        [HttpPut("image/{id}")]
        [Authorize]
        public async Task<ActionResult> UpdateImage(int id, IFormFile image)
        {
            if (image == null)
                return BadRequest(new { error = "No image file provided." });

            try
            {
                var updateSuccessful = await _userProfileService.UpdateImageAsync(id, image);

                if (!updateSuccessful)
                    return NotFound(new { error = "User not found" });

                var user = await _userProfileService.GetUserByIdAsync(id);

                return Ok(new
                {
                    message = "Image updated successfully",
                    imagePath = user.UserAvatar
                });
            }
            catch (Exception e)
            {
                return StatusCode(500, new { error = e.Message });
            }
        }


        // DELETE: api/users/{id}
        [HttpDelete("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult> DeleteUser(int id)
        {
            try
            {
                var deleteSuccessful = await _userProfileService.DeleteUserAsync(id);

                if (!deleteSuccessful)
                    return NotFound(new ErrorResponseDto { error = "User not found" });

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


        [HttpGet("match/{userId}")]
        [Authorize]
        public async Task<ActionResult> GetPlantMatches(int userId)
        {
            try
            {

                var user = await _userProfileService.GetUserByIdAsync(userId);
                var matches = await _userProfileService.MatchPlantsToUserAsync(user);

                return Ok(new
                {
                    perfectMatches = matches.perfectMatches.Select(p => new
                    {
                        p.Id,
                        p.Name,
                        p.ExpSuggested,
                        p.WaterNeeds,
                        p.LuminosityNeeded,
                        p.Description,
                        p.PlantImage,
                        p.Type,
                        matchType = "Perfect Match (3/3)"
                    }),
                    averageMatches = matches.mediumMatches.Select(p => new
                    {
                        p.Id,
                        p.Name,
                        p.ExpSuggested,
                        p.WaterNeeds,
                        p.LuminosityNeeded,
                        p.Description,
                        p.PlantImage,
                        p.Type,
                        matchType = "Average Match (2/3)"
                    }),
                    weakMatches = matches.weakMatches.Select(p => new
                    {
                        p.Id,
                        p.Name,
                        p.ExpSuggested,
                        p.WaterNeeds,
                        p.LuminosityNeeded,
                        p.Description,
                        p.PlantImage,
                        p.Type,
                        matchType = "Weak Match (1/3)"
                    }),
                    noMatches = matches.noMatches.Select(p => new
                    {
                        p.Id,
                        p.Name,
                        p.ExpSuggested,
                        p.WaterNeeds,
                        p.LuminosityNeeded,
                        p.Description,
                        p.PlantImage,
                        p.Type,
                        matchType = "Weak Match (0/3)"
                    })
                });
            }
            catch (KeyNotFoundException ex)
            {
                return NotFound(new ErrorResponseDto { error = "User Not Found", details = ex.Message });
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
