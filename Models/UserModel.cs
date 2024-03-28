namespace Pong_user_service.Models;

public class UserModel
{
    public int? Id { get; private set; }
    public string? Name { get; private set; }
    public string? Password { get; private set; }
    
    
    public UserModel()
    {
        
    }
    
    public UserModel(int id, string name, string password)
    {
        Id = id;
        Name = name;
        Password = password;
    }
}
