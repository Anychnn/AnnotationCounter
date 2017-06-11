package grammer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Anyang on 2017/6/11.
 */
public class StateNode {
    public String name;             //状态名
    public Map<Condition, StateNode> pathTo; //路径

    public StateNode(String name) {
        this.name = name;
    }

    public void add(Condition condition,StateNode node){
        if(pathTo==null){
            pathTo=new LinkedHashMap<>();
        }
        pathTo.put(condition,node);
    }
}
