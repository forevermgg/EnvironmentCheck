#include <jni.h>

#include <iostream>

class ViewModel {
 public:
  ViewModel();
  virtual ~ViewModel();
  virtual void handle(const int key, const std::string &value);
  void setProp(const int key, const std::string &value);
  void showLoading(const std::string &msg);
  void hideLoading();
  void showToast(const std::string &params);
  void showCustomToast(const std::string &params);
  virtual void bind();
  virtual void unBind();
  void setJavaViewModel(jobject bind_java_view_model);
  jobject getJavaViewModel();
  bool isViewModelAttached() const;
  void setViewModelAttached(bool state);

 private:
  jobject bind_java_view_model_ = nullptr;
  bool view_model_is_attached_ = false;
};