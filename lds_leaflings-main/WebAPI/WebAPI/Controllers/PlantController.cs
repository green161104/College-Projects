using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using System.Text.Json;
using WebAPI.DTO.request;
using WebAPI.DTO.response;
using WebAPI.Entity;
using WebAPI.Services;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace WebAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PlantController : ControllerBase
    {
        private readonly PlantService _plantService;

        public PlantController(PlantService plantService)
        {
            _plantService = plantService;
        }


        /// <summary>
        /// Retrieves a list of all plants available in the DB.
        /// </summary>
        /// <remarks>
        /// This endpoint requires the user to be authorized. It also exposes the total count of plants
        /// through the "Content-Range" header.
        /// </remarks>
        /// <returns>
        /// A list of <see cref="Plant"/> objects representing all plants.
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the list of plants.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        [HttpGet]
        [Authorize] // Protege este endpoint
        public async Task<ActionResult<List<PlantResponseDto>>> GetAllPlants(
            [FromQuery] int _limit = 5000,
            [FromQuery] int _page = 1,
            [FromQuery] string _sort = "Id",
            [FromQuery] string _order = "ASC"
        )
        {
            try
            {
                // Calculate the start and end indices
                int start = (_page - 1) * _limit;
                int end = _limit * _page;

                var plants = await _plantService.GetAllPlantsAsync(start, end, _sort, _order);
                int totalPlants = await _plantService.GetTotalPlantsCountAsync();

                if (plants == null || plants.Count == 0)
                    return NoContent();

                var plantsResponseDtos = plants.Select(plant => new PlantResponseDto
                {
                    Id = plant.Id,
                    Name = plant.Name,
                    Type = plant.Type,
                    ExpSuggested = plant.ExpSuggested,
                    WaterNeeds = plant.WaterNeeds,
                    LuminosityNeeded = plant.LuminosityNeeded,
                    Description = plant.Description,
                    PlantImage = plant.PlantImage
                }).ToList();



                return Ok(new
                {
                    data = plantsResponseDtos,
                    total = totalPlants
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
        /// Retrieves a list of all plants available in the DB that match contain the name given ignoring capslocks.
        /// </summary>
        /// <remarks>
        /// This endpoint requires the user to be authorized. It also exposes the total count of plants that match the name
        /// through the "Content-Range" header.
        /// </remarks>
        /// <returns>
        /// A list of <see cref="Plant"/> objects representing all plants that match the name.
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the list of plants.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>

        // GET: api/plants/search/{name}
        [HttpGet("search/{name}")]
        public async Task<ActionResult<List<PlantResponseDto>>> GetAllPlantsByName(string name)
        {
            try
            {
                var plants = await _plantService.SearchPlantsByNameAsync(name);

                var plantsResponseDtos = plants.Select(plant => new PlantResponseDto
                {
                    Id = plant.Id,
                    Name = plant.Name,
                    Type = plant.Type,
                    ExpSuggested = plant.ExpSuggested,
                    WaterNeeds = plant.WaterNeeds,
                    LuminosityNeeded = plant.LuminosityNeeded,
                    Description = plant.Description,
                    PlantImage = plant.PlantImage
                }).ToList();

                return Ok(new
                {
                    data = plantsResponseDtos,
                    total = plantsResponseDtos.Count
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
        /// Retrieves a specific plant by its unique identifier.
        /// </summary>
        /// <param name="id">The ID of the plant to retrieve.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the plant is found, it is returned; otherwise,
        /// a 404 status code is returned with a "Plant not found" message.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the <see cref="Plant"/> object if found, 
        /// or a 404 Not Found response if the plant does not exist. 
        /// Returns a <see cref="StatusCodeResult"/> with an HTTP 500 status code if an internal error occurs.
        /// </returns>
        /// <response code="200">Returns the requested plant.</response>
        /// <response code="404">If the plant with the specified ID is not found.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>

        // GET: api/plants/{id}
        [HttpGet("{id}")]
        [Authorize] // Protege este endpoint
        public async Task<ActionResult<PlantResponseDto>> GetPlant(int id)
        {
            try
            {

                var plant = await _plantService.GetPlantByIdAsync(id);

                if (plant == null)
                    return NotFound(new ErrorResponseDto { error = "Plant not found" });

                var plantDto = new PlantResponseDto
                {
                    Id = plant.Id,
                    Name = plant.Name,
                    Type = plant.Type,
                    ExpSuggested = plant.ExpSuggested,
                    WaterNeeds = plant.WaterNeeds,
                    LuminosityNeeded = plant.LuminosityNeeded,
                    Description = plant.Description,
                    PlantImage = plant.PlantImage
                };

                return Ok(plantDto);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }

        }


        /// <summary>
        /// Adds a new plant to the system.
        /// </summary>
        /// <param name="plant">A <see cref="PlantRequestDto"/> object containing the details of the plant to add.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the plant is added successfully, 
        /// it returns the created plant object with a 201 status code and includes the plant's location in the response header.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the newly created <see cref="Plant"/> object.
        /// Returns a 400 Bad Request if the input data is invalid, or a 500 Internal Server Error if an error occurs during processing.
        /// </returns>
        /// <response code="201">Returns the created plant and the URI of the new resource in the Location header.</response>
        /// <response code="400">If the provided plant data is invalid.</response>
        /// <response code="500">If an internal server error occurs, includes an error message.</response>
        /// [FromForm] -> Devido a haver uploads de images/Ficheiro
        // POST: api/plants
        [HttpPost]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<PlantResponseDto>> AddPlant([FromForm] PlantRequestDto plant)
        {
            if (plant == null)
                return BadRequest(new ErrorResponseDto { error = "Invalid data.", details = "Plant data cannot be null." });

            try
            {
                var createdPlant = await _plantService.AddPlantAsync(plant);

                var plantResponse = new PlantResponseDto
                {
                    Id = createdPlant.Id,
                    Name = createdPlant.Name,
                    Type = createdPlant.Type,
                    ExpSuggested = createdPlant.ExpSuggested,
                    WaterNeeds = createdPlant.WaterNeeds,
                    LuminosityNeeded = createdPlant.LuminosityNeeded,
                    Description = createdPlant.Description,
                    PlantImage = createdPlant.PlantImage
                };

                return CreatedAtAction(nameof(GetPlant), new { id = plantResponse.Id }, plantResponse);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }


        /// <summary>
        /// Updates the details of an existing plant.
        /// </summary>
        /// <param name="id">The ID of the plant to update.</param>
        /// <param name="updatedPlant">An object of type <see cref="Plant"/> containing the updated details of the plant.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the plant with the specified ID exists, it is updated; otherwise,
        /// a 404 status code is returned indicating that the plant was not found.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> containing the updated <see cref="Plant"/> object if the update is successful.
        /// Returns a 400 Bad Request if the input data is invalid, or a 404 Not Found if the plant does not exist.
        /// </returns>
        /// <response code="200">Returns the updated plant object.</response>
        /// <response code="400">If the provided plant data is invalid.</response>
        /// <response code="404">If a plant with the specified ID is not found.</response>
        // PUT: api/plants
        [HttpPut("{id}")]
        [Authorize(Policy = "AdminPolicy")] // Apenas admins podem aceder
        public async Task<ActionResult<PlantResponseDto>> UpdatePlant(int id, [FromForm] PlantRequestDto updatedPlant)
        {
            if (updatedPlant == null)
            {
                return BadRequest(new ErrorResponseDto { error = "Invalid data." });
            }

            try
            {
                // Chama o serviço para atualizar a planta, passando o ID e os dados atualizados
                var updated = await _plantService.UpdatePlantAsync(id, updatedPlant);

                if (!updated)
                {
                    return NotFound(new ErrorResponseDto { error = $"Plant with id {id} not found." });
                }

                var plant = await _plantService.GetPlantByIdAsync(id);

                var plantResponse = new PlantResponseDto
                {
                    Id = plant.Id,
                    Name = plant.Name,
                    Type = plant.Type,
                    ExpSuggested = plant.ExpSuggested,
                    WaterNeeds = plant.WaterNeeds,
                    LuminosityNeeded = plant.LuminosityNeeded,
                    Description = plant.Description,
                    PlantImage = plant.PlantImage
                };

                return Ok(plantResponse);

            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError, new ErrorResponseDto
                {
                    error = $"{ex.Message}"
                });
            }
        }


        /// <summary>
        /// Deletes a specific plant by its unique identifier.
        /// </summary>
        /// <param name="id">The ID of the plant to delete.</param>
        /// <remarks>
        /// This endpoint requires the user to be authorized. If the plant with the specified ID is found, it is deleted; 
        /// otherwise, a 404 status code is returned indicating that the plant was not found.
        /// </remarks>
        /// <returns>
        /// An <see cref="ActionResult"/> with a 204 No Content status code if the deletion is successful. 
        /// Returns a 404 Not Found if the plant does not exist.
        /// </returns>
        /// <response code="204">Indicates that the plant was successfully deleted.</response>
        /// <response code="404">If a plant with the specified ID is not found.</response>
        // DELETE: api/Plants/{id}
        [HttpDelete("{id}")]
        [Authorize] // Protege este endpoint
        public async Task<ActionResult> DeletePlant(int id)
        {
            try
            {
                var deleted = await _plantService.DeletePlantAsync(id);

                if (!deleted)
                    return NotFound(new ErrorResponseDto { error = "Plant not found" });

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
