package com.gl.hive.TaskService.util;

import com.gl.hive.TaskService.fegin.client.AuthUserFeignClient;
import com.gl.hive.TaskService.fegin.client.ProjectUtilFeignClient;
import com.gl.hive.TaskService.model.entity.Task;
import com.gl.hive.shared.lib.exceptions.HiveException;
import com.gl.hive.shared.lib.exceptions.NotLeaderOfProjectException;
import com.gl.hive.shared.lib.model.dto.ProjectDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTaskValidationUtils {

    private final AuthUserFeignClient authUserFeignClient;
    private final ProjectUtilFeignClient projectUtilFeignClient;
    private final HttpServletRequest httpServletRequest;

    public void handle_UserLeadership(ProjectDTO projectDTO) {
        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUsersDTO_Id = authUserFeignClient.getCurrentUsers_Id(requestHeader);
        if (!projectUtilFeignClient.isLeaderOrAdminOfProject(projectDTO.getProjectId(), currentUsersDTO_Id))
            throw new NotLeaderOfProjectException("👮🏻You are not a leader or admin of THIS project👮🏻", FORBIDDEN, FORBIDDEN.value());
    }


    public void handle_TaskBelongingToProject(Task task, long projectId) {
        if (!task.getProjectId().equals(projectId))
            throw new HiveException("Task with ID '" + task.getTaskId() + "' does not belong to project with ID '" + projectId + "'", BAD_REQUEST, BAD_REQUEST.value());
    }

}
