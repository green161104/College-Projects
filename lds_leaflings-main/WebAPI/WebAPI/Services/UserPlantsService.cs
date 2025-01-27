using Microsoft.EntityFrameworkCore;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.exceptions;


namespace WebAPI.Services
{
    public class UserPlantsService
    {
        private readonly DataContext _context;

        public UserPlantsService(DataContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Retrieves all user-plant associations.
        /// </summary>
        /// <returns>A list of UserPlant objects including associated plants and users.</returns>
        public async Task<List<UserPlant>> GetAllUserPlantsAsync()
        {
            return await _context.UserPlant
                .Include(up => up.Plant)  // Inclui os dados da planta
                .Include(up => up.User)   // Inclui os dados do utilizador
                .ToListAsync();
        }

        /// <summary>
        /// Retrieves all plants associated with a specific user.
        /// </summary>
        /// <param name="userId">The ID of the user whose plants are to be retrieved.</param>
        /// <returns>A list of UserPlant objects for the specified user.</returns>
        public async Task<List<UserPlant>> GetPlantsByUserIdAsync(int userId)
        {
            if (_context.User.Find(userId) != null)
            {
                return await _context.UserPlant
                .Include(up => up.Plant) // Inclui os dados da planta
                .Where(up => up.PersonID == userId)
                .ToListAsync();
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// Retrieves all users associated with a specific plant.
        /// </summary>
        /// <param name="plantId">The ID of the plant whose users are to be retrieved.</param>
        /// <returns>A list of UserPlant objects for the specified plant.</returns>
        public async Task<List<UserPlant>> GetUsersByPlantIdAsync(int plantId)
        {
            return await _context.UserPlant
                .Include(up => up.User) // Inclui os dados do utilizador
                .Include(up => up.Plant) // Inclui os dados da planta
                .Where(up => up.PlantID == plantId)
                .ToListAsync();
        }

        /// <summary>
        /// Adds a new user-plant association.
        /// </summary>
        /// <param name="userPlant">The UserPlantDto object containing the user and plant IDs.</param>
        /// <returns>The newly created UserPlant object.</returns>
        public async Task<UserPlant?> AddUserPlantAsync(UserPlantRequestDto userPlant)
        {
            User? user = await _context.User.FindAsync(userPlant.UserId);
            Plant? plant = await _context.Plant.FindAsync(userPlant.PlantId);

            if (user == null || plant == null)
            {
                return null;
            }

            UserPlant? existentUserPlant = await _context.UserPlant
                .FirstOrDefaultAsync(up => up.PlantID == plant.Id && up.PersonID == user.Id);
            if(existentUserPlant != null)
            {
                throw new Exception("Plant already added to user");
            }

            if (!user.RolePaid && await CountUserPlantsAsync(user) == 3)
            {
                throw new NonPaidUserException("Free users can only have 3 plants associated");
            }
            else
            {
                UserPlant newUserPlant = new UserPlant
                {
                    PlantID = userPlant.PlantId,
                    PersonID = userPlant.UserId
                };

                _context.UserPlant.Add(newUserPlant);
                await _context.SaveChangesAsync();
                return newUserPlant; // Retorna a associação criada
            }
        }



        /// <summary>
        /// Deletes the association between a user and a plant.
        /// </summary>
        /// <param name="userId">The ID of the user.</param>
        /// <param name="plantId">The ID of the plant.</param>
        /// <returns>True if the deletion was successful; otherwise, false.</returns>
        public async Task<bool> DeleteUserPlantAsync(int userId, int plantId)
        {
            var userPlant = await _context.UserPlant
                .FirstOrDefaultAsync(up => up.PersonID == userId && up.PlantID == plantId);

            if (userPlant == null) return false;

            _context.UserPlant.Remove(userPlant);
            await _context.SaveChangesAsync();

            return true;
        }

        /// <summary>
        /// Counts how many plants are associated with a specific user
        /// </summary>
        /// <param name="user"></param>
        /// <returns>The number of plants</returns>
        public async Task<int> CountUserPlantsAsync(User user)
        {
            return await _context.UserPlant.CountAsync(up => up.PersonID == user.Id);
        }

    }


}

