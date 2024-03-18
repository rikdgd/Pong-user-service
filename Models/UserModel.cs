namespace Models;

public class UserModel
{
    public int? _id { get; private set; }
    public string? _name { get; private set; }
    public string? _password { get; private set; }
    
    
    public UserModel()
    {
        
    }
    
    public UserModel(int id, string name, string password)
    {
        _id = id;
        _name = name;
        _password = password;
    }
}
