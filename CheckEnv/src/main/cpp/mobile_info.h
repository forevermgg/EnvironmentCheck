//
// Created by centforever on 2022/4/20.
//

#ifndef ENVIRONMENTCHECK_MOBILE_INFO_H
#define ENVIRONMENTCHECK_MOBILE_INFO_H

#include <dlfcn.h>
#include <jni.h>
#include <malloc.h>
#include <stdio.h>
#include <string.h>
#include <sys/system_properties.h>
#include <unistd.h>

#include <string>

#include "common.h"

using std::string;

string getBootId();

string getEntropyAvail();

string getPoolSize();

string getReadWakeupThreshold();

string getWriteWakeupThreshold();

string getUuid();

string getURandomMinReseedSecs();

string getKennel();

int checkMoreOpenByUid();

int checkSubstrateBySo();

int frameCheck();

int xHookCheck();

string checkHookByMap();

string checkHookByPackage();

#endif  // ENVIRONMENTCHECK_MOBILE_INFO_H
