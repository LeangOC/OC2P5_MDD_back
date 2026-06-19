package com.openclassrooms.mddapi.service;



import com.openclassrooms.mddapi.dto.TopicDTO;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public List<TopicDTO> getAllTopics() {

        return topicRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private TopicDTO toDTO(Topic topic) {

        return new TopicDTO(
                topic.getId(),
                topic.getName(),
                topic.getDescription()
        );
    }
}