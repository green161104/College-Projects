using Microsoft.EntityFrameworkCore;
using System.Reflection;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Entity.enums;
using WebAPI.Utils; // Certifique-se de adicionar o namespace correto para os modelos

namespace WebAPI.Services
{
    public class PlantService
    {
        private readonly DataContext _context;        //DB context
        private readonly Images _images;              //Image service

        public PlantService(DataContext context)  // Simplified constructor matching the test
        {
            _context = context;
            _images = null;  // Or you might want to pass IWebHostEnvironment as a parameter if needed
        }

        // Alternative if you still want the original constructor
        public PlantService(DataContext context, IWebHostEnvironment env)
        {
            _context = context;
            _images = new Images(env);
        }



        public async Task<List<Plant>> SearchPlantsByNameAsync(string query)
        {
            if (string.IsNullOrWhiteSpace(query))
            {
                return new List<Plant>(); // Retorna lista vazia
            }

            // query e planta para lowercase.
            var plants = await _context.Plant
                .Where(plant => plant.Name.ToLower().Contains(query.ToLower()))
                .ToListAsync();

            return plants;
        }

        /// <GetAllPlantsAsync>
        /// Retrieves a plant object by its unique identifier.
        /// </summary>
        /// <param name="offset">The starting point of the id we want to start at.</param>
        /// <param name="limit">The amount of plants we wish to show.</param>
        /// <returns>The plant object with the specified ID, or null if not found.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<List<Plant>> GetAllPlantsAsync(int start = 0, int end = 10, string sortField = "id", string sortOrder = "ASC")
        {
            sortField = char.ToUpper(sortField[0]) + sortField.Substring(1); // Capitalize the sortField
            sortOrder = sortOrder.ToUpper(); // Convert sortOrder to uppercase

            bool sortFieldExists = SortFieldExists(sortField);

            IQueryable<Plant> query = _context.Plant
                .Skip(start)
                .Take((end - start) + 1);


            // apply sort by the sortFild
            if (sortOrder == "ASC" && sortFieldExists)
            {
                query = query.OrderBy(p => EF.Property<object>(p, sortField));
            }
            else if (sortOrder == "DESC" && sortFieldExists)
            {
                query = query.OrderByDescending(p => EF.Property<object>(p, sortField));
            }


            return await query.ToListAsync();
        }

        /// <GetPlantByIdAsync>
        /// Retrieves a plant object by its unique identifier.
        /// </summary>
        /// <param name="id">The unique identifier of the plant entry.</param>
        /// <returns>The plant object with the specified ID, or null if not found.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<Plant?> GetPlantByIdAsync(int id)
        {
            return await _context.Plant.FindAsync(id);
        }

        /// <GetPlantByExpAsync>
        /// This method should return a list of plants by their exp lvls.
        /// </summary>
        /// <param name = "explvl">is a ExperienceLevels param.</param>
        /// <returns>returns a list of plants where their exp lvls is the same as the given in params.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<List<Plant>?> GetPlantByExpAsync(ExperienceLevels explvl)
        {
            if(explvl != null){ 
                var plants = await _context.Plant.Where(plant => plant.ExpSuggested.Equals(explvl)).ToListAsync();
                return plants;
            }

            return null;
        }

        /// <GetPlantByWaterNeeds>
        /// This method should return a list of plants by their water needs.
        /// </summary>
        /// <param name = "waterlevels">is a waterlvl param.</param>
        /// <returns>returns a list of plants where their water level is the same as the given in params.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<List<Plant>?> GetPlantByWaterNeeds(WaterLevels waterlevels)
        {
            if (waterlevels != null)
            {
                var plants = await _context.Plant.Where(plant => plant.WaterNeeds.Equals(waterlevels)).ToListAsync(); // for each plant row return if waterlvl equals given waterlvl in entry param
                return plants;
            }
            return null;
        }

        /// <GetPlantByLuminosityNeeded>
        /// This method should return a list of plants by their light needs.
        /// </summary>
        /// <param name = "lightlvl">is a LightLevel enum value.</param>
        /// <returns>returns a list of plants where their Light level is the same as the given in params.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<List<Plant>?> GetPlantByLuminosityNeeded(LightLevel lightlvl)
        {
            if (lightlvl != null)
            {
                var plants = await _context.Plant.Where(plant => plant.LuminosityNeeded.Equals(lightlvl)).ToListAsync(); // for each plant row return if lightlvl equals given lightlvl in entry param
                return plants;
            }
            return null;
        }

        /// <GetPlantByTypePlant>
        /// This method should return a list of plants by their light needs.
        /// </summary>
        /// <param name = "plantType">is a TypesPlants enum value.</param>
        /// <returns>returns a list of plants where their type is the same as the given in params.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<List<Plant>?> GetPlantByTypePlant(TypesPlants plantType)
        {
            if (plantType != null)
            {
                var plants = await _context.Plant.Where(plant => plant.Type.Equals(plantType)).ToListAsync();
                return plants;
            }
            return null;
        }


