package me.stevenkin.boomvc.mvc.kit;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonKitTest {

    @Test
    public void toJson() {
        System.out.println(json());
    }

    @Test
    public void fromJson() {
        Type type = new TypeToken<List<?>>(){}.getType();
        Object o = JsonKit.fromJson(json(), type);
        System.out.println(o);
    }

    String json(){
        Type type = new TypeToken<List<User>>(){}.getType();
        Result<User> result = new Result<>();
        result.setCode(0);
        result.setMessage("hello gson");
        result.setData1(new User("test","test",0));
        List<User> userList = new ArrayList<>();
        userList.add(new User("test1","test1",1));
        userList.add(new User("test2","test2",2));
        userList.add(new User("test3","test3",3));
        result.setData(userList);
        String json = JsonKit.toJson(userList, type);
        //System.out.println(json);
        return json;
    }
}
