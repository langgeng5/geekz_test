package com.test.geekz.helper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MapList {

    private final ModelMapper modelMapper;

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
    }

    public <S, T> List<T> mapSet(Set<S> source, Class<T> targetClass) {
        return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
    }

    public <D, T> CustomPage<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> targetClass) {
        Page<D> customPage = entities.map(objectEntity -> modelMapper.map(objectEntity, targetClass));
        return new CustomPage<D>(customPage);
    }

    // Note:
    // Some data from my hospital api is not complete, so we need to check it
    // api my hospital sometimes return empty string, so check for empty string
    // && !myHospitalResponse.getCity().isEmpty()
    // api my hospital sometimes return whitespace only, so check for whitespace
    // && !myHospitalResponse.getCity().replaceAll("\\s+", "").isEmpty()
    // api my hospital sometimes return address not city only,address consists of
    // city name and province name separated by dash, so check for dash
    // && myHospitalResponse.getCity().contains("-"))
    public Boolean checkString(String str) {
        return str != null && !str.isEmpty() && !str.replaceAll("\\s+", "").isEmpty() && !str.equals("-")
                && !str.contains("xxx") && !str.contains("XXX") && !str.contains("null") && !str.contains("NULL")
                && !str.contains("N/A") && !str.contains("n/a") && !str.contains("NA") && !str.contains("na")
                && !str.contains("x") && !str.contains("X");
    }
}
