local stockKey = KEYS[1]
local userKey = KEYS[2]
local userId = ARGV[1]

-- 检查用户是否已购买
if redis.call("EXISTS", userKey) == 1 then
    return 0
end

-- 检查库存
local stock = tonumber(redis.call("GET", stockKey))
if not stock or stock <= 0 then
    return 0
end

-- 扣减库存并标记用户
redis.call("DECR", stockKey)
redis.call("SET", userKey, 1, "EX", 86400)
return 1