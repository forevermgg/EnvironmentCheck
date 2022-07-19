package com.mgg.environmentcheck;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;

public class TestItemViewModel {
	private final MutableLiveData<String> item = new MutableLiveData<>();
	
	private final AtomicReference<Disposable> work = new AtomicReference<>();
	
	public TestItemViewModel(String topicInitial) {
		item.setValue(topicInitial);
		DisposableHelper.set(work, Observable
				.interval((long) (Math.random() * 5 + 1), TimeUnit.SECONDS)
				.map(i -> topicInitial + " " + (int) (Math.random() * 100))
				.subscribe(item::postValue));
	}
	
	public LiveData<String> getItem() {
		return item;
	}
	
	public void cancel() {
		DisposableHelper.dispose(work);
	}
}
