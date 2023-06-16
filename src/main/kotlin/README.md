# GitHub Api Project
This project lists all the GitHub repositories (which are not forks) of the hardcoded user. For each one of them it provides the following information:
- repository name,
- owner login,
- every branch name and its last commit SHA.

The project uses https://developer.github.com/v3 as a backing API.

## How it works
### Establishing connection to get repositories data
Firstly `Main.kt` creates an instance of the `Retrofit` service interface with gson converter and implements it using `ApiInterface.kt`. 
Then it defines a request call, enqueues it and waits for response. This is the part where either `onResponse()` or `onFailure()` function is invoked.


### Managing repositories data
Next step is to handle the data in the received response, provided the response `.isSuccessful`. 
Bear in mind that obtained response is `Response<List<DataItem>>`, so its `.body()` is a list of objects of `DataItem` class defined in `DataItem.kt` file.
For every element (repository) in the response, program creates `CoroutineScope` by using `RunBlocking{}`
to make sure that all the information concerning one repository is printed together. 
It is vital because the program has already printed the information about repository name and owner login but only information relating branches is `branches_url`.
The program uses this url and prints the desired branches data by invoking function `getBranches()` defined in `Branch.kt` file. It requires a proper url as an argument. 
`branches_url` is always in format url{/branch} so program simply substrings it by cutting off last 9 characters and passes it to `getBranches()` function.


### Invoking `getBranches()` function
`Branch.kt` is a `suspendCoroutine`. This function firstly creates an instance of the `Retrofit` service interface with gson converter and implements it using `BranchApiInterface.kt`.
Then it defines a request call, enqueues it and waits for response. This is the part where either `onResponse()` or `onFailure()` function is invoked.
So it is time to handle the branches data in the received response, provided the response `.isSuccessful` and not empty.
Bear in mind that obtained response is `Response<List<BranchItem>>`, so its `.body()` is a list of objects of `BranchItem` class defined in `BranchItem.kt` file.
For every element (branch) in the response, program prints out its name and last commit SHA. Then it resumes without exception.
If the response is either not successful or empty, the program resumes with a proper exception. 


## Used technologies
- Kotlin,
- coroutines,
- retrofit2 library with gson converter.


## Things to implement
- arguments in `main()` function, user and header (they are hardcoded right now)
- extract text from `errorBody` in `main()`

