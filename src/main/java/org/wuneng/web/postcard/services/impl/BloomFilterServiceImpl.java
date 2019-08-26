package org.wuneng.web.postcard.services.impl;

import com.google.common.hash.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.services.BloomFilterService;

import javax.annotation.Resource;

@Service
public class BloomFilterServiceImpl implements BloomFilterService {
    @Resource(name = "id_bloom_filter")
    BloomFilter<Integer> id_bloom_filter;

    @Resource(name = "stu_id_bloom_filter")
    BloomFilter<String> stu_id_bloom_filter;

    @Resource(name = "phone_number_bloom_filter")
    BloomFilter<Long> phone_number_bloom_filter;

    @Override
    public boolean id_might_constain(Integer id){
        return id_bloom_filter.mightContain(id);
    }
    @Override
    public void id_put(Integer id){
        id_bloom_filter.put(id);
    }
    @Override
    public boolean stu_id_might_contain(String stu_id){
        return stu_id_bloom_filter.mightContain(stu_id);
    }
    @Override
    public void stu_id_put(String stu_id){
        this.stu_id_bloom_filter.put(stu_id);
    }
    @Override
    public boolean phone_number_might_contain(long phone_number){
        return phone_number_bloom_filter.mightContain(phone_number);
    }
    @Override
    public void phone_number_put(long phone_number){
        phone_number_bloom_filter.put(phone_number);
    }

}
