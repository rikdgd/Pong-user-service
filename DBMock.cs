using Models;

public class DBMock 
{
    private List<UserModel> _store;
    
    
    public DBMock()
    {
        _store = new List<UserModel>();
    }
    public DBMock(List<UserModel> users)
    {
        _store = [.. users];
    }
    
    public UserModel GetUserById(int id)
    {
        return new UserModel(id, "Ron", "notsecure");
    }
}