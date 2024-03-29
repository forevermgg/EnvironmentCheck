#include "ViewModel.h"

#include <search.h>

#include <string>

#include "generated/loading_view_generated.h"
#include "generated/toast_generated.h"
#include "log/log_utils.h"
#include "log/logging.h"
#include "qjniobject.h"

using namespace com::fbs::app;
using namespace flatbuffers;
ViewModel::ViewModel() {
  std::cout << "ViewModel()" << std::endl;
  LOGE("ViewModel()")
}

ViewModel::~ViewModel() {
  std::cout << "~ViewModel()" << std::endl;
  LOGE("~ViewModel()")
}

void ViewModel::bind() { LOGE("ViewModel::bind()") }

void ViewModel::unBind() { LOGE("ViewModel::unBind()") }

void ViewModel::handle(const int key, const std::string &value) {
  LOGE("ViewModel handle()")
  if (getJavaViewModel() == nullptr) {
    LOGE("bind_java_view_model_ == nullptr")
    return;
  }
  if (!isViewModelAttached()) {
    LOGE("view_model_is_attached_ is false")
    return;
  }
}

void ViewModel::setProp(const int key, const std::string &value) {
  LOGE("ViewModel setProp()")
  if (getJavaViewModel() == nullptr) {
    LOGE("bind_java_view_model_ == nullptr")
    return;
  }
  if (!isViewModelAttached()) {
    LOGE("view_model_is_attached_ is false")
    return;
  }
  QJniObject jstring_value = QJniObject::fromString(value);
  QJniObject jniObject = QJniObject(bind_java_view_model_);
  jniObject.callMethod<void>("setProp", "(ILjava/lang/String;)V", jint(key),
                             jstring_value.object<jstring>());
}

void ViewModel::setJavaViewModel(jobject bind_java_view_model) {
  bind_java_view_model_ = bind_java_view_model;
}

jobject ViewModel::getJavaViewModel() { return bind_java_view_model_; }

bool ViewModel::isViewModelAttached() const { return view_model_is_attached_; }

void ViewModel::setViewModelAttached(bool state) {
  view_model_is_attached_ = state;
  if (bind_java_view_model_ != nullptr && view_model_is_attached_ == true) {
    QJniObject jniObject = QJniObject(bind_java_view_model_);
    jniObject.callMethod<void>("setViewModelAttached", "(Z)V",
                               view_model_is_attached_);
  }
}

void ViewModel::showLoading(const std::string &msg) {
  QJniObject jniObject = QJniObject(bind_java_view_model_);

  // create a new animal flatbuffers
  auto fb = FlatBufferBuilder(1024);
  auto tiger = loading::CreateLoadingViewParamsDirect(fb, msg.c_str(), 1);
  fb.Finish(tiger);

  // copies it to a Java byte array.
  QJniEnvironment env;
  auto buf = reinterpret_cast<jbyte *>(fb.GetBufferPointer());
  int size = fb.GetSize();
  auto ret = env->NewByteArray(size);
  env->SetByteArrayRegion(ret, 0, fb.GetSize(), buf);
  jniObject.callMethod<void>("showLoading", "([B)V", ret);
}

void ViewModel::hiddenLoading() {
  // 必须在主线调用
  /*QJniObject::callStaticMethod<void>("com/mgg/environmentcheck/QtNative",
                                     "runPendingCppRunnablesOnAndroidThread",
                                     "()V");*/
  // Note: The '64' below is the max size of the callables passed to the
  // scheduler. This is done to reduce allocations and make schedulers more
  // efficient.
  QJniObject jniObject = QJniObject(bind_java_view_model_);
  jniObject.callMethod<void>("hiddenLoading", "()V");
}

void ViewModel::showToast(const std::string &params) {
  QJniObject jstring_params = QJniObject::fromString(params);
  QJniObject jniObject = QJniObject(bind_java_view_model_);
  jniObject.callMethod<void>("showToast", "(Ljava/lang/String;)V",
                             jstring_params.object<jstring>());
  // create a new animal flatbuffers
  auto fb = FlatBufferBuilder(1024);
  auto tiger = CreateToastParamsDirect(fb, params.c_str(), 0,
                                       toast::Duration_LENGTH_SHORT);
  fb.Finish(tiger);

  // copies it to a Java byte array.
  QJniEnvironment env;
  auto buf = reinterpret_cast<jbyte *>(fb.GetBufferPointer());
  int size = fb.GetSize();
  auto ret = env->NewByteArray(size);
  env->SetByteArrayRegion(ret, 0, fb.GetSize(), buf);
  jniObject.callMethod<void>("showToast", "([B)V", ret);
}

void ViewModel::showCustomToast(const std::string &params) {}
