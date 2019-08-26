package org.wuneng.web.postcard.configs;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

@Configuration
public class BloomFilterConfig {

    @Value("${bloomFilter.size}")
    Integer size;

    @Bean("id_bloom_filter")
    public BloomFilter<Integer> id_bloom_filter(){
        System.out.println(size);
        BloomFilter idBloomFilter =BloomFilter.create(Funnels.integerFunnel(),size);
        return idBloomFilter;
    }

    @Bean("stu_id_bloom_filter")
    public BloomFilter<String> stu_id_bloom_filter(){
        BloomFilter sut_id_bloom_filter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf8")),size);
        return sut_id_bloom_filter;
    }

    @Bean("phone_number_bloom_filter")
    public BloomFilter<Long> phone_number_bloom_filter(){
        BloomFilter phone_number_filter = BloomFilter.create(Funnels.longFunnel(),size);
        return phone_number_filter;
    }
}