        /// <AddPlantAsync>
        /// This method will add a plant type object to the table plant in the DB.
        /// the possible exceptions are thrown by the saveChangesAsync method used in the function body
        /// </summary>
        /// <param name = "plant">is a Plant type object.</param>
        /// <returns>returns a plant type object.</returns>
        /// <exception cref="DbUpdateException">Thrown if an error occurs during the database update, such as a constraint violation.</exception>
        /// <exception cref="DbUpdateConcurrencyException">Thrown if a concurrency conflict is detected during the database update.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<Plant> AddPlantAsync(PlantRequestDto plant)
        {
            // Guarda a imagem no servidor
            String imagePath = "";
            if(plant.PlantImage != null)
            {
                imagePath = await _images.SaveImageAsync(plant.PlantImage);
            }


            Plant newPlant = new Plant
            {
                Name = plant.Name,
                Type = plant.Type,
                ExpSuggested = plant.ExpSuggested,
                LuminosityNeeded = plant.LuminosityNeeded,
                WaterNeeds = plant.WaterNeeds,
                Description = plant.Description,
                PlantImage = imagePath,
                AdminID = plant.AdminID,
            };

            _context.Plant.Add(newPlant);
            await _context.SaveChangesAsync();
            return newPlant;
        }


        /// <UpdatePlantAsync>
        /// This method should update a plant entry in the DB.
        /// </summary>
        /// <param name = "updatedPlant">is a Plant type object.</param>
        /// <param name = "id">is the id of the plant we wish to update.</param>
        /// <returns>returns the updated plant.</returns>
        /// <exception cref="ArgumentNullException">Thrown if updatedPlant is null.</exception>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="DbUpdateConcurrencyException">Thrown if a concurrency conflict occurs while saving changes.</exception>
        /// <exception cref="DbUpdateException">Thrown if an error occurs while updating the database.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        /// 
        public async Task<bool> UpdatePlantAsync(int id, PlantRequestDto updatedPlant)
        {
            // Find plant to be updated by it's ID
            var dbPlant = await _context.Plant.FindAsync(id);

            if (dbPlant == null) return false; // if the plant isn't found, return false.

            // Store the current image path before potentially updating
            string oldImagePath = dbPlant.PlantImage;

            // Se uma nova imagem for fornecida, guarda-a
            if (updatedPlant.PlantImage != null)
            {
                var imagePath = await _images.SaveImageAsync(updatedPlant.PlantImage);

                if (imagePath == null)
                    throw new Exception("Image upload failed.");

                // Atualiza o caminho da imagem
                dbPlant.PlantImage = imagePath;

                // Delete the old image if it exists
                if (!string.IsNullOrEmpty(oldImagePath))
                {
                    // Extract just the filename from the full path
                    string oldFileName = Path.GetFileName(oldImagePath);
                    _images.DeleteImage(oldFileName);
                }

            }

            // change the values of the plant found to the values of the updated plant.
            dbPlant.Name = updatedPlant.Name;
            dbPlant.Type = updatedPlant.Type;
            dbPlant.ExpSuggested = updatedPlant.ExpSuggested;
            dbPlant.WaterNeeds = updatedPlant.WaterNeeds;
            dbPlant.LuminosityNeeded = updatedPlant.LuminosityNeeded;
            dbPlant.Description = updatedPlant.Description;

            // Save changes in the DB
            await _context.SaveChangesAsync();

            return true;
        }


        /// <DeletePlantAsync>
        /// This method should delete a plant entry in the DB.
        /// </summary>
        /// <param name = "id">is the id of the plant we wish to delete.</param>
        /// <returns>returns a boolean value representing if the operation was succesful or failed.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="DbUpdateConcurrencyException">Thrown if a concurrency conflict occurs while deleting.</exception>
        /// <exception cref="DbUpdateException">Thrown if an error occurs while deleting from the database.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<bool> DeletePlantAsync(int id)
        {
            var plant = await _context.Plant.FindAsync(id);
            if (plant == null) return false;

            string oldImagePath = plant.PlantImage;
            // Delete the old image if it exists
            if (!string.IsNullOrEmpty(oldImagePath))
            {
                // Extract just the filename from the full path
                string oldFileName = Path.GetFileName(oldImagePath);
                _images.DeleteImage(oldFileName);
            }


            _context.Plant.Remove(plant);
            await _context.SaveChangesAsync();
            return true;
        }

        /// <GetTotalPlantsCountAsync>
        /// This method should return an int value representing the count of plants in the DB.
        /// </summary>
        /// <returns>number of Plant elements in the DB.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        /// <exception cref="DbUpdateException">Thrown if an error occurs while accessing the database.</exception>

        public async Task<int> GetTotalPlantsCountAsync()
        {
            return await _context.Plant.CountAsync();
        }


        private bool SortFieldExists(string sortField)
        {
            // Use BindingFlags to perform a case-insensitive search for the property
            PropertyInfo? property = typeof(Plant).GetProperty(sortField, BindingFlags.IgnoreCase | BindingFlags.Public | BindingFlags.Instance);
            return property != null;
        }
    }
}
