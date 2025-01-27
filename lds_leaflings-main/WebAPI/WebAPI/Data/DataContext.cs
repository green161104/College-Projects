using Microsoft.EntityFrameworkCore;
using System.Drawing;
using WebAPI.DTO;
using WebAPI.Entity;
using WebAPI.Utils;

namespace WebAPI.Data
{
    public class DataContext : DbContext
    {
        private readonly Cryptography _cryptography;
        public DataContext(DbContextOptions<DataContext> options, Cryptography cryptography) : base(options)
        {
            _cryptography = cryptography;
        }


        // Tabelas da Base de Dados

        public DbSet<Person> Person { get; set; }  
        public DbSet<Admin> Admin { get; set; }
        public DbSet<User> User { get; set; } // Tabela de utilizadores
        public DbSet<Ad> Ad { get; set; }
        public DbSet<Entity.PlantTask> Task { get; set; } // Tabela de tarefas
        public DbSet<Plant> Plant { get; set; } // Tabela de plantas
        public DbSet<Warning> Warning { get; set; }
        public DbSet<UserPlant> UserPlant { get; set; } // Tabela de junção para a relação muitos-para-muitos
        public DbSet<Payment> Payment { get; set; }
        public DbSet<Diary> Diary { get; set; }
        public DbSet<Log> Log { get; set; }


        // Configuração de mapeamento de entidade
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {


            // Set up TPH or TPT inheritance
            modelBuilder.Entity<Person>()
                .ToTable("Persons"); // Single table if using TPH

            modelBuilder.Entity<Admin>()
                .ToTable("Admins"); // Separate table if using TPT

            // Adicionar admin inicial
            modelBuilder.Entity<Admin>().HasData(
                new Admin
                {
                    Id = 1, // Defina um ID inicial
                    Username = "Admin",
                    Email = _cryptography.HashString("admin@admin.com"),
                    Password = _cryptography.HashString("adminPassword"), 
                    Contact = "913456654",
                    Role = 1
                }
            );

            modelBuilder.Entity<User>()
                .ToTable("Users"); // Separate table if using TPT


            modelBuilder.Entity<Person>()
                .HasIndex(u => u.Email)
                .IsUnique();


            base.OnModelCreating(modelBuilder);

            // Configuração dos enums para User
            modelBuilder.Entity<User>()
                .Property(u => u.CareExperience)
                .HasConversion<string>();

            modelBuilder.Entity<User>()
                .Property(u => u.WaterAvailability)
                .HasConversion<string>();

            modelBuilder.Entity<User>()
                .Property(u => u.LuminosityAvailability)
                .HasConversion<string>();


            // Configurações de enum para Plant
            modelBuilder.Entity<Plant>()
                .Property(p => p.Type)
                .HasConversion<string>();

            modelBuilder.Entity<Plant>()
                .Property(p => p.ExpSuggested)
                .HasConversion<string>();

            modelBuilder.Entity<Plant>()
             .HasOne(p => p.Admin) // Each Plant has one Admin
             .WithMany(a => a.Plants) // An Admin can have many Plants
             .HasForeignKey(p => p.AdminID) // Foreign key in Plant
             .OnDelete(DeleteBehavior.Restrict); // Prevent cascade delete when Admin is deleted



            // Relacionamento entre Task e (Admin e Plantas)
            modelBuilder.Entity<Entity.PlantTask>()
                        .HasKey(t => t.Id);

            modelBuilder.Entity<Entity.PlantTask>()
                .HasOne(t => t.Admin)
                .WithMany(a => a.Tasks)
                .HasForeignKey(t => t.AdminId) 
                .OnDelete(DeleteBehavior.Restrict); 

            modelBuilder.Entity<Entity.PlantTask>()
                .HasOne(t => t.Plant)
                .WithMany(p => p.Tasks)
                .HasForeignKey(t => t.PlantId) 
                .OnDelete(DeleteBehavior.Cascade);


            modelBuilder.Entity<UserPlant>()
                .HasOne(up => up.User)
                .WithMany(u => u.UserPlants)
                .HasForeignKey(up => up.PersonID)
                .OnDelete(DeleteBehavior.Cascade); 

            modelBuilder.Entity<UserPlant>()
                .HasOne(up => up.Plant)
                .WithMany(p => p.UserPlants)
                .HasForeignKey(up => up.PlantID)
                .OnDelete(DeleteBehavior.Cascade); 

            // Define a unique constraint on PersonID and PlantID in UserPlant
            modelBuilder.Entity<UserPlant>()
                .HasIndex(up => new { up.PersonID, up.PlantID })
                .IsUnique();

            // Definir userId e PlantId como unique
            modelBuilder.Entity<UserPlant>()
            .HasIndex(up => new { up.PersonID, up.PlantID })
            .IsUnique();
            
        }
    }
}
