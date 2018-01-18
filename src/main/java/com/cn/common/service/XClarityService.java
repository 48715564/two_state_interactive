package com.cn.common.service;

import com.cn.common.utils.OSClientV3Factory;
import com.cn.common.utils.XClarityApi;
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

    public void _onFinishInitializeInventory(JSONObject jsonObject) {
        Map<String, JSONObject> serverMemoryStore = Maps.newHashMap();
        Map<String, JSONObject> storageMemoryStore = Maps.newHashMap();
        Map<String, JSONObject> switchMemoryStore = Maps.newHashMap();
        Map<String, JSONObject> chassisMemoryStore = Maps.newHashMap();
        Map<String, JSONObject> complexMemoryStore = Maps.newHashMap();
        Map<String, JSONObject> rackMemoryStore = Maps.newHashMap();
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
                this._processComplex(complexMemoryStore,cabinetNode, c);
            }
            for (d = 0; d < cabinetNode.getJSONArray("storageList").size(); d++) {
                _processRackStorage(storageMemoryStore,cabinetNode, d);
            }
            if(!"STANDALONE_OBJECT_UUID".equals(cabinetNode.getStr("UUID"))){
                _processRack(rackMemoryStore,cabinetNode);
            }
        }
    }

    private void _processRack(Map<String, JSONObject> dataMap,JSONObject a) {
        a.put("name",a.getStr("cabinetName"));
        a.put("_statusIndex",getCabinetStatusIndex(a));
        dataMap.put(a.getStr("UUID"),a);

    }

    //todo
    private int getCabinetStatusIndex(JSONObject a){
        return 0;
    }

    private void _processRackStorage (Map<String, JSONObject> dataMap,JSONObject a, int b) {
        JSONObject storageListNode = (JSONObject) a.getJSONArray("storageList").get(b);
        JSONObject c = new JSONObject(storageListNode.getJSONObject("itemInventory"));
        if (StringUtils.isNotBlank(c.getStr("uuid"))) {
            storageListNode.getJSONObject("itemInventory").clear();
            JSONObject location = c.getJSONObject("location");
            location.put("itemHeight",storageListNode.getInt("itemHeight"));
            location.put("itemLocation", storageListNode.getStr("itemLocation"));
            location.put("itemLocationRack", storageListNode.getStr("itemLocationRack"));
            location.put("itemLocationRoom", storageListNode.getStr("itemLocationRoom"));
            location.put("itemLowerUnit", storageListNode.getInt("itemLowerUnit"));
            c.put("_statusIndex", statusStrToIndex(c));
            c.put("_parent",new JSONObject(c.getJSONObject("parent")));
            putParent(a,c);
            c.getJSONObject("parent").put("type","rack");
            c.put("_parentRackName", !"STANDALONE_OBJECT_NAME".equals(a.getStr("cabinetName")) ? a.getStr("cabinetName") : "");
            dataMap.put(c.getStr("uuid"),c);
        }
    }

    private void _processComplex(Map<String, JSONObject> dataMap,JSONObject a,int b) {
        JSONObject complexListNode = (JSONObject) a.getJSONArray("complexList").get(b);
        JSONObject location = complexListNode.getJSONObject("location");
        location.put("itemHeight",a.getInt("height"));
        location.put("itemLocation", location.getStr("location"));
        location.put("itemLocationRack", location.getStr("rack"));
        location.put("itemLocationRoom", location.getStr("room"));
        location.put("itemLowerUnit", location.getInt("lowestRackUnit"));
        putParent(a,complexListNode.getJSONObject("parent"));
        dataMap.put(complexListNode.getStr("uuid"),complexListNode);
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

    //todo /utils/statusUtils
    private Object statusStrToIndex(JSONObject o) {
        return 1;
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
