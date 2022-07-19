#! /bin/bash
if ! type adb; then
    echo "adb not found"
    echo "check PATH"
else
    echo "============================"
    echo "Android Device Specificatios"
    echo "============================"
    adb wait-\for-device
    echo "> Manufacturer"
    adb shell getprop ro.product.manufacturer
    echo "> Name"
    adb shell getprop ro.product.name
    echo "> Model"
    adb shell getprop ro.product.model
    echo "> Android version"
    adb shell getprop ro.build.version.release
    echo "> Platform"
    adb shell getprop ro.board.platform
    echo "> CPU"
    adb shell getprop ro.product.cpu.abi
    echo "> CPU.abi2"
    adb shell getprop ro.product.cpu.abi2
    echo "> Description"
    adb shell getprop ro.build.description
    echo "> Fingerprint"
    adb shell getprop ro.build.fingerprint
    echo "> GSM Flexversion"
    adb shell getprop ro.gsm.flexversion
    echo "> Locale language"
    adb shell getprop ro.product.locale.language
    echo "> Locale region"
    adb shell getprop ro.product.locale.region
    echo "> Wifi channels"
    adb shell getprop ro.wifi.channels
    echo "> Board platform"
    adb shell getprop ro.board.platform
    echo "> Product board"
    adb shell getprop ro.product.board
    echo "> Display ID"
    adb shell getprop ro.build.display.id
    echo "> Serial number"
    adb get-serialno
    echo "> GSM IMEI"
    adb shell getprop gsm.baseband.imei
    echo "> Version incremental"
    adb shell getprop ro.build.version.incremental
    echo "> Version SDK"
    adb shell getprop ro.build.version.sdk
    echo "> Version codename"
    adb shell getprop ro.build.version.codename
    echo "> Version release"
    adb shell getprop ro.build.version.release
    echo "> Build date"
    adb shell getprop ro.build.date
    echo "> Build type"
    adb shell getprop ro.build.type
    echo "> Build user"
    adb shell getprop ro.build.user
    echo "----------------------------"
    echo "That is all folks."
fi
