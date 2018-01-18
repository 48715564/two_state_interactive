package com.cn.common.service;

import com.cn.common.utils.OSClientV3Factory;
import com.cn.common.utils.XClarityApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jclouds.json.Json;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.telemetry.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.apache.coyote.http11.Constants.a;

/**
 * Created by bozhou on 2017/12/18.
 */
@Service
public class XClarityService {
    @Autowired
    XClarityApi xClarityApi;

    private Map<String, JSONObject> serverMemoryStore = Maps.newHashMap(); //服务器
    private Map<String, JSONObject> storageMemoryStore = Maps.newHashMap();//存储
    private Map<String, JSONObject> switchMemoryStore = Maps.newHashMap();//交换机
    private Map<String, JSONObject> chassisMemoryStore = Maps.newHashMap();//机箱
    private Map<String, JSONObject> complexMemoryStore = Maps.newHashMap();
    private Map<String, JSONObject> rackMemoryStore = Maps.newHashMap();//机架

    /**
     *  reportData.critical = dataStore.query({_statusIndex: 0}).length;
     reportData.warning = dataStore.query({_statusIndex: 1}).length;
     reportData.offline = dataStore.query({_statusIndex: 2}).length;
     reportData.unknown = dataStore.query({_statusIndex: 3}).length;
     reportData.pending = dataStore.query({_statusIndex: 4}).length;
     reportData.ok = dataStore.query({_statusIndex: 6}).length;
     * @return
     */
    public JSONArray hardwareList(){
        _onFinishInitializeInventory(xClarityApi.cabinetApi());
        //根据状态查询数量
        JSONObject serverStore = getCount(serverMemoryStore,"server");
        JSONObject storageStore = getCount(storageMemoryStore,"storage");
        JSONObject switchStore = getCount(switchMemoryStore,"switch");
        JSONObject chassisStore = getCount(serverMemoryStore,"chassis");
        JSONObject rackStore = getCount(serverMemoryStore,"rack");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(serverStore);
        jsonArray.add(storageStore);
        jsonArray.add(switchStore);
        jsonArray.add(chassisStore);
        jsonArray.add(rackStore);
        return jsonArray;
    }

