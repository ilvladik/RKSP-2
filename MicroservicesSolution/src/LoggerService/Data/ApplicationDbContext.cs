using LoggerService.Models;
using Microsoft.EntityFrameworkCore;

namespace LoggerService.Data
{
    public class ApplicationDbContext : DbContext
    {
        public DbSet<Log> Logs { get; set; }

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options) 
        {
            Database.EnsureCreated();
        }
    }
}
