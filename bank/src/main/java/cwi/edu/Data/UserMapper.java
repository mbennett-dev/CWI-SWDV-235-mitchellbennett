package cwi.edu.Data;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Select("SELECT user_id, username, '' as password, email, first_name, last_name, role FROM users")
    List<User> getAllUsers();

    @Select("SELECT user_id, username, email, first_name, last_name, role FROM users WHERE user_id = #{id}")
    User getById(int id);

    @Insert("INSERT INTO users (username, password, email, first_name, last_name, role) VALUES (#{username}, #{password}, #{email}, #{firstName}, #{lastName}, #{role})") 
    @Options(useGeneratedKeys = true, keyProperty = "userId") 
    int insert(User user);

    @Update("UPDATE users SET username = #{username}, password = #{password}, email = #{email}, first_name = #{firstName}, last_name = #{lastName}, role = #{role} WHERE user_id = #{userId}") 
    int update(User user);
    
    @Delete("DELETE FROM users WHERE user_id = #{id}") 
    int delete(int id);

    @Select("SELECT user_id, username, password, email, first_name, last_name, role " +
        "FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
}