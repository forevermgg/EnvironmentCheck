#include "common.h"

#include <android/log.h>

namespace FOREVER {
namespace COMMON {
// log标签
#define TAG "Common"
// 定义info信息
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__))

std::string getMyPid() {
  pid_t pid = getpid();
  char str[BUF_SIZE_16];
  sprintf(str, "%d", pid);
  return str;
}

std::string getParentPid() {
  pid_t pid = getppid();
  char str[BUF_SIZE_16];
  sprintf(str, "%d", pid);
  return str;
}

std::string getPackageName(const std::string &pid) {
  if (pid.empty()) {
    return "";
  }

  std::string path = "/proc/" + pid + "/cmdline";
  std::string result = readFile(path);
  if (result.empty()) {
    return "";
  }
  LOGE("getPackageName pid:%s %s %d", result.c_str(), pid.c_str(), getppid());
  return result;
}

int existsFile(const std::string &path) {
  int access_result = access(path.c_str(), F_OK);
  if (access_result == -1) {
    return 0;
  } else {
    return 1;
  }
}

std::string readFile(const std::string &path) {
  if (path.empty()) {
    return "";
  }
  char buf[BUF_SIZE_512];
  FILE *pf;
  if ((pf = fopen(path.c_str(), "r")) == nullptr) {
    return "";
  }
  std::string resultStr;
  while (fgets(buf, sizeof(buf), pf)) {
    resultStr += buf;
  }
  fclose(pf);
  unsigned long size = resultStr.size();
  if (size > 0 && resultStr[size - 1] == '\n') {
    resultStr = resultStr.substr(0, size - 1);
  }
  return resultStr;
}

std::string getBuildInfo64(const char *name) {
  char data[BUF_SIZE_64];
  __system_property_get(name, data);
  return data;
}

std::string getBuildInfo256(const char *name) {
  char data[BUF_SIZE_256];
  __system_property_get(name, data);
  return data;
}

std::string shellExecute(const std::string &cmdStr) {
  char buf[128];
  FILE *pf;
  if ((pf = popen(cmdStr.c_str(), "r")) == nullptr) {
    return "";
  }
  std::string resultStr;
  while (fgets(buf, sizeof(buf), pf)) {
    resultStr += buf;
  }
  pclose(pf);
  unsigned long size = resultStr.size();
  if (size > 0 && resultStr[size - 1] == '\n') {
    resultStr = resultStr.substr(0, size - 1);
  }
  return resultStr;
}
}  // namespace COMMON
}  // namespace FOREVER