#include <jni.h>
#include <pty.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/system_properties.h>
#include <sys/un.h>

#include "linux_syscall_support.h"
#include "logging.h"
#define TAG "MagiskDetector"

static int major = -1;
static int minor = -1;

static inline void scan_mountinfo() {
  FILE *fp = nullptr;
  char line[PATH_MAX];
  char mount_info[] = "/proc/self/mountinfo";
  int fd = sys_open(mount_info, O_RDONLY, 0);
  if (fd < 0) {
    LOGE("cannot open %s", mount_info);
    return;
  }
  fp = fdopen(fd, "r");
  if (fp == NULL) {
    LOGE("cannot open %s", mount_info);
    close(fd);
    return;
  }
  while (fgets(line, PATH_MAX - 1, fp) != NULL) {
    if (strstr(line, "/ /data ") != NULL) {
      sscanf(line, "%*d %*d %d:%d", &major, &minor);
    }
  }
  fclose(fp);
  close(fd);
}

static inline int scan_maps() {
  FILE *fp = nullptr;
  char line[PATH_MAX];
  char maps[] = "/proc/self/maps";
  int fd = sys_open(maps, O_RDONLY, 0);
  if (fd < 0) {
    LOGE("cannot open %s", maps);
    return -1;
  }
  fp = fdopen(fd, "r");
  if (fp == nullptr) {
    LOGE("cannot open %s", maps);
    close(fd);
    return -1;
  }
  while (fgets(line, PATH_MAX - 1, fp) != NULL) {
    if (strchr(line, '/') == nullptr) continue;
    if (strstr(line, " /system/") != nullptr ||
        strstr(line, " /vendor/") != nullptr ||
        strstr(line, " /product/") != nullptr ||
        strstr(line, " /system_ext/") != nullptr) {
      int f;
      int s;
      char p[PATH_MAX];
      sscanf(line, "%*s %*s %*s %x:%x %*s %s", &f, &s, p);
      if (f == major && s == minor) {
        LOGE("Magisk module file %x:%x %s", f, s, p);
        return 1;
      }
    }
  }
  fclose(fp);
  close(fd);
  return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_mgg_checkenv_magisk_MagiskDetector_haveMagicMount(JNIEnv *env,
                                                           jobject thiz) {
  scan_mountinfo();
  if (minor == -1 || major == -1) return -1;
  return scan_maps();
}