package me.stevenkin.boomvc.mvc.mapping;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.mvc.mapping.condition.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteMappingInfo implements Condition {

    private List<Condition> conditionList = new ArrayList<>();

    @Override
    public boolean determine(HttpRequest request) {
        for(Condition condition : this.conditionList){
            if(!condition.determine(request))
                return false;
        }
        return true;
    }

    public RouteMappingInfo condition(Condition condition){
        this.conditionList.add(condition);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteMappingInfo that = (RouteMappingInfo) o;
        return Objects.equals(conditionList, that.conditionList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(conditionList);
    }
}
