#include "ViewModel.h"

#include <search.h>

#include <string>

#include "generated/toast_generated.h"
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

void ViewModel::bind() { LOGE("ViewModel::bind()") }

void ViewModel::unBind() { LOGE("ViewModel::unBind()") }

void ViewModel::handle(const std::string &key, const std::string &value) {
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
  view_model_is_attached_ = true;
}

jobject ViewModel::getJavaViewModel() { return bind_java_view_model_; }

bool ViewModel::isViewModelAttached() const { return view_model_is_attached_; }

void ViewModel::setViewModelAttached(bool state) {
  view_model_is_attached_ = state;
}

void ViewModel::showLoading(const std::string &msg) {
  QJniObject jstring_params = QJniObject::fromString(msg);
  QJniObject jniObject = QJniObject(bind_java_view_model_);
  jniObject.callMethod<void>("showLoading", "(Ljava/lang/String;)V",
                             jstring_params.object<jstring>());
}

void ViewModel::hideLoading() {
  QJniObject jniObject = QJniObject(bind_java_view_model_);
  jniObject.callMethod<void>("hideLoading", "()V");
}

void ViewModel::showToast(const std::string &params) {
  QJniObject jstring_params = QJniObject::fromString(params);
  QJniObject jniObject = QJniObject(bind_java_view_model_);
  jniObject.callMethod<void>("showToast", "(Ljava/lang/String;)V",
                             jstring_params.object<jstring>());

  // create a new animal flatbuffers
  auto fb = FlatBufferBuilder(1024);
  auto tiger = CreateToastParamsDirect(fb, params.c_str(), 300);
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
