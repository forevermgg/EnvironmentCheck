// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_LOGGING_H_
#define FOREVER_LOGGING_H_

#include <sstream>

#include "log_level.h"
#include "../macros.h"

namespace FOREVER {

class LogMessageVoidify {
 public:
  void operator&(std::ostream&) {}
};

class LogMessage {
 public:
  LogMessage(LogSeverity severity, const char* file, int line,
             const char* condition);
  ~LogMessage();

  std::ostream& stream() { return stream_; }

 private:
  std::ostringstream stream_;
  const LogSeverity severity_;
  const char* file_;
  const int line_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(LogMessage);
};

// Gets the FML_VLOG default verbosity level.
int GetVlogVerbosity();

// Returns true if |severity| is at or above the current minimum log level.
// LOG_FATAL and above is always true.
bool ShouldCreateLogMessage(LogSeverity severity);

[[noreturn]] void KillProcess();

}  // namespace FOREVER

#define FOREVER_LOG_STREAM(severity)                                   \
  ::FOREVER::LogMessage(::FOREVER::LOG_##severity, __FILE__, __LINE__, \
                        nullptr)                                       \
      .stream()

#define FOREVER_LAZY_STREAM(stream, condition) \
  !(condition) ? (void)0 : ::FOREVER::LogMessageVoidify() & (stream)

#define FOREVER_EAT_STREAM_PARAMETERS(ignored)                         \
  true || (ignored)                                                    \
      ? (void)0                                                        \
      : ::FOREVER::LogMessageVoidify() &                               \
            ::FOREVER::LogMessage(::FOREVER::LOG_FATAL, 0, 0, nullptr) \
                .stream()

#define FOREVER_LOG_IS_ON(severity) \
  (::FOREVER::ShouldCreateLogMessage(::FOREVER::LOG_##severity))

#define FOREVER_LOG(severity) \
  FOREVER_LAZY_STREAM(FOREVER_LOG_STREAM(severity), FOREVER_LOG_IS_ON(severity))

#define FOREVER_CHECK(condition)                                            \
  FOREVER_LAZY_STREAM(::FOREVER::LogMessage(::FOREVER::LOG_FATAL, __FILE__, \
                                            __LINE__, #condition)           \
                          .stream(),                                        \
                      !(condition))

#define FOREVER_VLOG_IS_ON(verbose_level) \
  ((verbose_level) <= ::FOREVER::GetVlogVerbosity())

// The VLOG macros log with negative verbosities.
#define FOREVER_VLOG_STREAM(verbose_level) \
  ::FOREVER::LogMessage(-verbose_level, __FILE__, __LINE__, nullptr).stream()

#define FOREVER_VLOG(verbose_level)                       \
  FOREVER_LAZY_STREAM(FOREVER_VLOG_STREAM(verbose_level), \
                      FOREVER_VLOG_IS_ON(verbose_level))

#ifndef NDEBUG
#define FOREVER_DLOG(severity) FOREVER_LOG(severity)
#define FOREVER_DCHECK(condition) FOREVER_CHECK(condition)
#else
#define FOREVER_DLOG(severity) FOREVER_EAT_STREAM_PARAMETERS(true)
#define FOREVER_DCHECK(condition) FOREVER_EAT_STREAM_PARAMETERS(condition)
#endif

#define FOREVER_UNREACHABLE()                          \
  {                                                    \
    FOREVER_LOG(ERROR) << "Reached unreachable code."; \
    ::FOREVER::KillProcess();                          \
  }

#endif  // FOREVER_LOGGING_H_
