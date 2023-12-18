package dev.jay.rxjava;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding4.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.jay.rxjava.databinding.ActivityMainBinding;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Unit;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "RxJava";

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        simpleTasks();
//        simpleObserver();
//        createObservable();
        Disposable disposableButton = RxView.clicks(binding.btnFetchData).throttleFirst(1500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Throwable {
                Log.d(TAG, "accept: Button clicked");
                implementNetworkCall();
            }
        });
    }

    private void createObservable() {
        Observable<String> observable = Observable.create(emitter -> {
            emitter.onNext("One");
            emitter.onError(new IllegalArgumentException("Error in observable"));
            emitter.onNext("Two");
            emitter.onComplete();
        });

        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe is called");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext is called: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError is called: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete is called");
            }
        });
    }

    private void simpleObserver() {
        List<String> stringList = new ArrayList<>();
        stringList.add("A");
        stringList.add("B");
        stringList.add("C");

        Observable<String> observable = Observable.fromIterable(stringList);

        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe is called");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext is called: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError is called: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete is called");
            }
        });
    }

    private void simpleTasks() {
        Observable<Task> taskObservable = Observable.fromIterable(DataSource.createTasksList()).subscribeOn(Schedulers.io()).filter(new Predicate<Task>() {
            @Override
            public boolean test(Task task) throws Throwable {
                Log.d(TAG, "test: " + Thread.currentThread().getName());
                Thread.sleep(1000);
                return task.getComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called.");
            }
        });
    }

    private void implementNetworkCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();

        ProductService productService = retrofit.create(ProductService.class);
        Disposable disposableNetwork = productService
                .getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productItems -> {
                            Log.d(TAG, "implementNetworkCall: " + productItems.toString());
                        }, throwable -> {
                            Log.e(TAG, "Throwable: ", throwable);
                        }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}