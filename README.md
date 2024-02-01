
## How To

To get a Git project into your build:

#### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:


```bash
  dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

#### Step 2. Add the dependency

```bash
  dependencies {
	        implementation 'com.github.appsmanager481:Kids-Vision-Ads:1.5'
	}
```
