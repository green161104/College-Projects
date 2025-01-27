using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class WarningController : ControllerBase
    {

        private readonly WarningService _warningService;

        public WarningController(WarningService warningService)
        {
            _warningService = warningService;
        }

        /// <summary>
        /// Retrieves all warnings associated with a specific user.
        /// </summary>
        /// <param name="UserId">The ID of the user whose warnings are to be retrieved.</param>
        /// <returns>A list of Warning objects.</returns>
        [HttpGet("{UserId}")]
        [Authorize]
        public async Task<ActionResult<List<Warning>>> GetUserWarnings(int UserId)
        {
            try
            { 
               List<Warning> warnings = await _warningService.GetAllWarningsFromUserAsync(UserId);

                if (warnings == null || warnings.Count == 0)
                {
                    return NoContent();
                }

                return Ok(new
                {
                    data = warnings,
                    total = warnings.Count
                });
            } catch (Exception e)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{e.Message}"
                });
            }
        }

        /// <summary>
        /// Creates a new warning for a user.
        /// </summary>
        /// <param name="warning">The WarningDto object containing the warning details.</param>
        /// <returns>The newly created Warning object.</returns>
        [HttpPost]
        [Authorize]
        public async Task<ActionResult<Warning>> CreateWarning(WarningRequestDto warning)
        {
            if (warning == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid Data."});

            try
            {
                Warning newWarning = await _warningService.AddWarningAsync(warning);

                if (newWarning == null)
                {
                    return BadRequest(new ErrorResponseDto { error = "An error occured, warning not added" });
                }

                return Ok(newWarning);
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