    private JSONObject getCount(Map<String, JSONObject> data,String name){
        int allCount = data.size();
        int criticalCount = 0;
        int warningCount = 0;
        int offlineCount = 0;
        int unknownCount = 0;
        int pendingCount = 0;
        int informationalCount = 0;
        int okCount = 0;
        for (JSONObject value : data.values()) {
            switch (value.getInt("_statusIndex")){
                case 0:criticalCount++;break;
                case 1:warningCount++;break;
                case 2:offlineCount++;break;
                case 3:unknownCount++;break;
                case 4:pendingCount++;break;
                case 5:informationalCount++;break;
                case 6:okCount++;break;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("allCount",allCount);
        jsonObject.put("criticalCount",criticalCount);
        jsonObject.put("warningCount",warningCount);
        jsonObject.put("offlineCount",offlineCount);
        jsonObject.put("unknownCount",unknownCount);
        jsonObject.put("pendingCount",pendingCount);
        jsonObject.put("informationalCount",informationalCount);
        jsonObject.put("okCount",okCount);
        return jsonObject;
    }

    public void _onFinishInitializeInventory(JSONObject jsonObject) {
        int b, c, d, e;
        JSONArray jsonArray = jsonObject.getJSONArray("cabinetList");
        for (b = 0; b < jsonArray.size(); b++) {
            JSONObject cabinetNode = (JSONObject) jsonArray.get(b);
            for (d = 0; d < cabinetNode.getJSONArray("nodeList").size(); d++) {
                _processRackNode(serverMemoryStore, cabinetNode, d);
            }
            for (c = 0; c < cabinetNode.getJSONArray("chassisList").size(); c++) {
                JSONObject chassisNode = (JSONObject) cabinetNode.getJSONArray("chassisList").get(c);
                for (d = 0; d < chassisNode.getJSONObject("itemInventory").getJSONArray("nodes").size(); d++) {
                    JSONObject node = (JSONObject) chassisNode.getJSONObject("itemInventory").getJSONArray("nodes").get(d);
                    if ("SCU".equals(node.getStr("type"))) {
                        _processChassisStorage(storageMemoryStore, cabinetNode, c, d);
                    } else {
                        _processChassisServer(serverMemoryStore, cabinetNode, c, d);
                    }
                }
                for (d = 0; d < ((JSONObject) cabinetNode.getJSONArray("chassisList").get(c)).getJSONObject("itemInventory").getJSONArray("switches").size(); d++) {
                    _processChassisSwitch(switchMemoryStore, cabinetNode, c, d);
                }
                _processChassis(chassisMemoryStore, cabinetNode, c);
            }
            for (d = 0; d < cabinetNode.getJSONArray("switchList").size(); d++) {
                _processRackSwitch(chassisMemoryStore, cabinetNode, d);
            }
            for (c = 0; c < cabinetNode.getJSONArray("complexList").size(); c++) {
                JSONObject complexListNode = (JSONObject) cabinetNode.getJSONArray("complexList").get(c);
                for (d = 0; d < complexListNode.getJSONArray("orphanNodes").size(); d++) {
                    this._processComplexNode(serverMemoryStore, cabinetNode, c, "Orphan", d, null);
                }
                for (e = 0; e < complexListNode.getJSONArray("partition").size(); e++) {
                    JSONObject partitionNode = (JSONObject) complexListNode.getJSONArray("partition").get(e);
                    for (d = 0; d < partitionNode.getJSONArray("nodes").size(); d++) {
                        this._processComplexNode(serverMemoryStore, cabinetNode, c, "Partitioned", d, e);
                    }
                }
                this._processComplex(complexMemoryStore, cabinetNode, c);
            }
            for (d = 0; d < cabinetNode.getJSONArray("storageList").size(); d++) {
                _processRackStorage(storageMemoryStore, cabinetNode, d);
            }
            if (!"STANDALONE_OBJECT_UUID".equals(cabinetNode.getStr("UUID"))) {
                _processRack(rackMemoryStore, cabinetNode);
            }
        }
    }

    private void _processRack(Map<String, JSONObject> dataMap, JSONObject a) {
        a.put("name", a.getStr("cabinetName"));
        a.put("_statusIndex", getCabinetStatusIndex(dataMap,a));
        dataMap.put(a.getStr("UUID"), a);

    }

    private List<JSONObject> getChassisInRack(Map<String, JSONObject> dataMap,JSONObject rackItem) {
        List<JSONObject> rackChassis = Lists.newArrayList();
        JSONArray chassisList = rackItem.getJSONArray("chassisList");
        if (chassisList!=null && chassisList.size() > 0) {
            int len = chassisList.size();
            for (int i = 0; i < len; i++) {
                String uuid = ((JSONObject) chassisList.get(i)).getStr("itemUUID");
                if (dataMap.containsKey(uuid)) {
                    rackChassis.add(dataMap.get(uuid));
                }
            }
        }
        return rackChassis;
    }

    private List<JSONObject> getRackServersInRack(JSONObject rackItem) {
        List<JSONObject> rackServers = Lists.newArrayList();
        JSONArray nodeList = rackItem.getJSONArray("nodeList");
        if (nodeList!=null && nodeList.size() > 0) {
            int len = nodeList.size();
            for (int i = 0; i < len; i++) {
                String uuid = ((JSONObject)nodeList.get(i)).getStr("itemUUID");
                JSONObject serverEntry = serverMemoryStore.get(uuid);
                if (serverEntry!=null) rackServers.add(serverEntry);
            }
        }
        return rackServers;
    }

    private List<JSONObject> getRackSwitchesInRack(JSONObject rackItem) {
        List<JSONObject> rackSwitches = Lists.newArrayList();
        JSONArray switchList = rackItem.getJSONArray("switchList");
        if (switchList!=null && switchList.size() > 0) {
            int len = switchList.size();
            for (int i = 0; i < len; i++) {
                String uuid = ((JSONObject)switchList.get(i)).getStr("itemUUID");
                JSONObject switchEntry = switchMemoryStore.get(uuid);
                if (switchEntry!=null) rackSwitches.add(switchEntry);
            }
        }
        return rackSwitches;
    }
    //todo
    private int getCabinetStatusIndex(Map<String, JSONObject> dataMap,JSONObject cabinetObj) {
        List<JSONObject> componentList = Lists.newArrayList();
        List<JSONObject> chassisList = getChassisInRack(dataMap,cabinetObj);
        for ( int i = 0; i < chassisList.size(); i++) { //for ( var i in chassisList) {
            componentList.add(chassisList.get(i));
        }
        List<JSONObject> serverList = getRackServersInRack(cabinetObj);
        for (int i = 0; i < serverList.size(); i++) {//i in serverList) {
            componentList.add(serverList.get(i));
        }
        List<JSONObject> switchList = getRackSwitchesInRack(cabinetObj);
        for (int i = 0; i < switchList.size(); i++) {//i in switchList) {
            componentList.add(switchList.get(i));
        }
        int statusIdx = _getStatusByString("Normal".toUpperCase());
        for (int i = 0;i<componentList.size();i++) {
            JSONObject node = componentList.get(i);
            Object itemInventory = node.get("itemInventory") ;
            if(itemInventory==null){
                itemInventory = componentList.get(i);
            }
            if (itemInventory instanceof JSONArray) {
                itemInventory = ((JSONArray)itemInventory).get(0);
            }
            Integer curIdx = Integer.valueOf(statusStrToIndex(itemInventory).toString());
            statusIdx = curIdx < statusIdx ? curIdx : statusIdx;
        }
        return statusIdx;
    }

    private void _processRackStorage(Map<String, JSONObject> dataMap, JSONObject a, int b) {
        JSONObject storageListNode = (JSONObject) a.getJSONArray("storageList").get(b);
        JSONObject c = new JSONObject(storageListNode.getJSONObject("itemInventory"));
        if (StringUtils.isNotBlank(c.getStr("uuid"))) {
            storageListNode.getJSONObject("itemInventory").clear();
            JSONObject location = c.getJSONObject("location");
            location.put("itemHeight", storageListNode.getInt("itemHeight"));
            location.put("itemLocation", storageListNode.getStr("itemLocation"));
            location.put("itemLocationRack", storageListNode.getStr("itemLocationRack"));
            location.put("itemLocationRoom", storageListNode.getStr("itemLocationRoom"));
            location.put("itemLowerUnit", storageListNode.getInt("itemLowerUnit"));
            c.put("_statusIndex", statusStrToIndex(c));
            c.put("_parent", new JSONObject(c.getJSONObject("parent")));
            putParent(a, c);
            c.getJSONObject("parent").put("type", "rack");
            c.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ? a.getStr("cabinetName") : "");
            dataMap.put(c.getStr("uuid"), c);
        }
    }

    private void _processComplex(Map<String, JSONObject> dataMap, JSONObject a, int b) {
        JSONObject complexListNode = (JSONObject) a.getJSONArray("complexList").get(b);
        JSONObject location = complexListNode.getJSONObject("location");
        location.put("itemHeight", a.getInt("height"));
        location.put("itemLocation", location.getStr("location"));
        location.put("itemLocationRack", location.getStr("rack"));
        location.put("itemLocationRoom", location.getStr("room"));
        location.put("itemLowerUnit", location.getInt("lowestRackUnit"));
        putParent(a, complexListNode.getJSONObject("parent"));
        dataMap.put(complexListNode.getStr("uuid"), complexListNode);
    }

    private void putParent(JSONObject a, JSONObject c) {
        if (c.getJSONObject("parent") == null) {
            String UUID = a.getStr("UUID");
            Map<String, String> tempMap = Maps.newHashMap();
            tempMap.put("uri", "cabinet/" + UUID);
            tempMap.put("uuid", UUID);
            c.put("parent", tempMap);
        }
    }

    private void _processComplexNode(Map<String, JSONObject> dataMap, JSONObject a, Integer _b, String _c, Integer d, Integer e) {
        JSONObject b = (JSONObject) a.getJSONArray("complexList").get(_b);
        JSONObject c = null;
        if ("Orphan".equals(_c)) {
            JSONObject orphanNodes = (JSONObject) b.getJSONArray("orphanNodes").get(d);
            c = new JSONObject(orphanNodes);
            if (StringUtils.isBlank(c.getStr("uuid"))) {
                return;
            }
            orphanNodes.clear();
            orphanNodes.put("uuid", c.getStr("uuid"));
            orphanNodes.put("name", c.getStr("name"));
            orphanNodes.put("partitionEnabled", c.getStr("partitionEnabled"));
            orphanNodes.put("partitionID", c.getStr("partitionID"));
        } else {
            JSONObject partitionNode = (JSONObject) b.getJSONArray("partition").get(d);
            c = new JSONObject(partitionNode);
            if (StringUtils.isBlank(c.getStr("uuid"))) {
                return;
            }
            partitionNode.clear();
            partitionNode.put("uuid", c.getStr("uuid"));
            partitionNode.put("name", c.getStr("name"));
            partitionNode.put("partitionEnabled", c.getStr("partitionEnabled"));
            partitionNode.put("partitionID", c.getStr("partitionID"));
        }
        putParent(a, c);
        c.put("_statusIndex", statusStrToIndex(c));
        dataMap.put(c.getStr("uuid"), c);
    }

    private void _processRackSwitch(Map<String, JSONObject> dataMap, JSONObject a, int b) {
        JSONObject switchListNode = (JSONObject) a.getJSONArray("switchList").get(b);
        JSONObject c = new JSONObject(switchListNode.getJSONObject("itemInventory"));
        if (StringUtils.isNotBlank(c.getStr("uuid"))) {
            switchListNode.getJSONObject("itemInventory").clear();
            c.getJSONObject("location").put("itemHeight", switchListNode.getInt("itemHeight"));
            c.getJSONObject("location").put("itemLocation", switchListNode.getStr("itemLocation"));
            c.getJSONObject("location").put("itemLocationRack", switchListNode.getStr("itemLocationRack"));
            c.getJSONObject("location").put("itemLocationRoom", switchListNode.getStr("itemLocationRoom"));
            c.getJSONObject("location").put("itemLowerUnit", switchListNode.getInt("itemLowerUnit"));
            c.put("_statusIndex", statusStrToIndex(c));
            putParent(a, c);
            c.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ? a.getStr("cabinetName") : "");
            dataMap.put(c.getStr("uuid"), c);
        }
    }

