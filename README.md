# EnvironmentCheck

> android app 允许环境检查，目前包括 cpu/debug/monkey/root/emulator/Vpn/多开等情况进行检测。
> 待完善 hook&sign&riskapp

# 集成

```
implementation 'io.github.forevermgg:CheckEnv:1.0.6'
```

# api

## CheckEnv

## CheckDebug

## CheckRoot

## CheckVpn

- hasVpn(context: Context)

## CheckMonkey

- isUserAMonkey

## CheckMultiOpen

## CheckEmulator

```
fun checkIsRunningInEmulator(callback: EmulatorCheckCallback?) {
    getContext()?.let {
        CheckEmulator.getSingleInstance().readSysProperty(it, callback)
    }
}
```

## CheckCpu

- getCpuCount

# 发布

https://s01.oss.sonatype.org/#stagingProfiles;80eef41a0935dd

# 校验

https://repo1.maven.org/maven2/io/github/forevermgg/
