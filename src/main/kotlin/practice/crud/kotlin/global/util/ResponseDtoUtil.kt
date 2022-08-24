package practice.crud.kotlin.global.util

import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration
import org.modelmapper.convention.MatchingStrategies
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
object ResponseDtoUtil {
    private val mapper: ModelMapper = ModelMapper()

    init {
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT
        mapper.configuration.isFieldMatchingEnabled = true
        //mapper.configuration.fieldAccessLevel = Configuration.AccessLevel.PRIVATE
    }

    fun <D, T> mapping(entity: T, dto: Class<D>?): D {
        return mapper.map(entity, dto)
    }

    fun <D, T> mapAll(entities: List<T>, dto: Class<D>?): MutableList<D> {
        return entities.stream()
            .map { entity: T -> mapper.map(entity, dto) }
            .collect(Collectors.toList())
    }
}