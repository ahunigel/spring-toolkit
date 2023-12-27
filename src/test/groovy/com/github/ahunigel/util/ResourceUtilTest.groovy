package com.github.ahunigel.util

import org.springframework.boot.env.OriginTrackedMapPropertySource
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.PropertySource
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.EncodedResource
import spock.lang.Specification

/**
 * Created by nigel on 2023/12/27.
 *
 * @author nigel
 */
class ResourceUtilTest extends Specification {
    def "load yaml property source"() {
        when:
        def result = ResourceUtil.loadPropertySource('yml-test',
                new EncodedResource(new ClassPathResource('test.yml')), new YamlPropertySourceLoader())
        then:
        result != null
        result instanceof PropertySource
        result instanceof OriginTrackedMapPropertySource

        and:
        result.name == 'yml-test'
        result.getProperty('nz.int') == 5
        result.getProperty('nz.float') == 6.0f
        result.getProperty('nz.string') == 'nigel'
    }
}
