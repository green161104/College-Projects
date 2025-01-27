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
    public class AdController : ControllerBase
    {

        private readonly AdService _adService;

        public AdController(AdService adservice)
        {
            _adService = adservice;
        }



        /// <summary>
        /// Retrieves a list of all ads available in the DB.
        /// </summary>
        /// <remarks>
        /// This endpoint requires the user to be authorized. It also exposes the total count of ads
        /// through the "Content-Range" header.
        /// </remarks>
        /// <returns>
        /// A list of <see cref="Ad"/> objects representing all ads.
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the list of ads.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        [HttpGet]
        [Authorize]
        public async Task<ActionResult<List<Ad>>> GetAllAds(
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

                var ads = await _adService.GetAllAds(start, end, _sort, _order);
                int totalAds = await _adService.GetTotalAdsCountAsync();

                if (ads == null || ads.Count == 0)
                    return NoContent();

                return Ok(new
                {
                    data = ads,
                    total = totalAds
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
        /// Retrieves a specific ad by its unique identifier.
        /// </summary>
        /// <param name="id">The ID of the ad to retrieve.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the ad is found, it is returned; otherwise,
        /// a 404 status code is returned with a "Ad not found" message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="Ad"/> object if found, 
        /// or a 404 Not Found response if the plant does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        // GET: api/Ad/{id}
        [HttpGet("{id}")]
        [Authorize]
        public async Task<ActionResult<Ad>> GetAdById(int id)
        {
            try
            {
                var ad = await _adService.GetAdByID(id);
                if (ad == null) return NotFound(new ErrorResponseDto { error = "Ad not found" });

                return Ok(ad);
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
        /// Retrieves the start date of a specific ad by its unique identifier.
        /// </summary>
        /// <param name="id">The start date of the ad to retrieve.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the ad is found, it is returned; otherwise,
        /// a 404 status code is returned with a "ad not found" message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="DateTime"/> object if found, 
        /// or a 404 Not Found response if the ad does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        // GET: api/Ad/{id}
        [HttpGet("start/{id}")]
        [Authorize]
        public async Task<ActionResult<DateTime>> GetAdStartDate(int id)
        {
            try
            {
                var dateTime = await _adService.GetAdStart(id);
                if (dateTime == null) return NotFound(new ErrorResponseDto { error = "Ad not found" });

                return Ok(dateTime);
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
        /// Retrieves the end date of a specific ad by its unique identifier.
        /// </summary>
        /// <param name="id">The end date of the ad to retrieve.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the ad is found, it is returned; otherwise,
        /// a 404 status code is returned with a "ad not found" message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="DateTime"/> object if found, 
        /// or a 404 Not Found response if the ad does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        // GET: api/Ad/{id}
        [HttpGet("end/{id}")]
        [Authorize]
        public async Task<ActionResult<DateTime>> GetAdEndDate(int id)
        {
            try
            {
                var dateTime = await _adService.GetAdEnd(id);
                if (dateTime == null) return NotFound(new ErrorResponseDto { error = "Ad not found " });

                return Ok(dateTime);
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
        /// Retrieves the Admin creator of a specific ad by its unique identifier.
        /// </summary>
        /// <param name="id">The unique identifier of the ad we want the creator of.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the ad is found, it is returned; otherwise,
        /// a 404 status code is returned with a "ad not found" message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="Admin"/> object if found, 
        /// or a 404 Not Found response if the ad does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        // GET: api/Ad/{id}
        [HttpGet("creator/{id}")]
        [Authorize]
        public async Task<ActionResult<Admin>> GetAdCreator(int id)
        {
            try
            {
                var admin = await _adService.GetAdCreator(id);
                if (admin == null) return NotFound(new ErrorResponseDto { error = "Ad not found" });

                return Ok(admin);
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
        /// Retrieves the count of all ads.
        /// </summary>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the count isn't null, it is returned; otherwise,
        /// a 404 status code is returned with a "something went wrong." message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="int"/> object if found, 
        /// or a 404 Not Found response if the ad does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        // GET: api/Ad/{id}
        [HttpGet("count")]
        [Authorize]
        public async Task<ActionResult<int>> GetAdCount()
        {
            try
            {
                var count = await _adService.GetAdCount();
                if (count == null) return NotFound(new ErrorResponseDto { error = "Something went wrong" });
                return Ok(count);
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
        /// Retrieves a random active advertisement.
        /// </summary>
        /// <returns>
        /// An HTTP 200 response containing the random advertisement if available, 
        /// or an HTTP 404 response if no active advertisements are found.
        /// </returns>
        /// <remarks>
        /// This endpoint selects a random active advertisement from the database.
        /// If no active advertisements are available, a 404 status is returned.
        /// </remarks>
        /// <example>
        /// Example request:
        /// <code>
        /// GET /api/Ad/random
        /// </code>
        /// Example response (if an ad is found):
        /// <code>
        /// {
        ///     "id": 5,
        ///     "isActive": true,
        ///     "message": "This is a random ad!"
        /// }
        /// </code>
        /// Example response (if no ad is found):
        /// <code>
        /// {
        ///     "error": "No active advertisements available."
        /// }
        /// </code>
        /// </example>
        [HttpGet("random")]
        public async Task<IActionResult> GetRandomActiveAd()
        {
            try
            {
                // Call the service to get a random active advertisement
                var ad = await _adService.GetRandomActiveAd();

                // If no ad is found, return a 404 response with a descriptive message
                if (ad == null)
                {
                    return NoContent();
                }

                // Return the ad in the response with a 200 status
                return Ok(ad);
            }
            catch (Exception ex)
            {
                // Return a 500 response if an unexpected error occurs
                return StatusCode((int)HttpStatusCode.InternalServerError, new
                {
                    error = "An unexpected error occurred.",
                    details = ex.Message
                });
            }
        }



        /// <summary>
        /// Retrieves a boolean value representing the success or unsuccess of an update action.
        /// </summary>
        /// <param name="id">The unique identifier of the ad we want to change.</param>
        /// <param name="adDTO">The DTO of the ad with the info we want to change.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the adDto isn't null, and the ad is found, it is updated
        /// and true is returned; otherwise,
        /// a 404 status code is returned with a "something went wrong." message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="bool"/> object if found, 
        /// or a 404 Not Found response if the ad does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        /// [FromForm] -> Devido a haver uploads de images/Ficheiro
        // GET: api/Ad/{id}
        [HttpPut("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<bool>> updateAd(int id, [FromForm] AdRequestDto adDTO)
        {
            if (adDTO == null)
            {
                return BadRequest(new ErrorResponseDto { error = "Invalid Data" });
            }
            try
            {
                var updatedAd = await _adService.UpdateAdAsync(id, adDTO);


                if (updatedAd == null)
                    return NotFound();

                return Ok(updatedAd);
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
        /// Retrieves an Ad object if the Ad is added successfully.
        /// </summary>
        /// <param name="adDTO">The DTO of the ad with the info we want to ad.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the adDto isn't null, it is added and the object is returned; otherwise,
        /// a 404 status code is returned with a "Error creating ad." message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="Ad"/> object if found, 
        /// or a 404 Not Found response if the ad does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        /// [FromForm] -> Devido a haver uploads de images/Ficheiro
        // GET: api/Ad/{id}
        [HttpPost]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<Ad>> createAd([FromForm] AdRequestDto adDto)
        {
            if (adDto == null)
            {
                return BadRequest("invalid data.");
            }
            try
            {
                var createAd = await _adService.AddAd(adDto);
                if (createAd == null) { return BadRequest(new ErrorResponseDto { error = "An error occured while creating the ad" }); }

                return CreatedAtAction(nameof(GetAdById), new { id = createAd.Id }, createAd);
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
        /// Retrieves nothing if the operation is done successfully.
        /// </summary>
        /// <param name="id">The id of the ad we wish to remove.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the ad is found, it is removed; otherwise,
        /// a 404 status code is returned with a "Ad not found." message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref=""/> object if found, 
        /// or a 404 Not Found response if the ad does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        [HttpDelete("{id}")]
        [Authorize]
        public async Task<ActionResult> DeleteAd(int id)
        {
            try
            {
                var deleteSuccessful = await _adService.deleteAdAsync(id);

                if (!deleteSuccessful)
                    return NotFound(new ErrorResponseDto { error = "Ad not found" });

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
