package com.example.coupon_core.repository.redis;

import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.exception.ErrorCode;
import com.example.coupon_core.repository.redis.dto.CouponIssueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestKey;
import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestQueueKey;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private RedisScript<String> issueScript = issueRequestScript();
    private final String issueRequestQueueKey = getIssueRequestQueueKey();
    private final ObjectMapper objectMapper;


    public Boolean zAdd(String key, String value, double score){
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score);
    }

    public Long sAdd(String key, String value){
        return redisTemplate.opsForSet().add(key,value);
    }

    public Long sCard(String key){
        return redisTemplate.opsForSet().size(key);
    }

    public Boolean sIsMember(String key, String value){
        return redisTemplate.opsForSet().isMember(key,value);
    }

    public Long rPush(String key, String value){
        return redisTemplate.opsForList().rightPush(key, value);

    }

    public void issueRequest(long couponId, long userId, int totalIssueQuantity){
        String issueRequestKey = getIssueRequestKey(couponId);
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(couponId, userId);

        try{
            String code = redisTemplate.execute(
                    issueScript,
                    List.of(issueRequestKey,issueRequestQueueKey),
                    String.valueOf(userId),
                    String.valueOf(totalIssueQuantity),
                    objectMapper.writeValueAsString(couponIssueRequest)
            );
            CouponIssueRequestCode.checkRequestResult(CouponIssueRequestCode.find(code));
        } catch ( JsonProcessingException e){
            throw new CouponIssueException(ErrorCode.FAIL_COUPON_ISSUE_REQUEST,"input :%s".formatted(issueRequestKey));
        }

    }

    private RedisScript<String> issueRequestScript(){
        String script = """
                if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
                    return '2'
                end
                
                if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then 
                    redis.call('SADD', KEYS[1], ARGV[1])
                    redis.call('RPUSH', KEYS[1], ARGV[3])
                    return '1'
                end
                
                return '3'    
                """;
        return RedisScript.of(script, String.class);
    }
}
