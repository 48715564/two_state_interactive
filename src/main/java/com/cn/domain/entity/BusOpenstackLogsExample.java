package com.cn.domain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusOpenstackLogsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BusOpenstackLogsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountIsNull() {
            addCriterion("hypervisor_count is null");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountIsNotNull() {
            addCriterion("hypervisor_count is not null");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountEqualTo(Integer value) {
            addCriterion("hypervisor_count =", value, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountNotEqualTo(Integer value) {
            addCriterion("hypervisor_count <>", value, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountGreaterThan(Integer value) {
            addCriterion("hypervisor_count >", value, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("hypervisor_count >=", value, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountLessThan(Integer value) {
            addCriterion("hypervisor_count <", value, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountLessThanOrEqualTo(Integer value) {
            addCriterion("hypervisor_count <=", value, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountIn(List<Integer> values) {
            addCriterion("hypervisor_count in", values, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountNotIn(List<Integer> values) {
            addCriterion("hypervisor_count not in", values, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountBetween(Integer value1, Integer value2) {
            addCriterion("hypervisor_count between", value1, value2, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andHypervisorCountNotBetween(Integer value1, Integer value2) {
            addCriterion("hypervisor_count not between", value1, value2, "hypervisorCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountIsNull() {
            addCriterion("running_VM_count is null");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountIsNotNull() {
            addCriterion("running_VM_count is not null");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountEqualTo(Integer value) {
            addCriterion("running_VM_count =", value, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountNotEqualTo(Integer value) {
            addCriterion("running_VM_count <>", value, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountGreaterThan(Integer value) {
            addCriterion("running_VM_count >", value, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("running_VM_count >=", value, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountLessThan(Integer value) {
            addCriterion("running_VM_count <", value, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountLessThanOrEqualTo(Integer value) {
            addCriterion("running_VM_count <=", value, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountIn(List<Integer> values) {
            addCriterion("running_VM_count in", values, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountNotIn(List<Integer> values) {
            addCriterion("running_VM_count not in", values, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountBetween(Integer value1, Integer value2) {
            addCriterion("running_VM_count between", value1, value2, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andRunningVmCountNotBetween(Integer value1, Integer value2) {
            addCriterion("running_VM_count not between", value1, value2, "runningVmCount");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastIsNull() {
            addCriterion("disk_available_least is null");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastIsNotNull() {
            addCriterion("disk_available_least is not null");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastEqualTo(Integer value) {
            addCriterion("disk_available_least =", value, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastNotEqualTo(Integer value) {
            addCriterion("disk_available_least <>", value, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastGreaterThan(Integer value) {
            addCriterion("disk_available_least >", value, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastGreaterThanOrEqualTo(Integer value) {
            addCriterion("disk_available_least >=", value, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastLessThan(Integer value) {
            addCriterion("disk_available_least <", value, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastLessThanOrEqualTo(Integer value) {
            addCriterion("disk_available_least <=", value, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastIn(List<Integer> values) {
            addCriterion("disk_available_least in", values, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastNotIn(List<Integer> values) {
            addCriterion("disk_available_least not in", values, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastBetween(Integer value1, Integer value2) {
            addCriterion("disk_available_least between", value1, value2, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andDiskAvailableLeastNotBetween(Integer value1, Integer value2) {
            addCriterion("disk_available_least not between", value1, value2, "diskAvailableLeast");
            return (Criteria) this;
        }

        public Criteria andFreeDiskIsNull() {
            addCriterion("free_disk is null");
            return (Criteria) this;
        }

        public Criteria andFreeDiskIsNotNull() {
            addCriterion("free_disk is not null");
            return (Criteria) this;
        }

        public Criteria andFreeDiskEqualTo(Integer value) {
            addCriterion("free_disk =", value, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskNotEqualTo(Integer value) {
            addCriterion("free_disk <>", value, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskGreaterThan(Integer value) {
            addCriterion("free_disk >", value, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskGreaterThanOrEqualTo(Integer value) {
            addCriterion("free_disk >=", value, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskLessThan(Integer value) {
            addCriterion("free_disk <", value, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskLessThanOrEqualTo(Integer value) {
            addCriterion("free_disk <=", value, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskIn(List<Integer> values) {
            addCriterion("free_disk in", values, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskNotIn(List<Integer> values) {
            addCriterion("free_disk not in", values, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskBetween(Integer value1, Integer value2) {
            addCriterion("free_disk between", value1, value2, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andFreeDiskNotBetween(Integer value1, Integer value2) {
            addCriterion("free_disk not between", value1, value2, "freeDisk");
            return (Criteria) this;
        }

        public Criteria andLocalIsNull() {
            addCriterion("local is null");
            return (Criteria) this;
        }

        public Criteria andLocalIsNotNull() {
            addCriterion("local is not null");
            return (Criteria) this;
        }

        public Criteria andLocalEqualTo(Integer value) {
            addCriterion("local =", value, "local");
            return (Criteria) this;
        }

        public Criteria andLocalNotEqualTo(Integer value) {
            addCriterion("local <>", value, "local");
            return (Criteria) this;
        }

        public Criteria andLocalGreaterThan(Integer value) {
            addCriterion("local >", value, "local");
            return (Criteria) this;
        }

        public Criteria andLocalGreaterThanOrEqualTo(Integer value) {
            addCriterion("local >=", value, "local");
            return (Criteria) this;
        }

        public Criteria andLocalLessThan(Integer value) {
            addCriterion("local <", value, "local");
            return (Criteria) this;
        }

        public Criteria andLocalLessThanOrEqualTo(Integer value) {
            addCriterion("local <=", value, "local");
            return (Criteria) this;
        }

        public Criteria andLocalIn(List<Integer> values) {
            addCriterion("local in", values, "local");
            return (Criteria) this;
        }

        public Criteria andLocalNotIn(List<Integer> values) {
            addCriterion("local not in", values, "local");
            return (Criteria) this;
        }

        public Criteria andLocalBetween(Integer value1, Integer value2) {
            addCriterion("local between", value1, value2, "local");
            return (Criteria) this;
        }

        public Criteria andLocalNotBetween(Integer value1, Integer value2) {
            addCriterion("local not between", value1, value2, "local");
            return (Criteria) this;
        }

        public Criteria andLocalUsedIsNull() {
            addCriterion("local_used is null");
            return (Criteria) this;
        }

        public Criteria andLocalUsedIsNotNull() {
            addCriterion("local_used is not null");
            return (Criteria) this;
        }

        public Criteria andLocalUsedEqualTo(Integer value) {
            addCriterion("local_used =", value, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedNotEqualTo(Integer value) {
            addCriterion("local_used <>", value, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedGreaterThan(Integer value) {
            addCriterion("local_used >", value, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedGreaterThanOrEqualTo(Integer value) {
            addCriterion("local_used >=", value, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedLessThan(Integer value) {
            addCriterion("local_used <", value, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedLessThanOrEqualTo(Integer value) {
            addCriterion("local_used <=", value, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedIn(List<Integer> values) {
            addCriterion("local_used in", values, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedNotIn(List<Integer> values) {
            addCriterion("local_used not in", values, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedBetween(Integer value1, Integer value2) {
            addCriterion("local_used between", value1, value2, "localUsed");
            return (Criteria) this;
        }

        public Criteria andLocalUsedNotBetween(Integer value1, Integer value2) {
            addCriterion("local_used not between", value1, value2, "localUsed");
            return (Criteria) this;
        }

        public Criteria andFreeRamIsNull() {
            addCriterion("free_ram is null");
            return (Criteria) this;
        }

        public Criteria andFreeRamIsNotNull() {
            addCriterion("free_ram is not null");
            return (Criteria) this;
        }

        public Criteria andFreeRamEqualTo(Integer value) {
            addCriterion("free_ram =", value, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamNotEqualTo(Integer value) {
            addCriterion("free_ram <>", value, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamGreaterThan(Integer value) {
            addCriterion("free_ram >", value, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamGreaterThanOrEqualTo(Integer value) {
            addCriterion("free_ram >=", value, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamLessThan(Integer value) {
            addCriterion("free_ram <", value, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamLessThanOrEqualTo(Integer value) {
            addCriterion("free_ram <=", value, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamIn(List<Integer> values) {
            addCriterion("free_ram in", values, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamNotIn(List<Integer> values) {
            addCriterion("free_ram not in", values, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamBetween(Integer value1, Integer value2) {
            addCriterion("free_ram between", value1, value2, "freeRam");
            return (Criteria) this;
        }

        public Criteria andFreeRamNotBetween(Integer value1, Integer value2) {
            addCriterion("free_ram not between", value1, value2, "freeRam");
            return (Criteria) this;
        }

        public Criteria andMemoryIsNull() {
            addCriterion("memory is null");
            return (Criteria) this;
        }

        public Criteria andMemoryIsNotNull() {
            addCriterion("memory is not null");
            return (Criteria) this;
        }

        public Criteria andMemoryEqualTo(Integer value) {
            addCriterion("memory =", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryNotEqualTo(Integer value) {
            addCriterion("memory <>", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryGreaterThan(Integer value) {
            addCriterion("memory >", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("memory >=", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryLessThan(Integer value) {
            addCriterion("memory <", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryLessThanOrEqualTo(Integer value) {
            addCriterion("memory <=", value, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryIn(List<Integer> values) {
            addCriterion("memory in", values, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryNotIn(List<Integer> values) {
            addCriterion("memory not in", values, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryBetween(Integer value1, Integer value2) {
            addCriterion("memory between", value1, value2, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryNotBetween(Integer value1, Integer value2) {
            addCriterion("memory not between", value1, value2, "memory");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedIsNull() {
            addCriterion("memory_used is null");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedIsNotNull() {
            addCriterion("memory_used is not null");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedEqualTo(Integer value) {
            addCriterion("memory_used =", value, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedNotEqualTo(Integer value) {
            addCriterion("memory_used <>", value, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedGreaterThan(Integer value) {
            addCriterion("memory_used >", value, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedGreaterThanOrEqualTo(Integer value) {
            addCriterion("memory_used >=", value, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedLessThan(Integer value) {
            addCriterion("memory_used <", value, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedLessThanOrEqualTo(Integer value) {
            addCriterion("memory_used <=", value, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedIn(List<Integer> values) {
            addCriterion("memory_used in", values, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedNotIn(List<Integer> values) {
            addCriterion("memory_used not in", values, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedBetween(Integer value1, Integer value2) {
            addCriterion("memory_used between", value1, value2, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andMemoryUsedNotBetween(Integer value1, Integer value2) {
            addCriterion("memory_used not between", value1, value2, "memoryUsed");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuIsNull() {
            addCriterion("virtual_CPU is null");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuIsNotNull() {
            addCriterion("virtual_CPU is not null");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuEqualTo(Integer value) {
            addCriterion("virtual_CPU =", value, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuNotEqualTo(Integer value) {
            addCriterion("virtual_CPU <>", value, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuGreaterThan(Integer value) {
            addCriterion("virtual_CPU >", value, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuGreaterThanOrEqualTo(Integer value) {
            addCriterion("virtual_CPU >=", value, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuLessThan(Integer value) {
            addCriterion("virtual_CPU <", value, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuLessThanOrEqualTo(Integer value) {
            addCriterion("virtual_CPU <=", value, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuIn(List<Integer> values) {
            addCriterion("virtual_CPU in", values, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuNotIn(List<Integer> values) {
            addCriterion("virtual_CPU not in", values, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuBetween(Integer value1, Integer value2) {
            addCriterion("virtual_CPU between", value1, value2, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualCpuNotBetween(Integer value1, Integer value2) {
            addCriterion("virtual_CPU not between", value1, value2, "virtualCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuIsNull() {
            addCriterion("virtual_used_CPU is null");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuIsNotNull() {
            addCriterion("virtual_used_CPU is not null");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuEqualTo(Integer value) {
            addCriterion("virtual_used_CPU =", value, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuNotEqualTo(Integer value) {
            addCriterion("virtual_used_CPU <>", value, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuGreaterThan(Integer value) {
            addCriterion("virtual_used_CPU >", value, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuGreaterThanOrEqualTo(Integer value) {
            addCriterion("virtual_used_CPU >=", value, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuLessThan(Integer value) {
            addCriterion("virtual_used_CPU <", value, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuLessThanOrEqualTo(Integer value) {
            addCriterion("virtual_used_CPU <=", value, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuIn(List<Integer> values) {
            addCriterion("virtual_used_CPU in", values, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuNotIn(List<Integer> values) {
            addCriterion("virtual_used_CPU not in", values, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuBetween(Integer value1, Integer value2) {
            addCriterion("virtual_used_CPU between", value1, value2, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andVirtualUsedCpuNotBetween(Integer value1, Integer value2) {
            addCriterion("virtual_used_CPU not between", value1, value2, "virtualUsedCpu");
            return (Criteria) this;
        }

        public Criteria andCreateTmIsNull() {
            addCriterion("create_tm is null");
            return (Criteria) this;
        }

        public Criteria andCreateTmIsNotNull() {
            addCriterion("create_tm is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTmEqualTo(Date value) {
            addCriterion("create_tm =", value, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmNotEqualTo(Date value) {
            addCriterion("create_tm <>", value, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmGreaterThan(Date value) {
            addCriterion("create_tm >", value, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmGreaterThanOrEqualTo(Date value) {
            addCriterion("create_tm >=", value, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmLessThan(Date value) {
            addCriterion("create_tm <", value, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmLessThanOrEqualTo(Date value) {
            addCriterion("create_tm <=", value, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmIn(List<Date> values) {
            addCriterion("create_tm in", values, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmNotIn(List<Date> values) {
            addCriterion("create_tm not in", values, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmBetween(Date value1, Date value2) {
            addCriterion("create_tm between", value1, value2, "createTm");
            return (Criteria) this;
        }

        public Criteria andCreateTmNotBetween(Date value1, Date value2) {
            addCriterion("create_tm not between", value1, value2, "createTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmIsNull() {
            addCriterion("update_tm is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTmIsNotNull() {
            addCriterion("update_tm is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTmEqualTo(Date value) {
            addCriterion("update_tm =", value, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmNotEqualTo(Date value) {
            addCriterion("update_tm <>", value, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmGreaterThan(Date value) {
            addCriterion("update_tm >", value, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmGreaterThanOrEqualTo(Date value) {
            addCriterion("update_tm >=", value, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmLessThan(Date value) {
            addCriterion("update_tm <", value, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmLessThanOrEqualTo(Date value) {
            addCriterion("update_tm <=", value, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmIn(List<Date> values) {
            addCriterion("update_tm in", values, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmNotIn(List<Date> values) {
            addCriterion("update_tm not in", values, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmBetween(Date value1, Date value2) {
            addCriterion("update_tm between", value1, value2, "updateTm");
            return (Criteria) this;
        }

        public Criteria andUpdateTmNotBetween(Date value1, Date value2) {
            addCriterion("update_tm not between", value1, value2, "updateTm");
            return (Criteria) this;
        }

        public Criteria andNetworkCountIsNull() {
            addCriterion("network_count is null");
            return (Criteria) this;
        }

        public Criteria andNetworkCountIsNotNull() {
            addCriterion("network_count is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkCountEqualTo(Integer value) {
            addCriterion("network_count =", value, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountNotEqualTo(Integer value) {
            addCriterion("network_count <>", value, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountGreaterThan(Integer value) {
            addCriterion("network_count >", value, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("network_count >=", value, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountLessThan(Integer value) {
            addCriterion("network_count <", value, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountLessThanOrEqualTo(Integer value) {
            addCriterion("network_count <=", value, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountIn(List<Integer> values) {
            addCriterion("network_count in", values, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountNotIn(List<Integer> values) {
            addCriterion("network_count not in", values, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountBetween(Integer value1, Integer value2) {
            addCriterion("network_count between", value1, value2, "networkCount");
            return (Criteria) this;
        }

        public Criteria andNetworkCountNotBetween(Integer value1, Integer value2) {
            addCriterion("network_count not between", value1, value2, "networkCount");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}