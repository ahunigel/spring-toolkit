package com.github.ahunigel.util

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import spock.lang.Specification

import static java.util.Objects.nonNull
import static org.assertj.core.api.Assertions.assertThat

/**
 * Created by nigel on 2020/3/26.
 *
 * @author nigel
 */
class CollectionUtilExTest extends Specification {

    private Collection<String> c1
    private Collection<String> c2
    private List<String> l1
    private List<String> listWithNull
    private Set<String> s1
    private Set<String> setWithNull


    def setup() {
        c1 = Lists.newArrayList('c1e1', 'c1e2')
        c2 = Lists.newArrayList('c2e1', 'c2e2', 'c2e3')
        l1 = Lists.newArrayList('l1e1', 'l1e2', 'l1e3')
        listWithNull = Lists.newArrayList('l2e1', 'l2e2', 'l2e3', 'l2e4', null) //size=6
        listWithNull.add(null)
        s1 = Sets.newHashSet('s1e1', 's1e2', 's1e3', 's1e4')
        setWithNull = Sets.newHashSet('s2e1', 's2e2', null) //size=3
        setWithNull.add(null)
    }


    def "is not empty"() {
        expect:
        assertThat(CollectionUtilEx.isNotEmpty(null)).isFalse()
        assertThat(CollectionUtilEx.isNotEmpty(new ArrayList<>(0))).isFalse()
        assertThat(CollectionUtilEx.isNotEmpty(c1)).isTrue()
        assertThat(CollectionUtilEx.isNotEmpty(listWithNull)).isTrue()
        assertThat(CollectionUtilEx.isNotEmpty(s1)).isTrue()
        assertThat(CollectionUtilEx.isNotEmpty(setWithNull)).isTrue()
    }


    def "null to empty"() {
        when:
        Collection<?> actual = CollectionUtilEx.nullToEmpty(null)
        then:
        assertThat(actual).isEmpty()
    }


    def "null to empty when not null"() {
        when:
        Collection<?> actual = CollectionUtilEx.nullToEmpty(c1)
        then:
        assertThat(actual).isNotEmpty().hasSameSizeAs(c1)
    }


    def "concat null"() {
        when:
        Collection<String> actual = CollectionUtilEx.concat(null)
        then:
        assertThat(actual).isEmpty()
    }


    def "concat with null"() {
        when:
        Collection<String> actual = CollectionUtilEx.concat(c1, null)
        then:
        assertThat(actual).isNotEmpty().hasSameSizeAs(c1)
    }


    def "concat with null element"() {
        when:
        Collection<String> actual = CollectionUtilEx.concat(c1, null, listWithNull, setWithNull)
        then:
        assertThat(actual).isNotEmpty().hasSize(c1.size() + listWithNull.size() + setWithNull.size())
    }


    def "concat without null element"() {
        when:
        Collection<String> actual = CollectionUtilEx.concatWithoutNull(c1, null, listWithNull, setWithNull)
        then:
        assertThat(actual).isNotEmpty().hasSize(c1.size() + listWithNull.size() + setWithNull.size() - 3)
    }


    def "concat and distinct"() {
        when:
        Collection<String> actual = CollectionUtilEx.concatAndDistinct(c1, null, listWithNull, setWithNull)
        then:
        assertThat(actual).isNotEmpty().hasSize(c1.size() + listWithNull.size() + setWithNull.size() - 2)
    }


    def "concat and filter"() {
        when:
        Collection<String> actual = CollectionUtilEx.concatAndFilter(e -> nonNull(e) && e.endsWith('e3'),
                c1, c2, null, l1, listWithNull, null, s1, setWithNull)
        then:
        assertThat(actual).isNotEmpty().hasSize(4)
    }


    def "concat nothing"() {
        when:
        Collection<String> actual = CollectionUtilEx.concat()
        then:
        assertThat(actual).isEmpty()
    }


    def "concat something"() {
        when:
        Collection<String> actual = CollectionUtilEx.concat(c1, c2)
        then:
        assertThat(actual).isNotEmpty().hasSize(c1.size() + c2.size())
    }


    def "concat list and set"() {
        when:
        Collection<String> actual = CollectionUtilEx.concat(c1, l1, s1)
        then:
        assertThat(actual).isNotEmpty().hasSize(c1.size() + l1.size() + s1.size())
    }
}
