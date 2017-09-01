jar cvfm PageManager.jar PageManager.txt com
set frompath=F:\project\myProject\J2SE\PageManager\PageManager_myeclipse\pagemanager1.1
set topath=F:\project\myProject\J2SE\PageManager\PageManager_myeclipse
md %topath%\Pagemanager1.1_package\lib
copy PageManager.jar %topath%\Pagemanager1.1_package
copy %frompath%\lib %topath%\Pagemanager1.1_package\lib
