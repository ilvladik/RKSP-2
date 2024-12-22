namespace LoggerService.Models
{
    public class Log
    {
        public Guid Id { get; set; }

        public string Info { get; set; }

        public DateTime Time { get; set; }

        public Log(string info)
        {
            Id = Guid.NewGuid();
            Info = info;
            Time = DateTime.UtcNow;
        }
    }
}
