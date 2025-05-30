package com.gl.hive.TaskService.service.intercommunication;

import com.gl.hive.TaskService.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskInterCommunicationService {

    private final TaskRepository taskRepository;

    public boolean validateTaskExistence(long taskId) {
        return taskRepository.findById(taskId)
                .isPresent();
    }

}
