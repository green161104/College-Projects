using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using System.ComponentModel;
using System.Reflection;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Utils;

namespace WebAPI.Services
{
    public class AdService
    {

        private readonly DataContext _context;        //DB context
        private readonly Images _images;

        public AdService(DataContext context, IWebHostEnvironment env)        //Constructor with the DB context
        {
            _context = context;
            _images = new Images(env);
        }

        public AdService(DataContext context)        //Constructor with the DB context
        {
            _context = context;
        }


        /** TODO:
       *   GETALL - done
        *  GETBYID - done 
        *  GetStart - done
        *  GetEnd -  done 
        *  GetCreator - done
        *  UPDATE - done
        *  ADD - done
        *  DELETE - done
        *  getAdCount - done
        */



        /// <GetallAds>
        /// Retrieves a list of AdDto objects.
        /// </summary>
        /// <returns>The method returns a list of all Ads within the database.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>

        public async Task<List<Ad>> GetAllAds(int start = 0, int end = 10, string sortField = "id", string sortOrder = "ASC")
        {
            sortField = char.ToUpper(sortField[0]) + sortField.Substring(1); // Capitalize the sortField
            sortOrder = sortOrder.ToUpper(); // Convert sortOrder to uppercase

            bool sortFieldExists = SortFieldExists(sortField);

            IQueryable<Ad> query = _context.Ad
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


        /// <getAdByID>
        /// Retrieves an AdDto object by its unique identifier.
        /// </summary>
        /// <param name="id">The unique identifier of the Ad entry.</param>
        /// <returns>The AdDto object with the specified ID, or null if not found.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<Ad?> GetAdByID(int id)
        {
            return await _context.Ad.FindAsync(id);
        }


        /// <getAdStart>
        /// Retrieves an AdDto object's Start date.
        /// </summary>
        /// <param name="id">The unique identifier of the Ad entry.</param>
        /// <returns>The AdDto object's startDate with the specified ID.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        ///  <exception cref="KeyNotFoundException">Thrown if the ID isn't found.</exception>
        public async Task<DateTime> GetAdStart(int id)
        {
            Ad? ad = await _context.Ad.FindAsync(id);

            if (ad != null)
            {
                return ad.StartDate;
            }
            else
            {
                throw new KeyNotFoundException($"Ad with ID number {id} was not found.");
            }
        }

        /// <getAdEnd>
        /// Retrieves an AdDto object's End date.
        /// </summary>
        /// <param name="id">The unique identifier of the Ad entry.</param>
        /// <returns>The AdDto object's EndDate with the specified ID.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        /// <exception cref="KeyNotFoundException">Thrown if the ID isn't found.</exception>
        public async Task<DateTime> GetAdEnd(int id)
        {
            Ad? ad = await _context.Ad.FindAsync(id);

            if (ad != null)
            {
                return ad.EndDate;
            }
            else
            {
                throw new KeyNotFoundException($"Ad with ID number {id} was not found.");
            }
        }

        /// <getAdCreator>
        /// Retrieves an AdDto object's Admin creator id.
        /// </summary>
        /// <param name="id">The unique identifier of the Ad entry.</param>
        /// <returns>The AdDto object's EndDate with the specified ID.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        ///  <exception cref="KeyNotFoundException">Thrown if the ID isn't found.</exception>

        public async Task<int> GetAdCreator(int id)
        {
            Ad? ad = await _context.Ad.FindAsync(id);

            if (ad != null)
            {
                return ad.AdminID;
            }
            else
            {
                throw new KeyNotFoundException($"Ad with ID number {id} was not found.");
            }

        }

        /// <summary>
        /// Retrieves a random active advertisement from the database.
        /// </summary>
        /// <returns>
        /// A random active <see cref="Ad"/> if there are active advertisements available, 
        /// or <c>null</c> if no active advertisements are found.
        /// </returns>
        /// <remarks>
        /// This method first filters the advertisements to include only those marked as active 
        /// (where <see cref="Ad.isActive"/> is <c>true</c>). If no active advertisements are available, 
        /// it returns <c>null</c>. Otherwise, it selects one random advertisement from the filtered list.
        /// </remarks>
        /// <example>
        /// Example usage:
        /// <code>
        /// var ad = await _adService.GetRandomActiveAd();
        /// if (ad != null)
        /// {
        ///     Console.WriteLine($"Ad ID: {ad.Id}, Message: {ad.Message}");
        /// }
        /// else
        /// {
        ///     Console.WriteLine("No active ads available.");
        /// }
        /// </code>
        /// </example>
        /// <exception cref="Exception">
        /// Does not explicitly throw exceptions, but may propagate exceptions from the database query.
        /// Ensure proper exception handling when calling this method.
        /// </exception>
        public async Task<Ad?> GetRandomActiveAd()
        {
            var activeAds = await _context.Ad
                .Where(ad => ad.isActive == true)
                .OrderBy(r => Guid.NewGuid())
                .ToListAsync();

            if (activeAds.Count == 0)
            {
                return null;
            }
            var random = new Random();
            int index = random.Next(activeAds.Count);
            return activeAds[index];
        }



        /// <UpdateAdAsync>
        /// This method should update an Ad entry in the DB.
        /// </summary>
        /// <param name = "ad">is an Ad type object.</param>
        /// <param name = "id">is the id of the Ad we wish to update.</param>
        /// <returns>returns a bool value representing if the operation was a success.</returns>
        /// <exception cref="ArgumentNullException">Thrown if updatedPlant is null.</exception>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="DbUpdateConcurrencyException">Thrown if a concurrency conflict occurs while saving changes.</exception>
        /// <exception cref="DbUpdateException">Thrown if an error occurs while updating the database.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        /// 
        public async Task<Ad?> UpdateAdAsync(int id, AdRequestDto ad)
        {
            Ad? oldAd = await _context.Ad.FindAsync(id);

            if (oldAd == null) return null;

            // Store the current image path before potentially updating
            string oldImagePath = oldAd.AdFile;

            // Se uma nova imagem for fornecida, guarda-a
            if (ad.AdFile != null)
            {
                var imagePath = await _images.SaveImageAsync(ad.AdFile);

                if (imagePath == null)
                    throw new Exception("Image upload failed.");

                // Atualiza o caminho da imagem
                oldAd.AdFile = imagePath;

                // Delete the old image if it exists
                if (!string.IsNullOrEmpty(oldImagePath))
                {
                    // Extract just the filename from the full path
                    string oldFileName = Path.GetFileName(oldImagePath);
                    _images.DeleteImage(oldFileName);
                }

            }

            oldAd.AdminID = ad.AdminId;
            oldAd.StartDate = ad.StartDate;
            oldAd.EndDate = ad.EndDate;
            oldAd.isActive = ad.isActive;

            await _context.SaveChangesAsync();

            return oldAd;
        }

        /// <addAd>
        /// This method will add an ad type object to the table Ad in the DB.
        /// the possible exceptions are thrown by the saveChangesAsync method used in the function body.
        /// </summary>
        /// <param name = "ad">is an AdDto type object.</param>
        /// <returns>returns an AdDto type object.</returns>
        /// <exception cref="DbUpdateException">Thrown if an error occurs during the database update, such as a constraint violation.</exception>
        /// <exception cref="DbUpdateConcurrencyException">Thrown if a concurrency conflict is detected during the database update.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        public async Task<Ad> AddAd(AdRequestDto ad)
        {

            // Guarda a imagem no servidor
            String imagePath = "";

            if (ad.AdFile != null)
            {
                imagePath = await _images.SaveImageAsync(ad.AdFile);

                if (imagePath == null)
                    throw new Exception("Image upload failed.");
            }

            Ad newAd = new Ad
            {
                AdFile = imagePath,
                AdminID = ad.AdminId,
                StartDate = ad.StartDate,
                EndDate = ad.EndDate,
                isActive = ad.isActive,
            };

            _context.Ad.Add(newAd);
            await _context.SaveChangesAsync();
            return newAd;
        }

        /// <deleteAdAsync>
        /// This method should delete an Ad entry in the DB.
        /// </summary>
        /// <param name = "id">is the id of the Ad we wish to delete.</param>
        /// <returns>returns a boolean value representing if the operation was succesful or failed.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="DbUpdateConcurrencyException">Thrown if a concurrency conflict occurs while deleting.</exception>
        /// <exception cref="DbUpdateException">Thrown if an error occurs while deleting from the database.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>

        public async Task<Boolean> deleteAdAsync(int id)
        {
            var Ad = await _context.Ad.FindAsync(id);  
            if (Ad == null) return false;

            string oldImagePath = Ad.AdFile;
            // Delete the old image if it exists
            if (!string.IsNullOrEmpty(oldImagePath))
            {
                // Extract just the filename from the full path
                string oldFileName = Path.GetFileName(oldImagePath);
                _images.DeleteImage(oldFileName);
            }

            _context.Ad.Remove(Ad);
            await _context.SaveChangesAsync();
            return true;
        }


        /// <GetAdCount>
        /// This method should return an int value representing the count of Ads present in the DB.
        /// </summary>
        /// <returns>number of Ad elements in the DB.</returns>
        /// <exception cref="InvalidOperationException">Thrown if there is an issue with the database context configuration.</exception>
        /// <exception cref="TaskCanceledException">Thrown if the asynchronous operation is canceled.</exception>
        /// <exception cref="DbUpdateException">Thrown if an error occurs while accessing the database.</exception>

        public async Task<int> GetAdCount()
        {
            return await _context.Ad.CountAsync(); 
        }

        public async Task<int> GetTotalAdsCountAsync()
        {
            return await _context.Ad.CountAsync();
        }


        private bool SortFieldExists(string sortField)
        {
            // Use BindingFlags to perform a case-insensitive search for the property
            PropertyInfo? property = typeof(Ad).GetProperty(sortField, BindingFlags.IgnoreCase | BindingFlags.Public | BindingFlags.Instance);
            return property != null;
        }
    }

}
