#include <dlfcn.h>
#include <jni.h>
#include <malloc.h>
#include <stdio.h>
#include <string.h>
#include <sys/system_properties.h>
#include <unistd.h>

#include <string>

#define BUF_SIZE_1024 1024
#define BUF_SIZE_512 512
#define BUF_SIZE_256 256
#define BUF_SIZE_128 128
#define BUF_SIZE_64 64
#define BUF_SIZE_16 16
#define BUF_SIZE_32 32

namespace FOREVER {
namespace COMMON {

std::string getMyPid();

std::string getParentPid();

std::string getPackageName(const std::string &pid);

int existsFile(const std::string &path);

std::string readFile(const std::string &path);

std::string getBuildInfo64(const char *name);

std::string getBuildInfo256(const char *name);

std::string shellExecute(const std::string &cmdStr);
}  // namespace COMMON
}  // namespace FOREVER