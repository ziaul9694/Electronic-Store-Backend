package com.cwz.electronic.store.helper;

import com.cwz.electronic.store.dtos.PageableResponse;
import com.cwz.electronic.store.dtos.UserDto;
import com.cwz.electronic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class HelperForPR{

    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){

        List<U> entity = page.getContent();
        List<V> userDtoList = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(userDtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }

}
