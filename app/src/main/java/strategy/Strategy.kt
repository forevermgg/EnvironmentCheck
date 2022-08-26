package strategy

/**
 * 策略模式
 */
interface Strategy<T : AbstractStrategyRequest?, R : AbstractStrategyResponse?> {
    /*
     * 执行策略
     * @param request
     * @return
     */
    fun executeStrategy(request: T): R
}

/**
 * 策略模式抽象入参
 */
abstract class AbstractStrategyRequest

/**
 * 策略模式抽象出参
 */
abstract class AbstractStrategyResponse

/**
 * 范围类
 * @param <T>
</T> */
class Range<T : Comparable<T>?>(private val start: T, private val end: T) {
    private val isIncludeStart = true
    private val isIncludeEnd = false

    /**
     * 判断是否在范围内
     * @param target
     * @return
     */
    fun inRange(target: T): Boolean {
        if (isIncludeStart) {
            if (start !=null && start > target) {
                return false
            }
        } else {
            if (start !=null && start >= target) {
                return false
            }
        }
        if (isIncludeEnd) {
            if (end !=null && end < target) {
                return false
            }
        } else {
            if (end !=null && end <= target) {
                return false
            }
        }
        return true
    }
}

class WeaponStrategyRequest : AbstractStrategyRequest() {
    /**
     * 距离
     */
    private val distance: Int? = null

    public fun getDistance(): Int? {
        return distance
    }

}

abstract class WeaponStrategy : Strategy<WeaponStrategyRequest?, AbstractStrategyResponse?> {
    /**
     * 发现敌人
     */
    protected fun findEnemy() {
        println("发现敌人")
    }

    /**
     * 开枪前的动作
     */
    protected abstract fun preAction()

    /**
     * 开枪
     */
    protected abstract fun shoot()

    /**
     * 获取距离范围
     * @return
     */
    protected abstract fun queryDistanceRange(): Range<Int>?

    /**
     * 整体的动作
     */
    fun kill() {
        findEnemy()
        preAction()
        shoot()
    }

    override fun executeStrategy(request: WeaponStrategyRequest?): AbstractStrategyResponse? {
        if (request != null) {
            println("距离敌人 " + request.getDistance())
            kill()
        }
        return null
    }
}


/**
 * 平底锅
 */
class PanStrategy : WeaponStrategy() {
    override fun preAction() {
        println("二步快速走过去")
    }

    override fun shoot() {
        println("掏出平底锅呼他")
    }

    override fun queryDistanceRange(): Range<Int>? {
        return Range(0, 1)
    }
}

/**
 * 手枪类
 */
class PistolStrategy : WeaponStrategy() {
    override fun preAction() {
        println("快速走过去")
    }

    override fun shoot() {
        println("掏出手枪打他")
    }

    override fun queryDistanceRange(): Range<Int>? {
        return Range(1, 10)
    }
}

/**
 * 步枪
 */
class RifleStrategy : WeaponStrategy() {
    override fun preAction() {
        println("身体蹲下降低后坐力")
        println("掏出步枪")
        println("打开 3 倍镜")
    }

    override fun shoot() {
        println("开枪射击")
    }

    override fun queryDistanceRange(): Range<Int>? {
        return Range(100, 1000)
    }
}

/**
 * 霰弹枪
 */
class ShotgunStrategy : WeaponStrategy() {
    override fun preAction() {
        println("身体站直, 瞄准")
    }

    override fun shoot() {
        println("打一枪算一枪")
    }

    override fun queryDistanceRange(): Range<Int>? {
        return Range(10, 20)
    }
}

/**
 * 狙击枪
 */
class SniperRifleStrategy : WeaponStrategy() {
    override fun preAction() {
        println("趴在草丛里苟着")
        println("掏出狙击枪")
        println("打开 8 倍镜")
    }

    override fun shoot() {
        println("开枪射击")
    }

    override fun queryDistanceRange(): Range<Int>? {
        return Range(1000, Int.MAX_VALUE)
    }
}

/**
 * 冲锋枪
 */
class SubmachineGunStrategy : WeaponStrategy() {
    override fun preAction() {
        println("身体站直, 心态稳住")
    }

    override fun shoot() {
        println("掏出冲锋枪打他")
    }

    override fun queryDistanceRange(): Range<Int>? {
        return Range(20, 100)
    }
}
