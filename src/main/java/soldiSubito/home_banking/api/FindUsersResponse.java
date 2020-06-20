package soldiSubito.home_banking.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.config.PropertyVisibilityStrategy;

public class FindUsersResponse {

	private final List<UserApi> userApiList;
	
	
	public FindUsersResponse(List<UserApi> userApiList) {
		this.userApiList = userApiList;

	}
	
	
	public List<UserApi> getUserApiList() {
		return userApiList;
	}
	
	
	public String toJson() {

		Jsonb jsonb = JsonbBuilder.create();
		String result = jsonb.toJson(this.userApiList);
		return result;

	}

}
