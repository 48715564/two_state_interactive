package com.cn.domain.entity;

import com.cn.common.persistence.BaseEntity;

public class BusOpenstackLogs extends BaseEntity {
    private Integer hypervisorCount;

    private Integer runningVmCount;

    private Integer diskAvailableLeast;

    private Integer freeDisk;

    private Integer local;

    private Integer localUsed;

    private Integer freeRam;

    private Integer memory;

    private Integer memoryUsed;

    private Integer virtualCpu;

    private Integer virtualUsedCpu;

    private Integer networkCount;

    public Integer getHypervisorCount() {
        return hypervisorCount;
    }

    public void setHypervisorCount(Integer hypervisorCount) {
        this.hypervisorCount = hypervisorCount;
    }

    public Integer getRunningVmCount() {
        return runningVmCount;
    }

    public void setRunningVmCount(Integer runningVmCount) {
        this.runningVmCount = runningVmCount;
    }

    public Integer getDiskAvailableLeast() {
        return diskAvailableLeast;
    }

    public void setDiskAvailableLeast(Integer diskAvailableLeast) {
        this.diskAvailableLeast = diskAvailableLeast;
    }

    public Integer getFreeDisk() {
        return freeDisk;
    }

    public void setFreeDisk(Integer freeDisk) {
        this.freeDisk = freeDisk;
    }

    public Integer getLocal() {
        return local;
    }

    public void setLocal(Integer local) {
        this.local = local;
    }

    public Integer getLocalUsed() {
        return localUsed;
    }

    public void setLocalUsed(Integer localUsed) {
        this.localUsed = localUsed;
    }

    public Integer getFreeRam() {
        return freeRam;
    }

    public void setFreeRam(Integer freeRam) {
        this.freeRam = freeRam;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public Integer getVirtualCpu() {
        return virtualCpu;
    }

    public void setVirtualCpu(Integer virtualCpu) {
        this.virtualCpu = virtualCpu;
    }

    public Integer getVirtualUsedCpu() {
        return virtualUsedCpu;
    }

    public void setVirtualUsedCpu(Integer virtualUsedCpu) {
        this.virtualUsedCpu = virtualUsedCpu;
    }

    public Integer getNetworkCount() {
        return networkCount;
    }

    public void setNetworkCount(Integer networkCount) {
        this.networkCount = networkCount;
    }
}