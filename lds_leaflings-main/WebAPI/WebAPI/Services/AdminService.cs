using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;
using System.Reflection;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Utils;

namespace WebAPI.Services
{
    public class AdminService
    {
        private readonly DataContext _context;
        private readonly Cryptography _cryptography;

        public AdminService(DataContext context, Cryptography cryptography)
        {
            _context = context;
            _cryptography = cryptography;
        }

        public async Task<List<Admin>> GetAllAdminsAsync(int start = 0, int end = 10, string sortField = "id", string sortOrder = "ASC")
        {
            sortField = char.ToUpper(sortField[0]) + sortField.Substring(1); // Capitalize the sortField
            sortOrder = sortOrder.ToUpper(); // Convert sortOrder to uppercase

            bool sortFieldExists = SortFieldExists(sortField);

            IQueryable<Admin> query = _context.Admin
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

        public async Task<Admin?> GetAdminByIdAsync(int id)
        {
            if( id < 1)
            {
                return null;
            }

            return await _context.Admin.FirstOrDefaultAsync(a => a.Id == id);

        }

        public async Task<Admin> AddAdminAsync( AdminRequestDto adminDto)
        {
            if (!new EmailAddressAttribute().IsValid(adminDto.Email))
            {
                throw new ArgumentException("Invalid email format.");
            }

            // Password length validation
            if (string.IsNullOrWhiteSpace(adminDto.Password) || adminDto.Password.Length < 6)
            {
                throw new ArgumentException("Password must be at least 6 characters long.");
            }

            // Check if email is already in use
            var existingAdmin = await _context.Admin.FirstOrDefaultAsync(a => a.Email == _cryptography.HashString(adminDto.Email));
            if (existingAdmin != null)
            {
                throw new InvalidOperationException("Email already in use.");
            }

            var newAdmin = new Admin
            {
                Username = adminDto.Username,
                Email = _cryptography.HashString(adminDto.Email),
                Password = _cryptography.HashString(adminDto.Password),
                Role = 1,
                Contact = adminDto.Contact,
            };

            _context.Admin.Add(newAdmin);
            await _context.SaveChangesAsync();

            return newAdmin;
        }

        public async Task<Admin?> UpdateAdminAsync(int id, UpdateAdminDto adminDto)
        {
            var existingAdmin = await _context.Admin.FindAsync(id);
            if (existingAdmin == null) return null;


            existingAdmin.Username = adminDto.Username;
            existingAdmin.Contact = adminDto.Contact;

            await _context.SaveChangesAsync();

            return existingAdmin;
        }


        public async Task<bool> DeleteAdminAsync(int id)
        {
            if(id < 1) return false;

            var admin = await _context.Admin.FindAsync(id);
            if (admin == null) return false;

            _context.Admin.Remove(admin);
            await _context.SaveChangesAsync();

            return true;
        }

        public async Task<int> GetTotalAdminsCountAsync()
        {
            return await _context.Admin.CountAsync();
        }


        private bool SortFieldExists(string sortField)
        {
            // Use BindingFlags to perform a case-insensitive search for the property
            PropertyInfo? property = typeof(Admin).GetProperty(sortField, BindingFlags.IgnoreCase | BindingFlags.Public | BindingFlags.Instance);
            return property != null;
        }

    }
}
