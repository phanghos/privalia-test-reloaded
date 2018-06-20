# privalia-test-reloaded
Privalia Technical Test for the Android Developer position

### Features

* Written in **Kotlin**
* Clean architecture using **MVVM** as design pattern with unidirectional data flow a.k.a. **MVI**
* **Architecture Components** (`ViewModel` to survive screen orientation changes and `LiveData` as a lifecycle-aware observable data holder with the observer pattern)
* **RxKotlin / RxAndroid** in order to build the reactive architecture
* **RxBinding** by *Jake Wharton* to further emphasize the reactive approach by having UI events such as click listeners, etc as observables which will be passed to the view model
* **Retrofit** for HTTP requests
* **HttpLoggingInterceptor** to log every HTTP request
* **Dagger** for handling DI easily
* **Mockito** for unit testing

### Some of the advantages of a reactive architecture with unidirectional data flow or MVI

* **Immutability**: For every result (loading, success, error), a new `ViewState` is constructed
* **Testing**: This approach makes testing easier because, in order to test view models, you only need to assert that for a given `UiEvent`, the corresponding `ViewState` is created and rendered
* **Single source of truth for data**: The data is coming from a single point, which makes it a lot easier to debug errors, becuse they're easily traceable

For a more detailed explanation of MVI, check this out: http://hannesdorfmann.com/android/mosby3-mvi-1


# Additional notes

**The test is not 100% complete.** I had no time to unit test every class nor implement any UI test. Furthermore, I would have moved dimensions (for margins, padding, text sizes, etc) to a separated `dimens.xml`, as well as moved hard-coded strings to `strings.xml`. **YES, I KNOW** that commit history is not the best. Also important to empahzize is the fact that I wanted to give a try to MVI, since it's a relatively new architecture in the Android world; so maybe some things could have been implemented in a better way. I just wanted to explore and play with a bit, and get out of the comfort zone of MVP and/or regular MVVM :)

### Thank you very much!
