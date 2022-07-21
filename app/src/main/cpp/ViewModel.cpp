#include "ViewModel.h"

#include <search.h>

#include <string>

#include "generated/toast_generated.h"
#include "generated/loading_view_generated.h"
#include "log_utils.h"
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

void ViewModel::bind() {
  LOGE("ViewModel::bind()")
}

void ViewModel::unBind() {
  LOGE("ViewModel::unBind()")
}

void ViewModel::handle(const int key, const std::string &value) {
  LOGE("ViewModel handle()")
  if (bind_java_view_model_ == nullptr) {
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
  if (bind_java_view_model_ == nullptr) {
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
  if (bind_java_view_model_ != nullptr) {
    QJniObject jniObject = QJniObject(bind_java_view_model_);
    jniObject.callMethod<void>("setViewModelAttached", "(Z)V", view_model_is_attached_);
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
  jniObject.callMethod<void>("showLoading", "([B)V",
                             ret);
}

void ViewModel::hiddenLoading() {
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
  auto tiger = CreateToastParamsDirect(fb, params.c_str(), 0, toast::Duration_LENGTH_SHORT);
  fb.Finish(tiger);

  // copies it to a Java byte array.
  QJniEnvironment env;
  auto buf = reinterpret_cast<jbyte *>(fb.GetBufferPointer());
  int size = fb.GetSize();
  auto ret = env->NewByteArray(size);
  env->SetByteArrayRegion(ret, 0, fb.GetSize(), buf);
  jniObject.callMethod<void>("showToast", "([B)V", ret);
}

void ViewModel::showCustomToast(const std::string &params) {
  QJniEnvironment env;
}
