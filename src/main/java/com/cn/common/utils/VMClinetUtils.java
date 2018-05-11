package com.cn.common.utils;

import java.util.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

import com.cn.common.infrastructure.Constant;
import com.vmware.vim25.*;

public class VMClinetUtils {
    private String url = Constant.url;
    private String userName = Constant.userName;
    private final String password = Constant.password;

    private final ManagedObjectReference SVC_INST_REF = new ManagedObjectReference();
    public VimService vimService;
    public VimPortType vimPort;

    public ServiceContent serviceContent;
    private final String SVC_INST_NAME = "ServiceInstance";
    private Boolean isConnected = false;
    public ManagedObjectReference perfManager;
    public ManagedObjectReference propCollectorRef;

    private class TrustAllTrustManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
            return;
        }
    }

    /**
     * 获取所有的数据中心
     *
     * @param pathList
     * @return
     * @throws Exception
     */
    public List<ObjectContent> getAllDataCeneter(List<String> pathList) throws Exception {
        return getAllBySpecAndType(getDatacenterTraversalSpec(), "Datacenter", pathList);
    }

    /**
     * 获取所有的主机
     *
     * @param pathList
     * @return
     * @throws Exception
     */
    public List<ObjectContent> getAllHost(List<String> pathList) throws Exception {
        return getAllBySpecAndType(getHostSystemTraversalSpec(), "HostSystem", pathList);
    }

    /**
     * 获取所有的集群
     *
     * @param pathList
     * @return
     * @throws Exception
     */
    public List<ObjectContent> getAllCluster(List<String> pathList) throws Exception {
        return getAllBySpecAndType(getComputeResourceTraversalSpec(), "ClusterComputeResource", pathList);
    }


    /**
     * 获取所有的虚拟机
     *
     * @param pathList
     * @return
     * @throws Exception
     */
    public List<ObjectContent> getAllVM(List<String> pathList) throws Exception {
        return getAllBySpecAndType(getVmTraversalSpec(), "VirtualMachine", pathList);
    }

    /**
     * 获取所有的数据存储
     *
     * @param pathList
     * @return
     * @throws Exception
     */
    public List<ObjectContent> getAllDataStore(List<String> pathList) throws Exception {
        return getAllBySpecAndType(getDataStoreTraversalSpec(), "Datastore", pathList);
    }

    /**
     * 获取所有的网络
     *
     * @param pathList
     * @return
     * @throws Exception
     */
    public List<ObjectContent> getAllNetWork(List<String> pathList) throws Exception {
        return getAllBySpecAndType(getNetWorkTraversalSpec(), "Network", pathList);
    }

    /**
     * 根据查询目录和查询类别查找所有
     *
     * @param traversalSpec
     * @param type
     * @param pathList
     * @return
     * @throws Exception
     */
    public List<ObjectContent> getAllBySpecAndType(TraversalSpec traversalSpec, String type, List<String> pathList) throws Exception {
        List<ManagedObjectReference> retVal = new ArrayList<ManagedObjectReference>();
        //获取根目录对象引用
        ManagedObjectReference rootFolder = serviceContent.getRootFolder();
        TraversalSpec tSpec = traversalSpec;

        /**
         * ObjectSpec：定义对象详述，明确清单导航起始点。
         * obj:定义遍历起始对象为根目录rootFolder
         * true:表示只收集Datacenter的数据，不收集containerView的数据。
         * */
        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(rootFolder);
        objectSpec.setSkip(Boolean.TRUE);

        /** 添加 tSpec到 ObjectSpec.selectSet队列中 */
        objectSpec.getSelectSet().add(tSpec);

        /**
         * PropertySpec：定义一个属性收集器详述，明确收集的具体对象(Datacenter)和属性(Datacenter中的name，可以为多个)
         * Type:具体的对象类型为Datacenter
         * pathset:明确对象Datacenter中的属性，可以为多个。
         * */
        PropertySpec propertySpec = new PropertySpec();
        propertySpec.setAll(Boolean.FALSE);
        propertySpec.getPathSet().addAll(pathList);
        propertySpec.setType(type);

        /**
         * PropertyFilterSpec:定义一个属性过滤器详述，添加对象详述和属性收集器详述到过率中
         * */
        PropertyFilterSpec propertyFilterSpec = new PropertyFilterSpec();
        propertyFilterSpec.getPropSet().add(propertySpec);
        propertyFilterSpec.getObjectSet().add(objectSpec);

        /** 添加属性过滤器详述到属性过滤器集合中 */
        List<PropertyFilterSpec> listfps = new ArrayList<PropertyFilterSpec>(1);
        listfps.add(propertyFilterSpec);

        /** 调用方法获取ObjectContent对象集合 */
        return retrievePropertiesAllObjects(listfps);
    }

    /**
     * 获取所有的网络
     *
     * @return
     */
    private TraversalSpec getNetWorkTraversalSpec() {
        SelectionSpec ss = new SelectionSpec();
        ss.setName("VisitFolders");

        TraversalSpec dataCenterToNetWorkFolder = new TraversalSpec();
        dataCenterToNetWorkFolder.setName("DataCenterToNetWorkFolder");
        dataCenterToNetWorkFolder.setType("Datacenter");
        dataCenterToNetWorkFolder.setPath("networkFolder");
        dataCenterToNetWorkFolder.setSkip(false);
        dataCenterToNetWorkFolder.getSelectSet().add(ss);

        TraversalSpec traversalSpec = new TraversalSpec();
        traversalSpec.setName("VisitFolders");
        traversalSpec.setType("Folder");
        traversalSpec.setPath("childEntity");
        traversalSpec.setSkip(false);

        List<SelectionSpec> sSpecArr = new ArrayList<SelectionSpec>();
        sSpecArr.add(ss);
        sSpecArr.add(dataCenterToNetWorkFolder);
        traversalSpec.getSelectSet().addAll(sSpecArr);
        return traversalSpec;
    }

    /**
     * 获取数据存储搜索目录
     *
     * @return
     */
    private TraversalSpec getDataStoreTraversalSpec() {
        SelectionSpec ss = new SelectionSpec();
        ss.setName("VisitFolders");

        TraversalSpec dataCenterToDataStoreFolder = new TraversalSpec();
        dataCenterToDataStoreFolder.setName("DataCenterToDataStoreFolder");
        dataCenterToDataStoreFolder.setType("Datacenter");
        dataCenterToDataStoreFolder.setPath("datastoreFolder");
        dataCenterToDataStoreFolder.setSkip(false);
        dataCenterToDataStoreFolder.getSelectSet().add(ss);

        TraversalSpec traversalSpec = new TraversalSpec();
        traversalSpec.setName("VisitFolders");
        traversalSpec.setType("Folder");
        traversalSpec.setPath("childEntity");
        traversalSpec.setSkip(false);

        List<SelectionSpec> sSpecArr = new ArrayList<SelectionSpec>();
        sSpecArr.add(ss);
        sSpecArr.add(dataCenterToDataStoreFolder);
        traversalSpec.getSelectSet().addAll(sSpecArr);
        return traversalSpec;
    }

    /**
     * 获得数据中心的搜索目录
     *
     * @return
     */
    private TraversalSpec getDatacenterTraversalSpec() {
        //SelectionSpec是TraversalSpec的一个引用。
        SelectionSpec sSpec = new SelectionSpec();
        sSpec.setName("VisitFolders");

        TraversalSpec traversalSpec = new TraversalSpec();
        //给traversalSpec设置名称
        traversalSpec.setName("VisitFolders");
        //从rootFolder开始遍历，rootFolder类型是Folder
        traversalSpec.setType("Folder");
        //rootFolder拥有childEntity属性，清单结构图中指向的便是Datacenter
        traversalSpec.setPath("childEntity");
        //false表示不对其本身进行收集，只对其下对象进行收集
        traversalSpec.setSkip(false);
        //将sSpec添加到SelectionSpec集合中
        traversalSpec.getSelectSet().add(sSpec);
        return traversalSpec;
    }

    /**
     * 获取虚拟机的搜索目录
     *
     * @return
     */
    private TraversalSpec getVmTraversalSpec() {
        TraversalSpec vAppToVM = new TraversalSpec();
        vAppToVM.setName("vAppToVM");
        vAppToVM.setType("VirtualApp");
        vAppToVM.setPath("vm");

        TraversalSpec vAppToVApp = new TraversalSpec();
        vAppToVApp.setName("vAppToVApp");
        vAppToVApp.setType("VirtualApp");
        vAppToVApp.setPath("resourcePool");

        SelectionSpec vAppRecursion = new SelectionSpec();
        vAppRecursion.setName("vAppToVApp");
        SelectionSpec vmInVApp = new SelectionSpec();
        vmInVApp.setName("vAppToVM");
        List<SelectionSpec> vAppToVMSS = new ArrayList<SelectionSpec>();
        vAppToVMSS.add(vAppRecursion);
        vAppToVMSS.add(vmInVApp);
        vAppToVApp.getSelectSet().addAll(vAppToVMSS);

        SelectionSpec sSpec = new SelectionSpec();
        sSpec.setName("VisitFolders");

        TraversalSpec dataCenterToVMFolder = new TraversalSpec();
        dataCenterToVMFolder.setName("DataCenterToVMFolder");
        dataCenterToVMFolder.setType("Datacenter");
        dataCenterToVMFolder.setPath("vmFolder");
        dataCenterToVMFolder.setSkip(false);
        dataCenterToVMFolder.getSelectSet().add(sSpec);

        TraversalSpec traversalSpec = new TraversalSpec();
        traversalSpec.setName("VisitFolders");
        traversalSpec.setType("Folder");
        traversalSpec.setPath("childEntity");
        traversalSpec.setSkip(false);
        List<SelectionSpec> sSpecArr = new ArrayList<SelectionSpec>();
        sSpecArr.add(sSpec);
        sSpecArr.add(dataCenterToVMFolder);
        sSpecArr.add(vAppToVM);
        sSpecArr.add(vAppToVApp);
        traversalSpec.getSelectSet().addAll(sSpecArr);
        return traversalSpec;
    }

    /**
     * 获得主机的搜索目录
     *
     * @return
     */
    private TraversalSpec getHostSystemTraversalSpec() {
        SelectionSpec ss = new SelectionSpec();
        ss.setName("VisitFolders");

        TraversalSpec computeResourceToHostSystem = new TraversalSpec();
        computeResourceToHostSystem.setName("computeResourceToHostSystem");
        computeResourceToHostSystem.setType("ComputeResource");
        computeResourceToHostSystem.setPath("host");
        computeResourceToHostSystem.setSkip(false);
        computeResourceToHostSystem.getSelectSet().add(ss);

        TraversalSpec hostFolderToComputeResource = new TraversalSpec();
        hostFolderToComputeResource.setName("hostFolderToComputeResource");
        hostFolderToComputeResource.setType("Folder");
        hostFolderToComputeResource.setPath("childEntity");
        hostFolderToComputeResource.setSkip(false);
        hostFolderToComputeResource.getSelectSet().add(ss);

        TraversalSpec dataCenterToHostFolder = new TraversalSpec();
        dataCenterToHostFolder.setName("DataCenterToHostFolder");
        dataCenterToHostFolder.setType("Datacenter");
        dataCenterToHostFolder.setPath("hostFolder");
        dataCenterToHostFolder.setSkip(false);
        dataCenterToHostFolder.getSelectSet().add(ss);

        TraversalSpec traversalSpec = new TraversalSpec();
        traversalSpec.setName("VisitFolders");
        traversalSpec.setType("Folder");
        traversalSpec.setPath("childEntity");
        traversalSpec.setSkip(false);

        List<SelectionSpec> sSpecArr = new ArrayList<SelectionSpec>();
        sSpecArr.add(ss);
        sSpecArr.add(dataCenterToHostFolder);
        sSpecArr.add(hostFolderToComputeResource);
        sSpecArr.add(computeResourceToHostSystem);
        traversalSpec.getSelectSet().addAll(sSpecArr);
        return traversalSpec;
    }

    /**
     * 获取集群和ComputeResource的搜索目录
     *
     * @return
     */
    private TraversalSpec getComputeResourceTraversalSpec() {
        SelectionSpec ss = new SelectionSpec();
        ss.setName("VisitFolders");

        TraversalSpec hostFolderToComputeResource = new TraversalSpec();
        hostFolderToComputeResource.setName("hostFolderToComputeResource");
        hostFolderToComputeResource.setType("Folder");
        hostFolderToComputeResource.setPath("childEntity");
        hostFolderToComputeResource.setSkip(false);
        hostFolderToComputeResource.getSelectSet().add(ss);

        TraversalSpec dataCenterToHostFolder = new TraversalSpec();
        dataCenterToHostFolder.setName("DataCenterToHostFolder");
        dataCenterToHostFolder.setType("Datacenter");
        dataCenterToHostFolder.setPath("hostFolder");
        dataCenterToHostFolder.setSkip(false);
        dataCenterToHostFolder.getSelectSet().add(ss);

        TraversalSpec traversalSpec = new TraversalSpec();
        traversalSpec.setName("VisitFolders");
        traversalSpec.setType("Folder");
        traversalSpec.setPath("childEntity");
        traversalSpec.setSkip(false);

        List<SelectionSpec> sSpecArr = new ArrayList<SelectionSpec>();
        sSpecArr.add(ss);
        sSpecArr.add(dataCenterToHostFolder);
        sSpecArr.add(hostFolderToComputeResource);
        traversalSpec.getSelectSet().addAll(sSpecArr);
        return traversalSpec;
    }

    private void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();
        sslsc.setSessionTimeout(0);
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }



    /**
     *
     * 获取服务器对象监控全部监控信息
     * @param vmmor 对象
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return
     * @throws RuntimeFaultFaultMsg
     * @throws DatatypeConfigurationException
     */
    public List<PerfCounterInfo> getMonitorData(ManagedObjectReference vmmor, XMLGregorianCalendar beginTime, XMLGregorianCalendar endTime) throws RuntimeFaultFaultMsg, DatatypeConfigurationException {
        List<PerfCounterInfo> list = new ArrayList<>();
        if (vmmor != null) {
            List<PerfCounterInfo> cInfo = getPerfCounters();
            List<PerfCounterInfo> vmCpuCounters = new ArrayList<PerfCounterInfo>();
            for (int i = 0; i < cInfo.size(); ++i) {
                vmCpuCounters.add(cInfo.get(i));
            }

            int i = 0;
            Map<Integer, PerfCounterInfo> counters = new HashMap<Integer, PerfCounterInfo>();
            for (Iterator<PerfCounterInfo> it = vmCpuCounters.iterator(); it.hasNext(); ) {
                PerfCounterInfo pcInfo = (PerfCounterInfo) it.next();
                counters.put(new Integer(pcInfo.getKey()), pcInfo);
            }

            List<PerfMetricId> listpermeid = vimPort.queryAvailablePerfMetric(perfManager, vmmor, null, null, null);

            ArrayList<PerfMetricId> mMetrics = new ArrayList<PerfMetricId>();
            if (listpermeid != null) {
                for (int index = 0; index < listpermeid.size(); ++index) {
                    if (counters.containsKey(new Integer(listpermeid.get(index).getCounterId()))) {
                        mMetrics.add(listpermeid.get(index));
                    }
                }
            }

            PerfQuerySpec qSpec = new PerfQuerySpec();
            qSpec.setEntity(vmmor);
            qSpec.getMetricId().addAll(mMetrics);
            qSpec.setEndTime(endTime);
            qSpec.setStartTime(beginTime);

            List<PerfQuerySpec> qSpecs = new ArrayList<>();
            qSpecs.add(qSpec);

            List<PerfEntityMetricBase> listpemb = vimPort.queryPerf(perfManager, qSpecs);
            List<PerfEntityMetricBase> pValues = listpemb;

            for (i = 0; i < pValues.size(); i++) {
                List<PerfMetricSeries> listpems = ((PerfEntityMetric) pValues.get(i)).getValue();
                for (int vi = 0; vi < listpems.size(); ++vi) {
                    String printInf = "";
                    PerfCounterInfo pci = (PerfCounterInfo) counters.get(new Integer(listpems.get(vi).getId().getCounterId()));

                    if (pci != null) {
                        if (listpems.get(vi) instanceof PerfMetricIntSeries) {
                            printInf += vi + ":" + pci.getNameInfo().getSummary() + ":" + pci.getNameInfo().getKey() + ":" + pci.getNameInfo().getLabel() + ":"
                                    + pci.getGroupInfo().getKey() + ":" + pci.getGroupInfo().getLabel() + ":" + pci.getGroupInfo().getSummary() + " ";
                            list.add(pci);
                            System.out.println(printInf);
                        }
                    }
                }
            }

        }

        return list;
    }

    /**
     * @功能描述 连接认证
     */
    public void connect() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();

        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        SVC_INST_REF.setType(SVC_INST_NAME);
        SVC_INST_REF.setValue(SVC_INST_NAME);

        vimService = new VimService();
        vimPort = vimService.getVimPort();
        Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();

        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

        serviceContent = vimPort.retrieveServiceContent(SVC_INST_REF);
        vimPort.login(serviceContent.getSessionManager(), userName, password, null);
        isConnected = true;

        perfManager = serviceContent.getPerfManager();
        propCollectorRef = serviceContent.getPropertyCollector();

        System.out.println(serviceContent.getAbout().getFullName());
        System.out.println("Server type is " + serviceContent.getAbout().getApiType());
    }

    /**
     * @return
     * @throws Exception
     * @功能描述 断开连接
     */
    public void disconnect() throws Exception {
        if (isConnected) {
            vimPort.logout(serviceContent.getSessionManager());
        }
        isConnected = false;
    }

    /**
     * @param
     * @param sfe
     * @功能描述 打印错误信息
     */
    public void printSoapFaultException(SOAPFaultException sfe) {
        System.out.println("Soap fault: ");
        if (sfe.getFault().hasDetail()) {
            System.out.println(sfe.getFault().getDetail().getFirstChild().getLocalName());
        }
        if (sfe.getFault().getFaultString() != null) {
            System.out.println("Message: " + sfe.getFault().getFaultString());
        }
    }

    /**
     * @param listpfs 属性过滤器集合
     * @throws Exception
     * @功能描述 根据属性检索要查询的对象信息
     */
    public List<ObjectContent> retrievePropertiesAllObjects(List<PropertyFilterSpec> listpfs) throws Exception {
        RetrieveOptions propObjectRetrieveOpts = new RetrieveOptions();
        List<ObjectContent> listobjcontent = new ArrayList<ObjectContent>();
        try {
            RetrieveResult rslts = vimPort.retrievePropertiesEx(propCollectorRef, listpfs, propObjectRetrieveOpts);
            if (rslts != null && rslts.getObjects() != null && !rslts.getObjects().isEmpty()) {
                listobjcontent.addAll(rslts.getObjects());
            }
            String token = null;
            if (rslts != null && rslts.getToken() != null) {
                token = rslts.getToken();
            }
            while (token != null && !token.isEmpty()) {
                rslts = vimPort.continueRetrievePropertiesEx(propCollectorRef, token);
                token = null;
                if (rslts != null) {
                    token = rslts.getToken();
                    if (rslts.getObjects() != null && !rslts.getObjects().isEmpty()) {
                        listobjcontent.addAll(rslts.getObjects());
                    }
                }
            }
        } catch (SOAPFaultException sfe) {
            printSoapFaultException(sfe);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listobjcontent;
    }


    public List<PerfCounterInfo> getPerfCounters() {
        List<PerfCounterInfo> pciArr = null;

        try {
            PropertySpec propertySpec = new PropertySpec();
            propertySpec.setAll(Boolean.FALSE);
            propertySpec.getPathSet().add("perfCounter");
            propertySpec.setType("PerformanceManager");
            List<PropertySpec> propertySpecs = new ArrayList<PropertySpec>();
            propertySpecs.add(propertySpec);

            ObjectSpec objectSpec = new ObjectSpec();
            objectSpec.setObj(perfManager);
            List<ObjectSpec> objectSpecs = new ArrayList<ObjectSpec>();
            objectSpecs.add(objectSpec);

            PropertyFilterSpec propertyFilterSpec = new PropertyFilterSpec();
            propertyFilterSpec.getPropSet().add(propertySpec);
            propertyFilterSpec.getObjectSet().add(objectSpec);

            List<PropertyFilterSpec> propertyFilterSpecs = new ArrayList<PropertyFilterSpec>();
            propertyFilterSpecs.add(propertyFilterSpec);

            List<PropertyFilterSpec> listpfs = new ArrayList<PropertyFilterSpec>(10);
            listpfs.add(propertyFilterSpec);
            List<ObjectContent> listobjcont = retrievePropertiesAllObjects(listpfs);

            if (listobjcont != null) {
                for (ObjectContent oc : listobjcont) {
                    List<DynamicProperty> dps = oc.getPropSet();
                    if (dps != null) {
                        for (DynamicProperty dp : dps) {
                            List<PerfCounterInfo> pcinfolist = ((ArrayOfPerfCounterInfo) dp.getVal()).getPerfCounterInfo();
                            pciArr = pcinfolist;
                        }
                    }
                }
            }
        } catch (SOAPFaultException sfe) {
            printSoapFaultException(sfe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pciArr;
    }


    public RetrieveResult getRetrieveResultObjectWithProperty(String MorName, String property) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference viewMgrRef = serviceContent.getViewManager();
        ManagedObjectReference propColl = serviceContent.getPropertyCollector();

        List<String> vmList = new ArrayList<String>();
        vmList.add(MorName);

        ManagedObjectReference cViewRef = vimPort.createContainerView(viewMgrRef, serviceContent.getRootFolder(), vmList, true);

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(cViewRef);
        oSpec.setSkip(true);

        TraversalSpec tSpec = new TraversalSpec();
        tSpec.setName("traversalEntities");
        tSpec.setPath("view");
        tSpec.setSkip(false);
        tSpec.setType("ContainerView");

        oSpec.getSelectSet().add(tSpec);

        PropertySpec pSpec = new PropertySpec();
        pSpec.setType(MorName);
        pSpec.getPathSet().add("name");

        PropertySpec pSpecRPr = new PropertySpec();
        pSpecRPr.setType(MorName);
        pSpecRPr.getPathSet().add(property);

        PropertyFilterSpec fSpec = new PropertyFilterSpec();
        fSpec.getObjectSet().add(oSpec);
        fSpec.getPropSet().add(pSpec);
        fSpec.getPropSet().add(pSpecRPr);

        List<PropertyFilterSpec> fSpecList = new ArrayList<PropertyFilterSpec>();
        fSpecList.add(fSpec);

        RetrieveOptions ro = new RetrieveOptions();
        RetrieveResult props = vimPort.retrievePropertiesEx(propColl, fSpecList, ro);

        return props;
    }

    public VimService getVimService() {
        return vimService;
    }

    public void setVimService(VimService vimService) {
        this.vimService = vimService;
    }

    public VimPortType getVimPort() {
        return vimPort;
    }

    public void setVimPort(VimPortType vimPort) {
        this.vimPort = vimPort;
    }

    public ServiceContent getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(ServiceContent serviceContent) {
        this.serviceContent = serviceContent;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public ManagedObjectReference getPerfManager() {
        return perfManager;
    }

    public void setPerfManager(ManagedObjectReference perfManager) {
        this.perfManager = perfManager;
    }

    public ManagedObjectReference getPropCollectorRef() {
        return propCollectorRef;
    }

    public void setPropCollectorRef(ManagedObjectReference propCollectorRef) {
        this.propCollectorRef = propCollectorRef;
    }

}
