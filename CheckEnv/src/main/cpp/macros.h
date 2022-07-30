#ifndef FOREVER_COMMON_MACROS_H_
#define FOREVER_COMMON_MACROS_H_

#ifndef FOREVER_USED_ON_EMBEDDER

#define FOREVER_EMBEDDER_ONLY [[deprecated]]

#else  // FOREVER_USED_ON_EMBEDDER

#define FOREVER_EMBEDDER_ONLY

#endif  // FOREVER_USED_ON_EMBEDDER

#define FOREVER_DISALLOW_COPY(TypeName) TypeName(const TypeName&) = delete

#define FOREVER_DISALLOW_ASSIGN(TypeName) \
  TypeName& operator=(const TypeName&) = delete

#define FOREVER_DISALLOW_MOVE(TypeName) \
  TypeName(TypeName&&) = delete;        \
  TypeName& operator=(TypeName&&) = delete

#define FOREVER_DISALLOW_COPY_AND_ASSIGN(TypeName) \
  TypeName(const TypeName&) = delete;              \
  TypeName& operator=(const TypeName&) = delete

#define FOREVER_DISALLOW_COPY_ASSIGN_AND_MOVE(TypeName) \
  TypeName(const TypeName&) = delete;                   \
  TypeName(TypeName&&) = delete;                        \
  TypeName& operator=(const TypeName&) = delete;        \
  TypeName& operator=(TypeName&&) = delete

#define FOREVER_DISALLOW_IMPLICIT_CONSTRUCTORS(TypeName) \
  TypeName() = delete;                                   \
  FOREVER_DISALLOW_COPY_ASSIGN_AND_MOVE(TypeName)

#endif  // FOREVER_COMMON_MACROS_H_
