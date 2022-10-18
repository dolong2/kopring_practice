package practice.crud.kotlin.global.config.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit


@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun setData(key: String, value: String, expiredTime: Long) {
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS)
    }

    fun getData(key: String?): String? {
        return redisTemplate.opsForValue()[key!!] as String?
    }

    fun deleteData(key: String) {
        redisTemplate.delete(key)
    }
}