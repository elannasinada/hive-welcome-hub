package com.gl.hive.CommentService.service.intercommunication;

import com.gl.hive.CommentService.model.entity.Comment;
import com.gl.hive.CommentService.repository.CommentRepository;
import com.gl.hive.shared.lib.model.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentInterCommunicationService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    public List<CommentDTO> getCommentListAsDTOs_ByProjectId(long projectId) {
        List<Comment> comments = commentRepository.findByCommentedOnProjectId(projectId);

        if (Objects.isNull(comments))
            return List.of(new CommentDTO());

        return comments
                .stream()
                .map(
                        comment -> modelMapper.map(
                                comment,
                                CommentDTO.class
                        )
                ).toList();
    }

}
