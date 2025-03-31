package com.example.hrms.biz.request.service;

import com.example.hrms.biz.request.model.Request;
import com.example.hrms.biz.request.model.criteria.RequestCriteria;
import com.example.hrms.biz.request.model.dto.RequestDto;
import com.example.hrms.biz.request.repository.RequestMapper;
import com.example.hrms.biz.user.model.User;
import com.example.hrms.biz.user.repository.UserMapper;
import com.example.hrms.common.http.criteria.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class RequestService {
    private final RequestMapper requestMapper;
    private final UserMapper userMapper;

    public RequestService(RequestMapper requestMapper, UserMapper userMapper) {
        this.requestMapper = requestMapper;
        this.userMapper = userMapper;
    }

    public int count(RequestCriteria criteria) {
        log.info("Counting requests with criteria: {}", criteria);
        return requestMapper.count(criteria);
    }

    public List<RequestDto.Resp> list(Page page, RequestCriteria criteria) {
        page.validate();
        log.info("Fetching request list with criteria: {}", criteria);
        try {
            List<Request> requests = requestMapper.select(
                    page.getPageSize(),
                    page.getOffset(),
                    criteria.getRequestId(),
                    criteria.getUsername(),
                    criteria.getDepartmentId(),
                    criteria.getRequestType(),
                    criteria.getRequestReason(),
                    criteria.getRequestStatus(),
                    criteria.getApproverUsername(),
                    criteria.getStartTime(),
                    criteria.getEndTime()
            );
            log.info("Number of requests fetched: {}", requests.size());
            return requests.stream().map(RequestDto.Resp::toResponse).toList();
        } catch (Exception e) {
            log.error("Error fetching request list", e);
            throw new RuntimeException("Could not fetch request list, please try again later.");
        }
    }

    public List<RequestDto.Resp> getRequestsForSupervisor(Page page, Long departmentId) {
        page.validate();
        List<Request> requests = requestMapper.getRequestsByDepartment(departmentId, page.getPageSize(), page.getOffset());
        return requests.stream().map(RequestDto.Resp::toResponse).toList();
    }
    public int countRequestsByDepartment(Long departmentId) {
        return requestMapper.countByDepartment(departmentId);
    }

    public void approveOrRejectRequest(Long requestId, String action, String supervisorUsername) {
        User supervisor = userMapper.getUserByUsername(supervisorUsername);

        if (supervisor == null || !supervisor.isSupervisor()) {
            throw new RuntimeException("User is not a supervisor");
        }

        Request request = requestMapper.getRequestById(requestId);
        if (request == null) {
            throw new RuntimeException("Request not found");
        }

        if (!request.getDepartmentId().equals(supervisor.getDepartmentId())) {
            throw new RuntimeException("Supervisor can only approve/reject requests in their department");
        }

        String newStatus = switch (action.toUpperCase()) {
            case "APPROVE" -> "APPROVED";
            case "REJECT" -> "REJECTED";
            default -> throw new IllegalArgumentException("Invalid action");
        };

        int updated = requestMapper.updateRequestStatus(requestId, newStatus, supervisorUsername, supervisor.getDepartmentId());
        if (updated == 0) {
            throw new RuntimeException("Failed to update request status");
        }
    }


}