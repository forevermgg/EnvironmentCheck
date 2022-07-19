#define Q_DISABLE_COPY(Class)    \
  Class(const Class &) = delete; \
  Class &operator=(const Class &) = delete;

#define Q_DISABLE_COPY_MOVE(Class) \
  Q_DISABLE_COPY(Class)            \
  Class(Class &&) = delete;        \
  Class &operator=(Class &&) = delete;