    ;

    private void _processChassis(Map<String, JSONObject> dataMap, JSONObject a, int b) {
        JSONObject jsonObject = (JSONObject) a.getJSONArray("chassisList").get(b);
        JSONObject d = new JSONObject(jsonObject.getJSONObject("itemInventory"));
        if (StringUtils.isNotBlank(d.getStr("uuid"))) {
            jsonObject.getJSONObject("itemInventory").clear();
            d.getJSONObject("location").put("itemHeight", jsonObject.getInt("itemHeight"));
            d.getJSONObject("location").put("itemLocation", jsonObject.getStr("itemLocation"));
            d.getJSONObject("location").put("itemLocationRack", jsonObject.getStr("itemLocationRack"));
            d.getJSONObject("location").put("itemLocationRoom", jsonObject.getStr("itemLocationRoom"));
            d.getJSONObject("location").put("itemLowerUnit", jsonObject.getInt("itemLowerUnit"));
            d.put("_statusIndex", statusStrToIndex(d));
            for (int c = 0; c < d.getJSONArray("fans").size(); c++) {
                JSONObject fansNode = (JSONObject) d.getJSONArray("fans").get(c);
                fansNode.put("_parentChassisName", d.getStr("name"));
            }
            for (int c = 0; c < d.getJSONArray("fanMuxes").size(); c++) {
                JSONObject fanMuxesNode = (JSONObject) d.getJSONArray("fanMuxes").get(c);
                fanMuxesNode.put("_parentChassisName", d.getStr("name"));
            }
            for (int c = 0; c < d.getJSONArray("cmms").size(); c++) {
                JSONObject cmmsNode = (JSONObject) d.getJSONArray("cmms").get(c);
                cmmsNode.put("_parentChassisName", d.getStr("name"));
            }
            for (int c = 0; c < d.getJSONArray("powerSupplies").size(); c++) {
                JSONObject powerSuppliesNode = (JSONObject) d.getJSONArray("powerSupplies").get(c);
                powerSuppliesNode.put("_parentChassisName", d.getStr("name"));
            }
            putParent(a, d);
            d.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ? a.getStr("cabinetName") : "");
            dataMap.put(d.getStr("uuid"), d);
        }
    }

    private void _processChassisSwitch(Map<String, JSONObject> dataMap, JSONObject a, int b, int c) {
        JSONObject d = ((JSONObject) a.getJSONArray("chassisList").get(b)).getJSONObject("itemInventory");
        JSONObject e = new JSONObject(d.getJSONArray("switches").get(c));
        if (StringUtils.isNotBlank(e.getStr("uuid"))) {
            JSONObject cnodes = (JSONObject) d.getJSONArray("switches").get(c);
            cnodes.clear();
            cnodes.put("uuid", e.getStr("uuid"));
            cnodes.put("name", e.getStr("name"));
            e.put("_parentChassisName", d.getStr("name"));
            e.put("_parentChassisLRU", ((JSONObject) a.getJSONArray("chassisList").get(b)).getStr("itemLowerUnit"));
            e.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ? a.getStr("cabinetName") : "");
            e.put("_statusIndex", statusStrToIndex(e));
//                    e._refreshIndex = t.refreshIndex,
            dataMap.put(e.getStr("uuid"), e);
        }
    }

    private void _processChassisStorage(Map<String, JSONObject> dataMap, JSONObject a, int b, int c) {
        JSONObject d = ((JSONObject) a.getJSONArray("chassisList").get(b)).getJSONObject("itemInventory");
        JSONObject tempB = new JSONObject(d.getJSONArray("nodes").get(c));
        if (StringUtils.isNotBlank(tempB.getStr("uuid"))) {
            JSONObject cnodes = (JSONObject) d.getJSONArray("nodes").get(c);
            cnodes.clear();
            cnodes.put("uuid", tempB.getStr("uuid"));
            cnodes.put("name", tempB.getStr("name"));
            tempB.put("_parentChassisName", d.getStr("name"));
            tempB.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ? a.getStr("cabinetName") : "");
            tempB.put("_statusIndex", statusStrToIndex(tempB));
            tempB.getJSONObject("parent").put("type", "chassis");
//            b._refreshIndex = t.refreshIndex,
            dataMap.put(tempB.getStr("uuid"), tempB);
        }
    }

    private void _processChassisServer(Map<String, JSONObject> dataMap, JSONObject a, int b, int c) {
        JSONObject jsonObject = (JSONObject) a.getJSONArray("chassisList").get(b);
        JSONObject d = jsonObject.getJSONObject("itemInventory"), e = new JSONObject(d.getJSONArray("nodes").get(c));
        if (StringUtils.isNotBlank(e.getStr("uuid"))) {
            JSONObject cnodes = (JSONObject) d.getJSONArray("nodes").get(c);
            cnodes.clear();
            cnodes.put("uuid", e.getStr("uuid"));
            cnodes.put("name", e.getStr("name"));
            e.put("_parentChassisName", d.getStr("name"));
            e.put("_parentChassisLRU", jsonObject.getStr("itemLowerUnit"));
            e.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ? a.getStr("cabinetName") : "");
            e.put("_statusIndex", statusStrToIndex(e));
//                    e._refreshIndex = t.refreshIndex,
            dataMap.put(e.getStr("uuid"), e);
        }
    }

    private void _processRackNode(Map<String, JSONObject> dataMap, JSONObject a, int b) {
        JSONObject nodeList = (JSONObject) a.getJSONArray("nodeList").get(b);
        JSONObject c = new JSONObject(nodeList.getJSONObject("itemInventory"));
        if (StringUtils.isNotBlank(c.getStr("uuid"))) {
            c.put("location", __getLocationProps(nodeList));
            c.put("_statusIndex", statusStrToIndex(c));
            a.getJSONArray("nodeList").getJSONObject(b).getJSONObject("itemInventory").clear();
            a.getJSONArray("nodeList").getJSONObject(b).put("_statusIndex", c.getInt("_statusIndex"));
            putParent(a, c);
            c.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ?
                    a.getStr("cabinetName") : "");
//            c.put("_refreshIndex",)
//            c._refreshIndex = t.refreshIndex;
            dataMap.put(c.getStr("uuid"), c);
        }
    }



    private Integer _getStatusByInteger(Integer statusStr) {
        Integer status = 3;
        switch (statusStr) {
            case 500:
            case 600:
            case 700:
                status = 0;
                break;
            case 300:
            case 400:
                status = 1;
                break;
            case 100:
                status = 3;
                break;
            case 200:
                status = 5;
                break;
        }
        return status;
    }

    private Integer _getStatusByString(String statusStr) {
        Integer status = 3;
        switch (statusStr) {
            case "ERROR":
            case  "CRITICAL":
            case "FATAL":
            case  "MAJOR-FAILURE":
                status = 0;
                break;
            case "WARNING":case "MINOR":case "NON-CRITICAL":case "MINOR-FAILURE":case "MAJOR":
                status =1;break;
            case "OFFLINE":
                status = 2;
                break;
            case "UNKNOWN":
                status = 3;
                break;
            case "PENDING":
                status = 4;
                break;
            case "INFORMATIONAL":
                status = 5;
                break;
            case "OK":case "NORMAL":case "GOOD":
                status = 6;
                break;
        }
        return status;
    }

    private boolean _isOffline(JSONObject endpoint) {
        String access = "";
        if (endpoint.get("accessState") instanceof String) {
            access = endpoint.getStr("accessState").toUpperCase();
        }
        if (access.equals("OFFLINE")) {
            endpoint.put("cmmHealthState",endpoint.getStr("accessState"));
            return true;
        } else {
            return false;
        }
    }

    private boolean _isPending(JSONObject endpoint) {
        String access = "";
        if (endpoint.get("accessState") instanceof String) {
            access = endpoint.getStr("accessState").toUpperCase();
        }
        if (access.equals("PENDING")) {
            endpoint.put("cmmHealthState",endpoint.getStr("accessState"));
            return true;
        } else {
            return false;
        }
    }

    private boolean isRackServer(JSONObject itemJSON) {
        if ("Rack-Tower Server".equals(itemJSON.getStr("type")) || "Lenovo ThinkServer".equals(itemJSON.getStr("type")))
            return true;
        return false;
    }

    private String _getStatus (JSONObject endpoint) {
        if (endpoint.get("overallHealthState") != null) {
//            if (window.gloablExcludeAlerts)
//                endpoint.cmmHealthState = endpoint.excludedHealthState
//            else
            endpoint.put("cmmHealthState", endpoint.get("overallHealthState"));
        }
        if (isRackServer(endpoint)) {
            String upper = endpoint.getStr("cmmHealthState").toUpperCase();
            if (upper .equals("MAJOR") || upper .equals("MAJOR-FAILURE") || upper .equals("NON-RECOVERABLE")) {
                endpoint.put("cmmHealthState","CRITICAL");
            }
        }
        if (endpoint.get("controllerId") != null) // lenovo storage
            endpoint.put("cmmHealthState", endpoint.get("health"));
        return endpoint.getStr("cmmHealthState");
    }

    private Object statusStrToIndex(Object statusStr) {
        Integer returnIndex = null;
        if (statusStr instanceof Integer) { // BY NUMBER
            Integer statusIndex = _getStatusByInteger((Integer) statusStr);
            if (statusIndex != null)
                return statusIndex;
            return statusStr;
        } else if (statusStr instanceof JSONObject && !((JSONObject) statusStr).getStr("type").equals("Lenovo Storage") && ((JSONObject) statusStr).get("canisters") instanceof JSONArray) {
            int x = 9999;
            JSONArray array = ((JSONObject) statusStr).getJSONArray("canisters");
            for(int i=0;i<array.size();i++){
                JSONObject item = (JSONObject) array.get(i);
                returnIndex = (Integer)statusStrToIndex(_isOffline(item)?"Offline":_isPending(item)?"Pending":_getStatus(item));
                if (returnIndex < x) {
                    x = returnIndex;
                }
            }
            return (9999 != x) ? x : statusStrToIndex(_isOffline((JSONObject)statusStr) ? "Offline" : (_isPending((JSONObject)statusStr) ? "Pending" : _getStatus((JSONObject)statusStr)));
        } else if (!(statusStr instanceof String)) {
            statusStr = _isOffline((JSONObject)statusStr) ? "Offline" : (_isPending((JSONObject)statusStr) ? "Pending" : _getStatus((JSONObject)statusStr));
        }
        if (StringUtils.isBlank(statusStr.toString())) // unsupported node?
            statusStr = "Unknown";

        // if not a direct return above, use string mapping to get status index
        returnIndex = _getStatusByString(statusStr.toString().toUpperCase());
        return returnIndex;
    }

    ;

    private JSONObject __getLocationProps(JSONObject a) {
        JSONObject b = a.getJSONObject("itemInventory").getJSONObject("location");
        if (b == null) {
            b = new JSONObject();
        }
        b.put("itemHeight", a.getInt("itemHeight"));
        b.put("itemLocation", a.getStr("itemLocation"));
        b.put("itemLocationRack", a.getStr("itemLocationRack"));
        b.put("itemLocationRoom", a.getStr("itemLocationRoom"));
        b.put("itemLowerUnit", a.getInt("itemLowerUnit"));
        return b;
    }
}
