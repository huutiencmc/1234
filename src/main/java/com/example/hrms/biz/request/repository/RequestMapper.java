package com.example.hrms.biz.request.repository;

import com.example.hrms.biz.request.model.Request;
import com.example.hrms.biz.request.model.criteria.RequestCriteria;
import com.example.hrms.enumation.RequestStatusEnum;
import com.example.hrms.enumation.RequestTypeEnum;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface RequestMapper {

    @Select("SELECT request_id, username, department_id, request_type, request_reason, request_status, " +
            "approver_username, start_time, end_time, created_at, updated_at, approved_at " +
            "FROM requests WHERE request_id = #{requestId}")
    Request getRequestById(@Param("requestId") Long requestId);

    @Select("SELECT COUNT(request_id) " +
            "FROM requests r " +
            "WHERE " +
            "    (#{requestId} IS NULL OR r.request_id = #{requestId}) " +
            "    AND (#{username} IS NULL OR r.username = #{username}) " +
            "    AND (#{departmentId} IS NULL OR r.department_id = #{departmentId}) " +
            "    AND (#{requestType} IS NULL OR r.request_type = #{requestType}) " +
            "    AND (#{requestReason} IS NULL OR r.request_reason = #{requestReason}) " +
            "    AND (#{requestStatus} IS NULL OR r.request_status = #{requestStatus}) " +
            "    AND (#{approverUsername} IS NULL OR r.approver_username = #{approverUsername}) " +
            "    AND (#{startTime} IS NULL OR r.start_time >= #{startTime}) " +
            "    AND (#{endTime} IS NULL OR r.end_time <= #{endTime})")
    int count(RequestCriteria criteria);

    @Select("SELECT " +
            "    r.request_id AS requestId, " +
            "    r.username AS username, " +
            "    r.department_id AS departmentId, " +
            "    r.request_type AS requestType, " +
            "    r.request_reason AS requestReason, " +
            "    r.request_status AS requestStatus, " +
            "    r.approver_username AS approverUsername, " +
            "    r.start_time AS startTime, " +
            "    r.end_time AS endTime, " +
            "    r.created_at AS createdAt, " +
            "    r.updated_at AS updatedAt, " +
            "    r.approved_at AS approvedAt " +
            "FROM " +
            "    requests r " +
            "WHERE " +
            "    (#{requestId} IS NULL OR r.request_id = #{requestId}) " +
            "    AND (#{username} IS NULL OR r.username = #{username}) " +
            "    AND (#{departmentId} IS NULL OR r.department_id = #{departmentId}) " +
            "    AND (#{requestType} IS NULL OR r.request_type = #{requestType}) " +
            "    AND (#{requestReason} IS NULL OR r.request_reason = #{requestReason}) " +
            "    AND (#{requestStatus} IS NULL OR r.request_status = #{requestStatus}) " +
            "    AND (#{approverUsername} IS NULL OR r.approver_username = #{approverUsername}) " +
            "    AND (#{startTime} IS NULL OR r.start_time >= #{startTime}) " +
            "    AND (#{endTime} IS NULL OR r.end_time <= #{endTime}) " +
            "ORDER BY " +
            "    r.request_id ASC " +
            "LIMIT #{pageSize} OFFSET #{offset}")
            List<Request> select(@Param("pageSize") int pageSize,
                                 @Param("offset") int offset,
                                 @Param("requestId") Long requestId,
                                 @Param("username") String username,
                                 @Param("departmentId") Long departmentId,
                                 @Param("requestType") RequestTypeEnum requestType,
                                 @Param("requestReason") String requestReason,
                                 @Param("requestStatus") RequestStatusEnum requestStatus,
                                 @Param("approverUsername") String approverUsername,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime);

    @Select("SELECT * FROM requests WHERE department_id = #{departmentId} LIMIT #{pageSize} OFFSET #{offset}")
    List<Request> getRequestsByDepartment(@Param("departmentId") Long departmentId,
                                          @Param("pageSize") int pageSize,
                                          @Param("offset") int offset);

    int countByDepartment(Long departmentId);
    @Update("UPDATE requests SET request_status = #{status}, approved_at = NOW(), approver_username = #{approverUsername} " +
            "WHERE request_id = #{requestId} AND department_id = #{departmentId}")
    int updateRequestStatus(@Param("requestId") Long requestId,
                            @Param("status") String status,
                            @Param("approverUsername") String approverUsername,
                            @Param("departmentId") Long departmentId);

}