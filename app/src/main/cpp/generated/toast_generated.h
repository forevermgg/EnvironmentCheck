// automatically generated by the FlatBuffers compiler, do not modify

#ifndef FLATBUFFERS_GENERATED_TOAST_COM_FBS_APP_TOAST_H_
#define FLATBUFFERS_GENERATED_TOAST_COM_FBS_APP_TOAST_H_

#include "flatbuffers/flatbuffers.h"

namespace com {
namespace fbs {
namespace app {
namespace toast {

struct ToastParams;
struct ToastParamsBuilder;

enum Duration : int32_t {
  Duration_LENGTH_SHORT = 0,
  Duration_LENGTH_LONG = 1,
  Duration_MIN = Duration_LENGTH_SHORT,
  Duration_MAX = Duration_LENGTH_LONG
};

inline const Duration (&EnumValuesDuration())[2] {
  static const Duration values[] = {Duration_LENGTH_SHORT,
                                    Duration_LENGTH_LONG};
  return values;
}

inline const char *const *EnumNamesDuration() {
  static const char *const names[3] = {"LENGTH_SHORT", "LENGTH_LONG", nullptr};
  return names;
}

inline const char *EnumNameDuration(Duration e) {
  if (flatbuffers::IsOutRange(e, Duration_LENGTH_SHORT, Duration_LENGTH_LONG))
    return "";
  const size_t index = static_cast<size_t>(e);
  return EnumNamesDuration()[index];
}

struct ToastParams FLATBUFFERS_FINAL_CLASS : private flatbuffers::Table {
  typedef ToastParamsBuilder Builder;
  enum FlatBuffersVTableOffset FLATBUFFERS_VTABLE_UNDERLYING_TYPE {
    VT_CONTENT = 4,
    VT_DURATION = 6,
    VT_SYSTEMSDURATION = 8
  };
  const flatbuffers::String *content() const {
    return GetPointer<const flatbuffers::String *>(VT_CONTENT);
  }
  int32_t duration() const { return GetField<int32_t>(VT_DURATION, 0); }
  com::fbs::app::toast::Duration systemsDuration() const {
    return static_cast<com::fbs::app::toast::Duration>(
        GetField<int32_t>(VT_SYSTEMSDURATION, 0));
  }
  bool Verify(flatbuffers::Verifier &verifier) const {
    return VerifyTableStart(verifier) && VerifyOffset(verifier, VT_CONTENT) &&
           verifier.VerifyString(content()) &&
           VerifyField<int32_t>(verifier, VT_DURATION, 4) &&
           VerifyField<int32_t>(verifier, VT_SYSTEMSDURATION, 4) &&
           verifier.EndTable();
  }
};

struct ToastParamsBuilder {
  typedef ToastParams Table;
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_content(flatbuffers::Offset<flatbuffers::String> content) {
    fbb_.AddOffset(ToastParams::VT_CONTENT, content);
  }
  void add_duration(int32_t duration) {
    fbb_.AddElement<int32_t>(ToastParams::VT_DURATION, duration, 0);
  }
  void add_systemsDuration(com::fbs::app::toast::Duration systemsDuration) {
    fbb_.AddElement<int32_t>(ToastParams::VT_SYSTEMSDURATION,
                             static_cast<int32_t>(systemsDuration), 0);
  }
  explicit ToastParamsBuilder(flatbuffers::FlatBufferBuilder &_fbb)
      : fbb_(_fbb) {
    start_ = fbb_.StartTable();
  }
  flatbuffers::Offset<ToastParams> Finish() {
    const auto end = fbb_.EndTable(start_);
    auto o = flatbuffers::Offset<ToastParams>(end);
    return o;
  }
};

inline flatbuffers::Offset<ToastParams> CreateToastParams(
    flatbuffers::FlatBufferBuilder &_fbb,
    flatbuffers::Offset<flatbuffers::String> content = 0, int32_t duration = 0,
    com::fbs::app::toast::Duration systemsDuration =
        com::fbs::app::toast::Duration_LENGTH_SHORT) {
  ToastParamsBuilder builder_(_fbb);
  builder_.add_systemsDuration(systemsDuration);
  builder_.add_duration(duration);
  builder_.add_content(content);
  return builder_.Finish();
}

inline flatbuffers::Offset<ToastParams> CreateToastParamsDirect(
    flatbuffers::FlatBufferBuilder &_fbb, const char *content = nullptr,
    int32_t duration = 0,
    com::fbs::app::toast::Duration systemsDuration =
        com::fbs::app::toast::Duration_LENGTH_SHORT) {
  auto content__ = content ? _fbb.CreateString(content) : 0;
  return com::fbs::app::toast::CreateToastParams(_fbb, content__, duration,
                                                 systemsDuration);
}

inline const com::fbs::app::toast::ToastParams *GetToastParams(
    const void *buf) {
  return flatbuffers::GetRoot<com::fbs::app::toast::ToastParams>(buf);
}

inline const com::fbs::app::toast::ToastParams *GetSizePrefixedToastParams(
    const void *buf) {
  return flatbuffers::GetSizePrefixedRoot<com::fbs::app::toast::ToastParams>(
      buf);
}

inline bool VerifyToastParamsBuffer(flatbuffers::Verifier &verifier) {
  return verifier.VerifyBuffer<com::fbs::app::toast::ToastParams>(nullptr);
}

inline bool VerifySizePrefixedToastParamsBuffer(
    flatbuffers::Verifier &verifier) {
  return verifier.VerifySizePrefixedBuffer<com::fbs::app::toast::ToastParams>(
      nullptr);
}

inline void FinishToastParamsBuffer(
    flatbuffers::FlatBufferBuilder &fbb,
    flatbuffers::Offset<com::fbs::app::toast::ToastParams> root) {
  fbb.Finish(root);
}

inline void FinishSizePrefixedToastParamsBuffer(
    flatbuffers::FlatBufferBuilder &fbb,
    flatbuffers::Offset<com::fbs::app::toast::ToastParams> root) {
  fbb.FinishSizePrefixed(root);
}

}  // namespace toast
}  // namespace app
}  // namespace fbs
}  // namespace com

#endif  // FLATBUFFERS_GENERATED_TOAST_COM_FBS_APP_TOAST_H_
