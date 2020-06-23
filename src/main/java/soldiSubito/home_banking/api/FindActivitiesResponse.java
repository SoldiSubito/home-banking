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

import soldiSubito.home_banking.entity.Activity;

public class FindActivitiesResponse  {

	private final List<Activity> activityList;
	
	
	public FindActivitiesResponse(List<Activity> activityList) {
		this.activityList = activityList;

	}
	
	
	public List<Activity> getContoApiList() {
		return activityList;
	}
	
	
	public String toJson() {

		Jsonb jsonb = JsonbBuilder.create();
		String result = jsonb.toJson(this.activityList);
		return result;

	}

}