package com.gl.hive.TaskService.service.module;

import com.gl.hive.TaskService.fegin.client.AuthUserFeignClient;
import com.gl.hive.TaskService.model.entity.Task;
import com.gl.hive.TaskService.repository.TaskRepository;
import com.gl.hive.TaskService.service.interfaces.TaskProgressService;
import com.gl.hive.TaskService.util.RepositoryUtils;
import com.gl.hive.TaskService.util.TaskUtils;
import com.gl.hive.shared.lib.exceptions.HiveException;
import com.gl.hive.shared.lib.model.enums.TaskStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Service implementation for task progress; marking a task as completed and updating its progress.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProgressServiceImpl implements TaskProgressService {

    private final TaskRepository taskRepository;
    private final TaskUtils taskUtils;
    private final RepositoryUtils repositoryUtils;
    private final AuthUserFeignClient authFeignClient;
    private final HttpServletRequest httpServletRequest;

    /**
     * {@inheritDoc}
     */
    @Override
    public void markTaskAsCompleted(Long taskId, Long projectId, TaskStatus taskStatus) {
        // 1. Find the `Task`, `ProjectDTO` and the current user which is a `UserDTO` (UserDTO & ProjectDTO are from Shared-Lib service)
        Task task = repositoryUtils.find_TaskById_OrElseThrow_ResourceNotFoundException(taskId);
        long projectDTO_Id = repositoryUtils.find_ProjectDTOById_OrElseThrow_ResourceNotFoundException(projectId).getProjectId();

        String requestHeader = httpServletRequest.getHeader(AUTHORIZATION);
        long currentUser_Id = authFeignClient.getCurrentUsers_DTO(requestHeader).getUserId();

        // 2. Validate that the task belongs to the project and the user is a member or leader/admin of the project
        taskUtils.validateTaskAndProject(task, projectDTO_Id, currentUser_Id);

        // 3. Check if the task has already been completed and throw an exception if it has
        if (task.getTaskStatus().equals(TaskStatus.COMPLETED))
            throw new HiveException(
                    "Task has already been completed",
                    BAD_REQUEST,
                    BAD_REQUEST.value()
            );

        // 4. Mark the Task as Completed
        if (taskStatus.equals(TaskStatus.COMPLETED)) {
            // Set the boolean value indicating whether the task was overdue when it was completed
            task.setHasOverdue(task.getDueDate().isBefore(LocalDateTime.now()));
            task.setTaskStatus(taskStatus);
            task.setCompletionDate(LocalDateTime.now());
            taskRepository.saveAndFlush(task);
        } else
            throw new HiveException(
                    "TaskStatus should be only as `COMPLETED`",
                    BAD_REQUEST,
                    BAD_REQUEST.value()
            );
        // TODO: notify the users in that project (including leader and admin) -> NOTIFICATION-SERVICE
    }

}
