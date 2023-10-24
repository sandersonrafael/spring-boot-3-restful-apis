package com.spring3.firstproject.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerMapper {

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <Origin, Destination> Destination parseObject(Origin origin, Class<Destination> destination) {
        return mapper.map(origin, destination);
    }

    public static <Origin, Destination> List<Destination> parseListObjects(List<Origin> origin, Class<Destination> destination) {
        List<Destination> destinationObjects = new ArrayList<>();
        for (Origin item : origin) {
            Destination mappedObject = mapper.map(item, destination);
            destinationObjects.add(mappedObject);
        }
        return destinationObjects;
    }
}
