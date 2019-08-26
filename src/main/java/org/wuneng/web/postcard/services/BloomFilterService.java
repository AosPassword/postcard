package org.wuneng.web.postcard.services;

public interface BloomFilterService {

    boolean id_might_constain(Integer id);

    void id_put(Integer id);

    boolean stu_id_might_contain(String stu_id);

    void stu_id_put(String stu_id);

    boolean phone_number_might_contain(long phone_number);

    void phone_number_put(long phone_number);
}
